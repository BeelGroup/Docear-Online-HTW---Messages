package org.docear.messages;


import java.io.File;
import java.io.Serializable;

public class Messages {

	public static class MindmapAsJsonRequest implements Serializable {
		private final String id;
		private final int nodeCount;

		public MindmapAsJsonRequest(String id) {
			this(id, -1);
		}

		public MindmapAsJsonRequest(String id, int nodeCount) {
			this.id = id;
			this.nodeCount = nodeCount;
		}

		public String getId() {
			return id;
		}

		public int getNodeCount() {
			return nodeCount;
		}

	}

	public static class MindmapAsXmlRequest implements Serializable {
		private final String mapId;

		public MindmapAsXmlRequest(String mapId) {
			super();
			this.mapId = mapId;
		}

		public String getMapId() {
			return mapId;
		}

	}

	public static class MindmapAsXmlResponse implements Serializable {
		private final String xmlString;

		public MindmapAsXmlResponse(String xmlString) {
			super();
			this.xmlString = xmlString;
		}

		public String getXmlString() {
			return xmlString;
		}

	}

	public static class MindmapAsJsonReponse implements Serializable {
		private final String jsonString;

		public MindmapAsJsonReponse(String jsonString) {
			this.jsonString = jsonString;
		}

		public String getJsonString() {
			return jsonString;
		}
	}

	public static class RemoveNodeRequest implements Serializable {
		private final String mapId;
		private final String nodeId;

		public RemoveNodeRequest(String mapId, String nodeId) {
			this.mapId = mapId;
			this.nodeId = nodeId;
		}

		public String getMapId() {
			return mapId;
		}

		public String getNodeId() {
			return nodeId;
		}
	}

	public static class AddNodeRequest implements Serializable {
		private final String mapId;
		private final String parentNodeId;

		public AddNodeRequest(String mapId, String parentNodeId) {
			super();
			this.mapId = mapId;
			this.parentNodeId = parentNodeId;
		}

		public String getMapId() {
			return mapId;
		}

		public String getParentNodeId() {
			return parentNodeId;
		}

	}

	public static class AddNodeResponse implements Serializable {
		private final String nodeAsJson;

		public AddNodeResponse(String nodeAsJson) {
			super();
			this.nodeAsJson = nodeAsJson;
		}

		public String getNode() {
			return nodeAsJson;
		}
	}

	public static class ErrorMessage implements Serializable {
		private final Exception e;

		public ErrorMessage(Exception e) {
			super();
			this.e = e;
		}

		public Exception getException() {
			return e;
		}
	}

	public static class ChangeNodeRequest implements Serializable {
		final private String mapId;
		final private String nodeAsJsonString;

		public ChangeNodeRequest(String mapId, String nodeAsJsonString) {
			this.mapId = mapId;
			this.nodeAsJsonString = nodeAsJsonString;
		}

		public String getMapId() {
			return mapId;
		}

		public String getNodeAsJsonString() {
			return nodeAsJsonString;
		}
	}

	public static class GetNodeRequest implements Serializable {
		private final String mapId;
		private final String nodeId;
		private final int nodeCount;

		public GetNodeRequest(String mapId, String nodeId, int nodeCount) {
			this.mapId = mapId;
			this.nodeId = nodeId;
			this.nodeCount = nodeCount;
		}

		public String getNodeId() {
			return nodeId;
		}

		public int getNodeCount() {
			return nodeCount;
		}

		public String getMapId() {
			return mapId;
		}
	}

	public static class CloseMapRequest implements Serializable {
		final private String mapId;

		public CloseMapRequest(String mapId) {
			super();
			this.mapId = mapId;
		}

		public String getMapId() {
			return mapId;
		}
	}

	public static class GetNodeResponse implements Serializable {
		private final String nodeAsJson;

		public GetNodeResponse(String nodeAsJson) {
			super();
			this.nodeAsJson = nodeAsJson;
		}

		public String getNode() {
			return nodeAsJson;
		}
	}

	public static class OpenMindMapRequest implements Serializable {
		final private String mindmapFileContent;

		public OpenMindMapRequest(String mindmapFileContent) {
			super();
			this.mindmapFileContent = mindmapFileContent;
		}

		public String getMindmapFileContent() {
			return mindmapFileContent;
		}

	}

	public static class CloseAllOpenMapsRequest implements Serializable {
		public CloseAllOpenMapsRequest() {
			
		}
	}
	public static class CloseServerRequest implements Serializable {
		public CloseServerRequest() {
		}
	}
}
