package controllers;

import static org.apache.commons.lang.BooleanUtils.isFalse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.*;
import java.util.concurrent.TimeUnit;

import backend.UserMindmapInfo;
import org.apache.commons.io.IOUtils;

import static play.libs.Json.toJson;
import play.Logger;
import play.Play;
import play.api.libs.json.Json;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Http.*;
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
                URL docear2 = new URL("http://docear2.f4.htw-berlin.de:8080/rest/v1");
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

    private static String unZipIt(String body){

        byte[] buffer = new byte[1024];

        try{
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(IOUtils.toInputStream(body));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                Logger.debug("fileName: " + fileName);
                if (fileName.endsWith("mm")){
                    Logger.debug("FOUND MM.");
                    ByteArrayOutputStream fos = new ByteArrayOutputStream();

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                    zis.closeEntry();
                    zis.close();
                    return fos.toString();
                }
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }
        return "empty";
    }

    public static Result mapFromDB(final String user, final String id) {
        if (mindmapServerMap.containsServerURLForMap(id)){
            return map(id);
        } else {
            String docearServerAPIURL = "https://api.docear.org/user";
            Response response =  WS.url(docearServerAPIURL + "/" + user + "/mindmaps/" + id)
                    .setHeader("accessToken", "05CC18009CCAF1EC07C91C4C85FD57E9")
                    //.setHeader("Content-Disposition", "attachment; filename=test_5.mm.zip")
                    //.setHeader("Content-Type","application/octet-stream")
                    .get().getWrappedPromise().await(3, TimeUnit.MINUTES).get();

            Logger.debug("body: " + response.getBody());

            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("output"));
                out.write(response.getBody().toString());
                out.close();
            }
            catch (IOException e)
            {
                Logger.error(e.getMessage());
            }


            //send map or failure message
            if(response.getStatus() == 200) {
                return ok();
            } else {
                return badRequest(response.getBody());
            }
        }
    }

    public static Result mapListFromDB(final String user) {
        String docearServerAPIURL = "https://api.docear.org/user";
        Response response =  WS.url(docearServerAPIURL + "/" + user + "/mindmaps/")
                .setHeader("accessToken", "05CC18009CCAF1EC07C91C4C85FD57E9").get()
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