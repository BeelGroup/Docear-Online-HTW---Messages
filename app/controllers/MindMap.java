package controllers;

import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.backend.mindmap.MindMapCrudService;
import services.backend.mindmap.MockMindMapCrudService;
import services.backend.mindmap.ServerMindMapCrudService;
import models.backend.exceptions.NoUserLoggedInException;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.*;

import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.apache.commons.lang.BooleanUtils.isTrue;

@Component
public class MindMap extends Controller {

    @Autowired
    private MindMapCrudService mindMapCrudService;

    @Deprecated //TODO does anybody use this method?
    public Result index(final String path) {
		final boolean proxyRequests = isFalse(Play.application().configuration().getBoolean("backend.mock"));
		Result result;
		if(proxyRequests) {
			result = responseWithWebserviceCallBackend(path);
		} else {
			result = responseWithExampleInConfFolder(path);
		}
		return result;
	}

    @Deprecated //TODO does anybody use this method?
	private Result responseWithWebserviceCallBackend(final String path) {
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

    @Deprecated //TODO does anybody use this method?
	private Result responseWithExampleInConfFolder(String path) {
		final String assetPath = path.substring(1);//remove first slash in path
		return ok(Play.application().resourceAsStream(assetPath)).as("application/json");
	}

	public Result map(final String id) {
        try {
            final JsonNode mindMap = mindMapCrudService.mindMapAsJson(id);
            return ok(mindMap);
        } catch (NoUserLoggedInException e) {
            final String message = "user not logged in";
            Logger.debug(message, e);
            return unauthorized(message);//TODO Michael replace with flash message and login page
        } catch (IOException e) {
            final String message = "can't load mind map";
            Logger.error(message, e);
            return internalServerError(message);
        }
	}


    @Deprecated
	public Result closeMap(String id) {
        try {
            mindMapCrudService.closeMap(id);
            return ok();
        } catch (IOException e) {
            final String message = "can't close mind map";
            Logger.error(message, e);
            return internalServerError(message);
        }
	}

    @Deprecated
	public Result mapTest() {
        Result result;
        try {
            result = ok(mindMapCrudService.mapTest());
        } catch (IOException e) {
            final String message = "can't open mindmap";
            Logger.error(message, e);
            result = internalServerError(message);
        }
        return result;
	}

	public Result mapListFromDB(final String user) {
        try {
            final JsonNode maps = mindMapCrudService.listMaps(user);
            return ok(maps);
        } catch (IOException e) {
            final String message = "can't list mindmaps";
            Logger.error(message, e);
            return internalServerError(message);
        }
    }
}