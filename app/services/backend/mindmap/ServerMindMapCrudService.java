package services.backend.mindmap;

import akka.util.Duration;
import controllers.Application;
import models.backend.User;
import models.backend.UserMindmapInfo;
import models.backend.exceptions.NoUserLoggedInException;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import play.Logger;
import play.Play;
import play.libs.Akka;
import play.libs.F;
import play.libs.WS;
import util.backend.ZipUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static play.libs.Json.toJson;

@Profile("backendProd")
@Component
public class ServerMindMapCrudService implements MindMapCrudService {
    @Override
    public JsonNode mindMapAsJson(String id) throws NoUserLoggedInException, IOException {
        URL serverUrl = ServerMindmapMap.getInstance().getServerURLForMap(id);
        if(serverUrl == null) { //if not hosted, send to a server
            serverUrl = sendMapToDocearInstance(id);
        }
        String wsUrl = serverUrl.toString();
        WS.Response response = WS.url(wsUrl+"/map/json/"+id).get().getWrappedPromise().await(3, TimeUnit.MINUTES).get();
        if(response.getStatus() != 200) {
            throw new IOException("couldn't obtain mind map from server. Response code: " + response.getStatus());
        }
        return response.asJson();
    }

    @Override
    public File mapTest() throws IOException {
        User user = new User("alschwank", "05CC18009CCAF1EC07C91C4C85FD57E9");
        return getMindMapFile(user, "103805");
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
    public JsonNode listMaps(String user) throws IOException {
        String docearServerAPIURL = "https://api.docear.org/user";
        WS.Response response =  WS.url(docearServerAPIURL + "/" + user + "/mindmaps/")
                .setHeader("accessToken", "").get()
                .getWrappedPromise().await(3,TimeUnit.MINUTES).get();

        BufferedReader br = new BufferedReader (new StringReader(response.getBody().toString()));

        List<UserMindmapInfo> infos = new LinkedList<UserMindmapInfo>();
        for ( String line; (line = br.readLine()) != null; ){
            String[] strings = line.split("\\|#\\|");
            UserMindmapInfo info = new UserMindmapInfo(strings[0], strings[1], strings[2], strings[3], strings[4]);
            infos.add(info);
        }
        return toJson(infos);
    }


    //TODO backend team: I don't understand the control flow in this method, and does this method two different things???
    private static URL sendMapToDocearInstance(String mapId) throws NoUserLoggedInException {
        //find server with capacity
        URL serverUrl = ServerMindmapMap.getInstance().getServerWithFreeCapacity();
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
                fileStream = new FileInputStream(getMindMapFile(user, mapId));
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
        ServerMindmapMap.getInstance().put(serverUrl, mapId);

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
    private static File getMindMapFile(final User user, final String id) throws IOException {
        String docearServerAPIURL = "https://api.docear.org/user";

        util.backend.WS.Response response =  util.backend.WS.url(docearServerAPIURL + "/" + user.getUsername() + "/mindmaps/" + id)
                .setHeader("accessToken", user.getAccesToken())
                .get().getWrappedPromise().await(3, TimeUnit.MINUTES).get();
        return ZipUtils.extractMindmap(response.getBodyAsStream());
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
}
