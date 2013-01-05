package controllers;

import java.io.IOException;

import models.backend.UserMindmapInfo;
import models.backend.exceptions.DocearServiceException;

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

	public Result map(final String id) throws DocearServiceException, IOException {
        final JsonNode mindMap = mindMapCrudService.mindMapAsJson(id);
        return ok(mindMap);
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
	public Result mapListFromDB() throws IOException, DocearServiceException {
        models.backend.User user = User.getCurrentUser();
        final UserMindmapInfo[] maps = mindMapCrudService.getListOfMindMapsFromUser(user);
        return ok(Json.toJson(maps));
    }
}