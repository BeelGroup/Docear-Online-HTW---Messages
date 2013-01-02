package services.backend.mindmap;

import java.io.File;
import java.io.IOException;

import models.backend.User;
import models.backend.exceptions.NoUserLoggedInException;

import org.codehaus.jackson.JsonNode;

public interface MindMapCrudService {

    /** Obtains a mind map as JSON with a specific id. */
    JsonNode mindMapAsJson(String id) throws NoUserLoggedInException, IOException;

    @Deprecated
    File mapTest() throws IOException;

    @Deprecated
    void closeMap(String id) throws IOException;

    JsonNode listMaps(String user) throws IOException;
}
