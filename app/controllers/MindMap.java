package controllers;

import services.backend.ServerMindmapMap;
import models.backend.User;
import models.backend.UserMindmapInfo;
import util.backend.ZipUtils;
import models.backend.exceptions.NoUserLoggedInException;
import org.apache.commons.io.IOUtils;
import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.BooleanUtils.isFalse;
import static play.libs.Json.toJson;

public class MindMap extends Controller {
	private final static ServerMindmapMap mindmapServerMap;

	static {
        final Configuration conf = Play.application().configuration();
        int mapsPerInstance = conf.getInt("backend.mapsPerInstance");
		boolean useSingleDocearInstance = conf.getBoolean("backend.useSingleInstance");
        final Integer port = conf.getInt("backend.port");
        mindmapServerMap = new ServerMindmapMap(mapsPerInstance, port);

		if(useSingleDocearInstance) {
            try {
                final String protocol = conf.getString("backend.scheme");
                final String host = conf.getString("backend.host");
                final String path = conf.getString("backend.v10.pathprefix");
                URL docear2 = new URL(protocol, host, port, path);
				mindmapServerMap.put(docear2, "5");//TODO what does that mean? #magicnumber
				mindmapServerMap.remove("5");//TODO what does that mean? #magicnumber
			} catch (MalformedURLException e) {
                throw new RuntimeException("cannot read backend url, check your configuration", e);
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
			try {
				serverUrl = sendMapToDocearInstance(id);
			} catch (NoUserLoggedInException e) {
				return unauthorized("no user is logged in");
			}
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

    //TODO backend team: I don't understand the control flow in this method, and does this method two different things???
	private static URL sendMapToDocearInstance(String mapId) throws NoUserLoggedInException {
		//find server with capacity
		URL serverUrl = mindmapServerMap.getServerWithFreeCapacity();
		if(serverUrl == null) { //or start a new instance
			serverUrl = startDocearInstance();
		}


		User user = Application.getCurrentUser();
		if(user == null && mapId.length() > 1)
			throw new NoUserLoggedInException();

		InputStream fileStream = null;
		if(mapId.length() == 1) {
			fileStream = Play.application().resourceAsStream("mindmaps/"+mapId+".mm");
		} else {
			try {
				fileStream = new FileInputStream(mapFromDB(user, mapId));
			} catch (FileNotFoundException e) {
				Logger.error("can't find mindmap file", e);
			} catch (IOException e) {
                Logger.error("can't open mindmap file", e);
            }
        }

		//send file to server and put in map
		String wsUrl = serverUrl.toString();
		WS.url(wsUrl+"/map")
		.setHeader("Content-Type", "application/octet-stream")
		.setHeader("Content-Deposition", "attachement; filename=\""+mapId+".mm\"")
		.put(fileStream).getWrappedPromise().await(10,TimeUnit.SECONDS).get();
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

	private static File unZipIt(InputStream bodyStream) throws IOException {

		File folder = new File("D:\\Temp\\dcr2");//TODO backend team, this is not platform independent

		Logger.debug("unpacking zip");
		ZipUtils.extractMindmap(bodyStream);

		Logger.debug("scanning files");
		File[] files = folder.listFiles();
		File mindmapFile = null;
		for(File file : files) {
			if(file.getName().endsWith(".mm"))
				mindmapFile = file;
			else{
				file.delete();
			}
		}

		Logger.debug("return file: "+mindmapFile.getName());
		return mindmapFile;
	}

	public static Result mapTest() {
        Result result;
        try {
            User user = new User("alschwank", "05CC18009CCAF1EC07C91C4C85FD57E9");
            File mmFile = mapFromDB(user, "103805");
            result = ok(mmFile);
        } catch (IOException e) {
            final String message = "can't open mindmap";
            Logger.error(message, e);
            result = internalServerError(message);
        }
        return result;
	}

	private static File mapFromDB(final User user, final String id) throws IOException {
		String docearServerAPIURL = "https://api.docear.org/user";

		//		play.api.libs.ws.WS.WSRequestHolder request = 
		//				play.api.libs.ws.WS.url(docearServerAPIURL + "/" + user.getUsername() + "/mindmaps/" + id);

		util.backend.WS.Response response =  util.backend.WS.url(docearServerAPIURL + "/" + user.getUsername() + "/mindmaps/" + id)
				.setHeader("accessToken", user.getAccesToken())
				//.setHeader("Content-Disposition", "attachment; filename=test_5.mm.zip")
				//.setHeader("Accept-Charset","utf-8")
				.get().getWrappedPromise().await(3, TimeUnit.MINUTES).get();


		return ZipUtils.extractMindmap(response.getBodyAsStream());
	}

	public static Result mapListFromDB(final String user) {
		String docearServerAPIURL = "https://api.docear.org/user";
		Response response =  WS.url(docearServerAPIURL + "/" + user + "/mindmaps/")
				.setHeader("accessToken", "").get()
				.getWrappedPromise().await(3,TimeUnit.MINUTES).get();

		BufferedReader br = new BufferedReader (new StringReader(response.getBody().toString()));

		List<UserMindmapInfo> infos = new LinkedList<UserMindmapInfo>();
		try {
			for ( String line; (line = br.readLine()) != null; ){
				String[] strings = line.split("\\|#\\|");
				UserMindmapInfo info = new UserMindmapInfo(strings[0], strings[1], strings[2], strings[3], strings[4]);
				infos.add(info);
			}
		} catch (IOException e) {
			Logger.error(e.getMessage());
			return badRequest("Error while parsing response.");
		}

		//send user map infos or failure message
		if(response.getStatus() == 200) {
			return ok(toJson(infos));
		} else {
			return badRequest(response.getBody());
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