package services.backend.mindmap;

import models.backend.exceptions.NoUserLoggedInException;
import org.codehaus.jackson.JsonNode;

import java.io.File;
import java.io.IOException;

public interface MindMapCrudService {

    /** Obtains a mind map as JSON with a specific id. */
    JsonNode mindMapAsJson(String id) throws NoUserLoggedInException, IOException;

    @Deprecated
    File mapTest() throws IOException;

    @Deprecated
    void closeMap(String id) throws IOException;

    JsonNode listMaps(String user) throws IOException;
}
