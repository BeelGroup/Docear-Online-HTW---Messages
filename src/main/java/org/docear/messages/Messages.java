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

	public static class RemoveNodeResponse implements Serializable {
		private final Boolean deleted;

		public RemoveNodeResponse(Boolean deleted) {
			super();
			this.deleted = deleted;
		}

		public Boolean getDeleted() {
			return deleted;
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

	public static class ChangeNodeResponse implements Serializable {
		private final String nodeAsJson;

		public ChangeNodeResponse(String nodeAsJson) {
			super();
			this.nodeAsJson = nodeAsJson;
		}

		public String getNode() {
			return nodeAsJson;
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
		final private String mindmapFileName;
		final private String mindmapFileContent;

		public OpenMindMapRequest(String mindmapFileContent,
				String mindmapFileName) {
			this.mindmapFileName = mindmapFileName;
			this.mindmapFileContent = mindmapFileContent;
		}

		public String getMindmapFileName() {
			return mindmapFileName;
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

	public static class RefreshLockRequest implements Serializable {
		private final String mapId;
		private final String nodeId;
		private final String username;

		public RefreshLockRequest(String mapId, String nodeId, String username) {
			super();
			this.mapId = mapId;
			this.nodeId = nodeId;
			this.username = username;
		}

		public String getMapId() {
			return mapId;
		}

		public String getNodeId() {
			return nodeId;
		}
		
		public String getUsername() {
			return username;
		}
	}

	public static class RefreshLockResponse implements Serializable {
		private final Boolean lockRefreshed;

		public RefreshLockResponse(Boolean lockRefreshed) {
			super();
			this.lockRefreshed = lockRefreshed;
		}

		public Boolean getLockRefreshed() {
			return lockRefreshed;
		}

	}

	public static class RequestLockRequest implements Serializable {
		private final String mapId;
		private final String nodeId;
		private final String username;

		public RequestLockRequest(String mapId, String nodeId, String username) {
			super();
			this.mapId = mapId;
			this.nodeId = nodeId;
			this.username = username;
		}

		public String getMapId() {
			return mapId;
		}

		public String getNodeId() {
			return nodeId;
		}

		public String getUsername() {
			return username;
		}
	}

	public static class RequestLockResponse implements Serializable {
		private final Boolean lockGained;
		private final String nodeAsJson;

		public RequestLockResponse(Boolean lockGained, String nodeAsJson) {
			super();
			this.lockGained = lockGained;
			this.nodeAsJson = nodeAsJson;
		}

		public Boolean getLockGained() {
			return lockGained;
		}

		public String getNodeAsJson() {
			return nodeAsJson;
		}
	}

	public static class ReleaseLockRequest implements Serializable {
		private final String mapId;
		private final String nodeId;
		private final String username;

		public ReleaseLockRequest(String mapId, String nodeId, String username) {
			super();
			this.mapId = mapId;
			this.nodeId = nodeId;
			this.username = username;
		}

		public String getMapId() {
			return mapId;
		}

		public String getNodeId() {
			return nodeId;
		}
		
		public String getUsername() {
			return username;
		}
	}

	public static class ReleaseLockResponse implements Serializable {
		private final Boolean lockReleased;
		private final String nodeAsJson;

		public ReleaseLockResponse(Boolean lockReleased, String nodeAsJson) {
			super();
			this.lockReleased = lockReleased;
			this.nodeAsJson = nodeAsJson;
		}

		public Boolean getLockReleased() {
			return lockReleased;
		}

		public String getNodeAsJson() {
			return nodeAsJson;
		}
	}

	public static class GetExpiredLocksRequest implements Serializable {
		private final String mapId;
		private final int deltaTimeInMs;

		public GetExpiredLocksRequest(String mapId, int deltaTimeInMs) {
			super();
			this.mapId = mapId;
			this.deltaTimeInMs = deltaTimeInMs;
		}

		public String getMapId() {
			return mapId;
		}

		public int getDeltaTimeInMs() {
			return deltaTimeInMs;
		}
	}

	public static class GetExpiredLocksResponse implements Serializable {
		private final String expiredNodesAsJSON;

		public GetExpiredLocksResponse(String expiredNodesAsJSON) {
			super();
			this.expiredNodesAsJSON = expiredNodesAsJSON;
		}

		public String getExpiredNodesAsJSON() {
			return expiredNodesAsJSON;
		}
	}

	public static class CloseUnusedMaps implements Serializable {
		private final long unusedSinceInMs;

		public CloseUnusedMaps() {
			unusedSinceInMs = 600000; // 10 minutes
		}

		public CloseUnusedMaps(long timeInMillis) {
			unusedSinceInMs = timeInMillis;
		}

		public long getUnusedSinceInMs() {
			return unusedSinceInMs;
		}
	}

	public static class ListenToUpdateOccurrenceRequest implements Serializable {
		private final String mapId;

		public ListenToUpdateOccurrenceRequest(String mapId) {
			this.mapId = mapId;
		}

		public String getMapId() {
			return mapId;
		}

	}

	public static class ListenToUpdateOccurrenceRespone implements Serializable {
		private final Boolean result;

		public ListenToUpdateOccurrenceRespone(Boolean result) {
			super();
			this.result = result;
		}

		public Boolean getResult() {
			return result;
		}
	}
}
