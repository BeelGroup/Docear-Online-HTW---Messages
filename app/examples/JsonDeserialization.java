package examples;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonDeserialization {
    public static String serializedMindMap() throws IOException {
        final MindMap root = new MindMap("root");
        final MindMap childLeft2 = new MindMap("left 2");
        childLeft2.setLeftChildren(Arrays.asList(new MindMap("left 2.1"), new MindMap("left 2.2")));
        childLeft2.setRightChildren(Arrays.asList(new MindMap("left right 2.1")));
        root.setLeftChildren(Arrays.asList(new MindMap("left 1"), childLeft2));
        ObjectMapper mapper = new ObjectMapper();
        final ObjectNode serialized = serialize(mapper, root);
        return toPrettyString(mapper, serialized) + "\n\n" + toMinString(mapper, serialized);
    }

    private static String toMinString(final ObjectMapper mapper, final ObjectNode serialized) {
        return serialized.toString();
    }

    private static String toPrettyString(ObjectMapper mapper, ObjectNode serialized) throws IOException {
        final ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(serialized);
    }

    public static ObjectNode serialize(final ObjectMapper mapper, final MindMap mindMap) {

        ObjectNode node = mapper.createObjectNode();
        node.put("text", mindMap.getText());
        appendChildTree(mapper, node, mindMap.getLeftChildren(), "leftChildren");
        appendChildTree(mapper, node, mindMap.getRightChildren(), "rightChildren");
        return node;
    }

    private static void appendChildTree(final ObjectMapper mapper, final ObjectNode node, final List<MindMap> side, final String key) {
        final ArrayNode left = serialize(mapper, side);
        if (left.size() > 0) {
            node.put(key, left);
        }
    }

    private static ArrayNode serialize(final ObjectMapper mapper, final List<MindMap> list) {
        final ArrayNode serializedList = mapper.createArrayNode();
        for (MindMap child : list) {
            final ObjectNode serializedChild = serialize(mapper, child);
            serializedList.add(serializedChild);
        }
        return serializedList;
    }
}
