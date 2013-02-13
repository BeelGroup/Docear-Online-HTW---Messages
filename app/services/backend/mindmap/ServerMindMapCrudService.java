package services.backend.mindmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.backend.User;
import models.backend.UserMindmapInfo;
import models.backend.exceptions.DocearServiceException;
import models.backend.exceptions.NoUserLoggedInException;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import play.Logger;
import play.Play;
import play.libs.Akka;
import play.libs.F;
import play.libs.WS;
import util.backend.ZipUtils;
import scala.concurrent.duration.Duration;

@Profile("backendProd")
@Component
public class ServerMindMapCrudService extends MindMapCrudServiceBase implements MindMapCrudService {
	private static Map<String, String> serverIdToMapIdMap = new HashMap<String, String>();
	
    @Override
    public JsonNode mindMapAsJson(String id) throws DocearServiceException, IOException {
    	super.mindMapAsJson(id);
    	String mindmapId = serverIdToMapIdMap.get(id);
        URL serverUrl = null;
        if(mindmapId == null) { //if not hosted, send to a server
        	Logger.debug("No map for server id " + id + ". Sending to server...");
            serverUrl = sendMapToDocearInstance(id);
            mindmapId = serverIdToMapIdMap.get(id);
        } else {
        	serverUrl = ServerMindmapMap.getInstance().getServerURLForMap(mindmapId);
        	Logger.debug("ServerId: " + id + "; MapId: " + mindmapId + "; url: " +serverUrl.toString());
        }
        String wsUrl = serverUrl.toString();
        WS.Response response = WS.url(wsUrl+"/map/"+mindmapId+"/json").get().get();
        if(response.getStatus() != 200) {
            throw new IOException("couldn't obtain mind map from server. Response code: " + response.getStatus());
        }
        return response.asJson();
    }

    @Override
    public File mapTest() throws IOException {
        User user = new User("alschwank", "05CC18009CCAF1EC07C91C4C85FD57E9");
        return getMindMapFileFromDocearServer(user, "103805");
    }

    @Override
    public void closeMap(String id) throws IOException {
        URL serverUrl = ServerMindmapMap.getInstance().remove(id);
        if(serverUrl == null) {
            throw new IOException("Map is not open");
        }
        WS.Response response = WS.url(serverUrl.toString()+"/map/"+id).delete().get();
        if(response.getStatus() == 200) {
            if(!ServerMindmapMap.getInstance().hasOpenMaps(serverUrl)) {
                closeDocearInstance(serverUrl);
            }
        } else {
            throw new IOException("can't close map");
        }
    }

    @Override
    public UserMindmapInfo[] getListOfMindMapsFromUser(User user) throws IOException {
    	if(user == null) {
    		throw new NullPointerException("user cannot be null");
    	}
    	
        String docearServerAPIURL = "https://api.docear.org/user";
        WS.Response response =  WS.url(docearServerAPIURL + "/" + user.getUsername() + "/mindmaps/")
                .setHeader("accessToken", user.getAccessToken()).get().get();

        BufferedReader br = new BufferedReader (new StringReader(response.getBody().toString()));

        List<UserMindmapInfo> infos = new LinkedList<UserMindmapInfo>();
        for ( String line; (line = br.readLine()) != null; ){
            String[] strings = line.split("\\|#\\|");
            Logger.debug(line);
            UserMindmapInfo info = new UserMindmapInfo(strings[0], strings[1], strings[2], strings[3], strings[4]);
            infos.add(info);
        }
        
        return infos.toArray(new UserMindmapInfo[0]);
        //return toJson(infos);
    }

