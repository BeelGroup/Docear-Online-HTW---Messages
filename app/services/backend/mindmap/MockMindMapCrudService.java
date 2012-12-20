package services.backend.mindmap;

import models.backend.exceptions.NoUserLoggedInException;
import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.JsonNode;

import java.io.File;
import java.io.IOException;

public class MockMindMapCrudService implements MindMapCrudService {
    @Override
    public JsonNode mindMapAsJson(String id) throws NoUserLoggedInException, IOException {
        throw new NotImplementedException();
    }

    @Override
    public File mapTest() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void closeMap(String id) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public JsonNode listMaps(String user) throws IOException {
        throw new NotImplementedException();
    }
}
