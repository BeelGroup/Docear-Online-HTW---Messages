package services.backend.mindmap;

import models.backend.exceptions.NoUserLoggedInException;
import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.closeQuietly;

@Profile("backendMock")
@Component
public class MockMindMapCrudService implements MindMapCrudService {
    @Override
    public JsonNode mindMapAsJson(String id) throws NoUserLoggedInException, IOException {
        InputStream stream = null;
        JsonNode jsonNode;
        try {
            stream = Play.application().resourceAsStream("rest/v1/map/" + id + ".json");
            if (stream == null) {
                throw new IOException("there is no map with id" + id);
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(stream);
        } finally {
            closeQuietly(stream);
        }
        return jsonNode;
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