    private static URL sendMapToDocearInstance(String mapId) throws NoUserLoggedInException {
        //find server with capacity
        URL serverUrl = ServerMindmapMap.getInstance().getServerWithFreeCapacity();
        if(serverUrl == null) { //or start a new instance
            serverUrl = startDocearInstance();
        }

        
        InputStream fileStream = null;
        String mmId = null;
        if(mapId.length() == 1) { //test map
        	mmId = mapId;
            fileStream = Play.application().resourceAsStream("mindmaps/"+mapId+".mm");
        } else { //map from user account
            try {
                User user = controllers.User.getCurrentUser();
                if(user == null)
                    throw new NoUserLoggedInException();
                
            	File mmFile = getMindMapFileFromDocearServer(user, mapId);
            	if(mmFile == null)
            		return null;
            	
            	//TODO just a hack, because we are currently using different ids for retrieval then supposed
            	mmId = getMapIdFromFile(mmFile);
                fileStream = new FileInputStream(mmFile);
            } catch (FileNotFoundException e) {
                Logger.error("can't find mindmap file", e);
            } catch (IOException e) {
                Logger.error("can't open mindmap file", e);
            }
        }

        serverIdToMapIdMap.put(mapId, mmId);

        //send file to server and put in map
        String wsUrl = serverUrl.toString();
        WS.url(wsUrl+"/map")
                .setHeader("Content-Type", "application/octet-stream")
                .setHeader("Content-Deposition", "attachement; filename=\""+mapId+".mm\"")
                .put(fileStream).get();
        ServerMindmapMap.getInstance().put(serverUrl, mmId);
        
        return serverUrl;
    }

    @Deprecated
    public static String createWebserviceUrl(int port) {
        return "http://localhost:"+port+"/rest/v1";
    }

    /**
     *
     * @return port of new server or -1 on failure
     */
    private static URL startDocearInstance() {
        int nextFreePort = ServerMindmapMap.getInstance().getNextAvailablePort();
        String docearPath = Play.application().configuration().getString("backend.docearDirectory");
        ProcessBuilder builder =  new ProcessBuilder();
        builder.environment().put("webservice_port", ""+nextFreePort);
        builder.directory(new File(docearPath));
        builder.command(new File(docearPath+"/docear").getAbsolutePath());

        try {
            Process p = builder.start();

            //Streams must be read, otherwise will the application pause to execute
            Akka.system().scheduler()
                    .scheduleOnce(Duration.create(0, TimeUnit.SECONDS),
                            new StreamLogger(p.getInputStream(),
                                    "docear in"));

            Akka.system().scheduler()
                    .scheduleOnce(Duration.create(0, TimeUnit.SECONDS),
                            new StreamLogger(p.getErrorStream(),
                                    "docear in"));

//			Thread t = new Thread(new Transporter(p.getInputStream(), System.out));
//			t.setDaemon(true);
//			t.start();

//			t = new Thread(new Transporter(p.getErrorStream(), System.err));
//			t.setDaemon(true);
//			t.start();

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
        F.Promise<WS.Response> promise = WS.url(wsUrl+"/close").get();
        if(promise.get().getStatus() == 200) {
            ServerMindmapMap.getInstance().remove(serverUrl);
            return true;
        }
        return false;
    }

    /**
     * retrieves a mindmap from the server
     * @param user 
     * @param mapId
     * @return .mm-file or null on failure
     */
    private static File getMindMapFileFromDocearServer(final User user, final String mmIdOnServer) throws IOException {
        String docearServerAPIURL = "https://api.docear.org/user";

        WS.Response response =  WS.url(docearServerAPIURL + "/" + user.getUsername() + "/mindmaps/" + mmIdOnServer)
                .setHeader("accessToken", user.getAccessToken())
                .get().get();
        
        if(response.getStatus() == 200) {
        	return ZipUtils.extractMindmap(response.getBodyAsStream());
        } else {
        	return null;
        }
        	
    }

    private static class StreamLogger implements Runnable {
        private final BufferedReader in;
        private final String prefix;

        public StreamLogger(InputStream in, String prefix) {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.prefix = prefix;
        }

        @Override
        public void run() {
            String line;
            try {
                while((line = in.readLine()) != null) {
                    Logger.info(prefix+": "+line);
                }
            } catch (Exception e) {
                Logger.trace(prefix+": "+"Error!", e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }
    
    private static String getMapIdFromFile(File mindmapFile) {
		try {
			DocumentBuilderFactory dbFactory =  DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mindmapFile);

			doc.getDocumentElement().normalize();

			return doc.getDocumentElement().getAttribute("dcr_id");

		} catch (Exception e) {}


		return null;
	}
}
