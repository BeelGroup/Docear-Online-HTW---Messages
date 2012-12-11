package controllers;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import static org.apache.commons.lang.BooleanUtils.isFalse;

public class MindMap extends Controller {

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
}