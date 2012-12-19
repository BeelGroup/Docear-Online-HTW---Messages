package controllers;

import static org.apache.commons.lang.BooleanUtils.isFalse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import backend.ServerMindmapMap;

public class MindMap extends Controller {
	private final static ServerMindmapMap mindmapServerMap;

	static {
		int mapsPerInstance = Play.application().configuration().getInt("backend.mapsPerInstance");
		boolean useSingleDocearInstance = Play.application().configuration().getBoolean("backend.useSingleInstance");

		mindmapServerMap = new ServerMindmapMap(mapsPerInstance,8080);

		if(useSingleDocearInstance) {
			try {
				//URL docear2 = new URL("http://docear2.f4.htw-berlin.de:8080/rest/v1");
				URL docear2 = new URL("http://localhost:8080/rest/v1");
				mindmapServerMap.put(docear2, "5");
				mindmapServerMap.remove("5");
			} catch (MalformedURLException e) {
			}
		}
	}

	public static Result index(final String path) {
		final boolean proxyRequests = isFalse(Play.application().configuration().getBoolean("backend.mock"));
		Result result;
		if(proxyRequests) {
			result = responseWithWebserviceCallBackend(path);
		} else {
			result = responseWithExampleInConfFolder(path);
		}
		return result;
	}

	private static Result responseWithWebserviceCallBackend(final String path) {
		final String url = Play.application().configuration().getString("backend.url") + path;
		return async(
				WS.url(url).get().map(
						new F.Function<WS.Response, Result>() {
							public Result apply(WS.Response response) {
								Logger.debug("webservice call: url=" + url + ", responseCode=" + response.getStatus());
								if (response.getStatus() == 200) {
									return ok(response.getBody()).as("application/json");
								} else if (response.getStatus() == 404) {
									return notFound("could not found " + url);
								} else {
									return badRequest();
								}
							}
						}
						).recover(new F.Function<Throwable, Result>() {
							@Override
							public Result apply(Throwable throwable) throws Throwable {
								Logger.error("webserviceCall", throwable);
								return notFound("could not load " + url + ", maybe server ist not running");
							}
						})
				);
	}

	private static Result responseWithExampleInConfFolder(String path) {
		final String assetPath = path.substring(1);//remove first slash in path
		return ok(Play.application().resourceAsStream(assetPath)).as("application/json");
	}

	public static String createWebserviceUrl(int port) {
		return "http://localhost:"+port+"/rest/v1";
	}


	public static Result map(final String id) {
		//get hosting server
		URL serverUrl = mindmapServerMap.getServerURLForMap(id);
		if(serverUrl == null) { //if not hosted, send to a server
			serverUrl = sendMapToDocearInstance(id);
		}

		//get response from server
		String wsUrl = serverUrl.toString();
		Response response = WS.url(wsUrl+"/map/json/"+id).get().getWrappedPromise().await(3, TimeUnit.MINUTES).get();

		//send map or failure message
		if(response.getStatus() == 200) {
			return ok(response.asJson());
		} else {
			mindmapServerMap.remove(id);
			return map(id);
			//return badRequest(response.getBody());
		}
	}

	private static URL sendMapToDocearInstance(String mapId) {
		//find server with capacity
		URL serverUrl = mindmapServerMap.getServerWithFreeCapacity();
		if(serverUrl == null) { //or start a new instance
			serverUrl = startDocearInstance();
		}

		//TODO get real map from file system
		InputStream fileStream = Play.application().resourceAsStream("mindmaps/"+mapId+".mm");
		//File f = new File(System.getProperty("user.dir")+"/app/files/mindmaps/"+mapId+".mm");

		//send file to server and put in map
		String wsUrl = serverUrl.toString();
		WS.url(wsUrl+"/map")
		.setHeader("Content-Type", "application/octet-stream")
		.setHeader("Content-Deposition", "attachement; filename=\""+mapId+".mm\"")
		.put(fileStream).getWrappedPromise().await(3,TimeUnit.MINUTES).get();
		mindmapServerMap.put(serverUrl, mapId);

		return serverUrl;
	}

	public static Result closeMap(String id) {
		URL serverUrl = mindmapServerMap.remove(id);
		if(serverUrl == null) {
			return badRequest("Map is not open");
		}

		Response response = WS.url(serverUrl.toString()+"/map/"+id).delete().get();
		if(response.getStatus() == 200) {

			if(!mindmapServerMap.hasOpenMaps(serverUrl)) {
				closeDocearInstance(serverUrl);
			}

			return ok();
		} else {
			return badRequest();
		}

	}

	/**
	 * 
	 * @return port of new server or -1 on failure
	 */
	private static URL startDocearInstance() {
		int nextFreePort = mindmapServerMap.getNextAvailablePort();
		String docearPath = Play.application().configuration().getString("backend.docearDirectory");
		ProcessBuilder builder =  new ProcessBuilder();
		builder.environment().put("webservice_port", ""+nextFreePort);
		builder.directory(new File(docearPath));
		builder.command(new File(docearPath+"/docear").getAbsolutePath());

		try {
			Process p = builder.start();

			//Streams must be read, otherwise will the application pause to execute
			Thread t = new Thread(new Transporter(p.getInputStream(), System.out));
			t.setDaemon(true);
			t.start();

			t = new Thread(new Transporter(p.getErrorStream(), System.err));
			t.setDaemon(true);
			t.start();

		} catch (IOException e) {
			return null;
		}

		//wait until webservice can be reached
		URL wsUrl = null;
		try {
			wsUrl = new URL(createWebserviceUrl(nextFreePort));
		} catch (MalformedURLException e1) {

		}
		boolean isOnline = false;
		while(!isOnline) {
			try {
				Thread.sleep(1000);
				isOnline = WS.url(wsUrl.toString()+"/status").get().get().getStatus() == 200;
			} catch (InterruptedException e) {
			} catch (Exception e) {}
		}
		//give docear another 3 seconds to start completely
		//TODO better solution?!!
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}

		return wsUrl;
	}

	private static boolean closeDocearInstance(URL serverUrl) {
		String wsUrl = serverUrl.toString();
		Promise<Response> promise = WS.url(wsUrl+"/close").get();
		if(promise.get().getStatus() == 200) {
			mindmapServerMap.remove(serverUrl);
			return true;
		}
		return false;
	}



	private static class Transporter implements Runnable {
		private final InputStreamReader in;
		private final OutputStreamWriter out;

		public Transporter(InputStream in, OutputStream out) {
			this.in = new InputStreamReader(in);
			this.out = new OutputStreamWriter(out);

		}

		@Override
		public void run() {
			char[] buffer = new char[1024];
			int length;
			try {
				while((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
				}
			} catch (Exception e) {
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}
}