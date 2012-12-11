package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import controllers.helpers.ServerMindmapMap;

public class Map extends Controller {

	private final static int MAPS_PER_APPLICATION = 2;
	private final static ServerMindmapMap mindmapServerMap;

	static {
		mindmapServerMap = new ServerMindmapMap(MAPS_PER_APPLICATION,8080);
	}

	public static String createWebserviceUrl(int port) {
		return "http://localhost:"+port+"/rest/v1";
	}


	public static Result map(String id) {
		//get hosting server
		int port = mindmapServerMap.getServerPortForMap(id);
		if(port == -1) { //if not hosted, send to a server
			port = sendMapToDocearInstance(id);
		}

		//get response from server
		String wsUrl = createWebserviceUrl(port);
		Response response = WS.url(wsUrl+"/map/json/"+id).get().get();

		//send map or failure message
		if(response.getStatus() == 200) {
			return ok(response.asJson());
		} else {
			return badRequest(response.getBody());
		}
	}

	private static int sendMapToDocearInstance(String mapId) {
		//find server with capacity
		int port = mindmapServerMap.getServerWithFreeCapacity();
		if(port == -1) { //or start a new instance
			port = startDocearInstance();
		}

		//TODO get real map from file system
		File f = new File(System.getProperty("user.dir")+"/app/files/mindmaps/"+mapId+".mm");

		//send file to server and put in map
		String wsUrl = createWebserviceUrl(port);
		WS.url(wsUrl+"/map")
		.setHeader("Content-Type", "application/octet-stream")
		.setHeader("Content-Deposition", "attachement; filename=\""+mapId+".mm\"")
		.put(f).get();
		mindmapServerMap.put(port, mapId);

		return port;
	}

	public static Result closeMap(String id) {
		int hostingPort = mindmapServerMap.remove(id);
		if(hostingPort == -1) {
			return badRequest("Map is not open");
		}
		Response response = WS.url(createWebserviceUrl(hostingPort)+"/map/"+id).delete().get();
		if(response.getStatus() == 200) {

			if(!mindmapServerMap.hasOpenMaps(hostingPort)) {
				closeDocearInstance(hostingPort);
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
	private static int startDocearInstance() {
		int nextFreePort = mindmapServerMap.getNextAvailablePort();
		ProcessBuilder builder =  new ProcessBuilder();
		builder.environment().put("webservice_port", ""+nextFreePort);
		builder.directory(new File(System.getProperty("user.dir")+"/docear"));
		builder.command(new File(System.getProperty("user.dir")+"/docear/docear.exe").getAbsolutePath());

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
			return -1;
		}

		//wait until webservice can be reached
		boolean isOnline = false;
		while(!isOnline) {
			try {
				Thread.sleep(1000);
				isOnline = WS.url(createWebserviceUrl(nextFreePort)+"/status").get().get().getStatus() == 200;
			} catch (InterruptedException e) {
			} catch (Exception e) {}
		}
		//give docear another 3 seconds to start completly
		//TODO better solution?!!
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}

		return nextFreePort;
	}

	private static boolean closeDocearInstance(int port) {
		String wsUrl = createWebserviceUrl(port);
		Promise<Response> promise = WS.url(wsUrl+"/close").get();
		if(promise.get().getStatus() == 200) {
			mindmapServerMap.remove(port);
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
				try{in.close();}catch(Exception e){};
				try{out.close();}catch(Exception e){};
			}

		}
	}

}
