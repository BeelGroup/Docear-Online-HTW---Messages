package services.backend.mindmap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import models.backend.User;
import models.backend.UserMindmapInfo;
import models.backend.exceptions.DocearServiceException;

import org.codehaus.jackson.JsonNode;

import play.libs.Akka;
import akka.actor.Cancellable;
import scala.concurrent.duration.Duration;

public abstract class MindMapCrudServiceBase implements MindMapCrudService {
    @Override
	public JsonNode mindMapAsJson(String id) throws DocearServiceException,
			IOException {
		return null;
	}

	@Override
	public UserMindmapInfo[] getListOfMindMapsFromUser(User user)
			throws IOException {
		return null;
	}

	@Override
	@Deprecated
	public File mapTest() throws IOException {
		return null;
	}

	@Override
	@Deprecated
	public void closeMap(String id) throws IOException {
		
	}
}
