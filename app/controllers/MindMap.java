package controllers;

import java.io.IOException;

import models.backend.UserMindmapInfo;
import models.backend.exceptions.NoUserLoggedInException;

import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.backend.mindmap.MindMapCrudService;

@Component
public class MindMap extends Controller {

	@Autowired
	private MindMapCrudService mindMapCrudService;

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

    @Security.Authenticated(Secured.class)
	public Result mapListFromDB() {
		try {
			models.backend.User user = User.getCurrentUser();
			if(user != null) {
				final UserMindmapInfo[] maps = mindMapCrudService.getListOfMindMapsFromUser(user);
				return ok(Json.toJson(maps));
			} else {
				return forbidden("You have to login before retrieving maps");
			}
		} catch (IOException e) {
			final String message = "can't list mindmaps";
			Logger.error(message, e);
			return internalServerError(message);
		}
	}
}