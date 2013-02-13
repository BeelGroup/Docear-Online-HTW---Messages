package services.backend.mindmap;

import java.io.File;
import java.io.IOException;

import models.backend.User;
import models.backend.UserMindmapInfo;
import models.backend.exceptions.DocearServiceException;

import org.codehaus.jackson.JsonNode;

public interface MindMapCrudService {

    /** Obtains a mind map as JSON with a specific id. */
    JsonNode mindMapAsJson(String id) throws DocearServiceException, IOException;
    
    UserMindmapInfo[] getListOfMindMapsFromUser(User user) throws IOException;

    @Deprecated
    File mapTest() throws IOException;

    @Deprecated
    void closeMap(String id) throws IOException;
}
