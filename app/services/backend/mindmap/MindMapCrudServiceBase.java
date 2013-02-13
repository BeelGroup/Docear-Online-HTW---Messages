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
	private static Cancellable lockCheckCancellable;
	private static Cancellable mindmapAccessCheckCancellable;
	
	
	
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

	public void startLockCheckThread() {
    	lockCheckCancellable = Akka
    	.system()
    	.scheduler()
    	.schedule(
    			Duration.create(0, TimeUnit.SECONDS), 
    			Duration.create(30, TimeUnit.SECONDS), 
    			new LockChecker());
    }
    
    public void startMindmapAccessCheck() {
    	mindmapAccessCheckCancellable = Akka
    	.system()
    	.scheduler()
    	.schedule(
    			Duration.create(0, TimeUnit.SECONDS), 
    			Duration.create(30, TimeUnit.SECONDS), 
    			new MindMapAccessChecker());
    	
    }
    
    
    private static class LockChecker implements Runnable {
		@Override
		public void run() {
			// TODO check locks
		}
    }
    
    private static class MindMapAccessChecker implements Runnable {
		@Override
		public void run() {
			// TODO check locks
		}
    }
}
