package org.docear.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Messages {

	public abstract static class MindMapRequest implements Serializable {
		private final String mapId;

		public MindMapRequest(String mapId) {
			super();
			this.mapId = mapId;
		}

		public String getMapId() {
			return mapId;
		}
	}

	public static class MindmapAsJsonRequest extends MindMapRequest implements Serializable {
		private final int nodeCount;

		public MindmapAsJsonRequest(String mapId) {
			this(mapId, -1);
		}

		public MindmapAsJsonRequest(String mapId, int nodeCount) {
			super(mapId);
			this.nodeCount = nodeCount;
		}

		public int getNodeCount() {
			return nodeCount;
		}
	}

	public static class MindmapAsXmlRequest extends MindMapRequest implements Serializable {

		public MindmapAsXmlRequest(String mapId) {
			super(mapId);
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

	public static class RemoveNodeRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final String username;

		public RemoveNodeRequest(String mapId, String nodeId, String username) {
			super(mapId);
			this.nodeId = nodeId;
			this.username = username;
		}

		public String getNodeId() {
			return nodeId;
		}

		public String getUsername() {
			return username;
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

	public static class AddNodeRequest extends MindMapRequest implements Serializable {
		private final String parentNodeId;
		private final String username;

		public AddNodeRequest(String mapId, String parentNodeId, String username) {
			super(mapId);
			this.parentNodeId = parentNodeId;
			this.username = username;
		}

		public String getParentNodeId() {
			return parentNodeId;
		}

		public String getUsername() {
			return username;
		}

	}

	public static class AddNodeResponse implements Serializable {
		private final String mapUpdate;

		public AddNodeResponse(String mapUpdate) {
			super();
			this.mapUpdate = mapUpdate;
		}

		public String getMapUpdate() {
			return mapUpdate;
		}
	}

	public static class ChangeNodeRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final Map<String, Object> attributeValueMap;
		private final String username;

		public ChangeNodeRequest(String mapId, String nodeId, Map<String, Object> attributeValueMap, String username) {
			super(mapId);
			this.nodeId = nodeId;
			this.attributeValueMap = attributeValueMap;
			this.username = username;
		}

		public String getNodeId() {
			return nodeId;
		}

		public Map<String, Object> getAttributeValueMap() {
			return attributeValueMap;
		}

		public String getUsername() {
			return username;
		}

	}

	public static class ChangeNodeResponse implements Serializable {
		private final List<String> mapUpdates;

		public ChangeNodeResponse(List<String> mapUpdates) {
			super();
			this.mapUpdates = mapUpdates;
		}

		public List<String> getMapUpdates() {
			return mapUpdates;
		}
	}

	public static class MoveNodeToRequest extends MindMapRequest implements Serializable {
		private final String newParentNodeId;
		private final String nodeToMoveId;
		private final Integer newIndex;

		public MoveNodeToRequest(String mapId, String newParentNodeId, String nodeToMoveId, Integer newIndex) {
			super(mapId);
			this.newParentNodeId = newParentNodeId;
			this.nodeToMoveId = nodeToMoveId;
			this.newIndex = newIndex;
		}

		public String getNewParentNodeId() {
			return newParentNodeId;
		}

		public String getNodeToMoveId() {
			return nodeToMoveId;
		}

		public Integer getNewIndex() {
			return newIndex;
		}
	}

	public static class MoveNodeToResponse implements Serializable {
		private final Boolean success;

		public MoveNodeToResponse(Boolean success) {
			super();
			this.success = success;
		}

		public Boolean getSuccess() {
			return success;
		}
	}

	public static class GetNodeRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final int nodeCount;

		public GetNodeRequest(String mapId, String nodeId, int nodeCount) {
			super(mapId);
			this.nodeId = nodeId;
			this.nodeCount = nodeCount;
		}

		public String getNodeId() {
			return nodeId;
		}

		public int getNodeCount() {
			return nodeCount;
		}
	}

	public static class CloseMapRequest extends MindMapRequest implements Serializable {

		public CloseMapRequest(String mapId) {
			super(mapId);
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

	public static class OpenMindMapRequest extends MindMapRequest implements Serializable {
		final private String mindmapFileName;
		final private String mindmapFileContent;

		public OpenMindMapRequest(String mapId, String mindmapFileContent, String mindmapFileName) {
			super(mapId);
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

	public static class OpenMindMapResponse implements Serializable {
		final private Boolean success;

		public OpenMindMapResponse(Boolean success) {
			super();
			this.success = success;
		}

		public Boolean getSuccess() {
			return success;
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

	public static class RequestLockRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final String username;

		public RequestLockRequest(String mapId, String nodeId, String username) {
			super(mapId);
			this.nodeId = nodeId;
			this.username = username;
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
		private final String mapUpdate;

		public RequestLockResponse(Boolean lockGained, String mapUpdate) {
			super();
			this.lockGained = lockGained;
			this.mapUpdate = mapUpdate;
		}

		public Boolean getLockGained() {
			return lockGained;
		}

		public String getMapUpdate() {
			return mapUpdate;
		}
	}

	public static class ReleaseLockRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final String username;

		public ReleaseLockRequest(String mapId, String nodeId, String username) {
			super(mapId);
			this.nodeId = nodeId;
			this.username = username;
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
		private final String mapUpdate;

		public ReleaseLockResponse(Boolean lockReleased, String mapUpdate) {
			super();
			this.lockReleased = lockReleased;
			this.mapUpdate = mapUpdate;
		}

		public Boolean getLockReleased() {
			return lockReleased;
		}

		public String getMapUpdate() {
			return mapUpdate;
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

	public static class ListenToUpdateOccurrenceRequest extends MindMapRequest implements Serializable {

		public ListenToUpdateOccurrenceRequest(String mapId) {
			super(mapId);
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

	public static class FetchMindmapUpdatesRequest extends MindMapRequest implements Serializable {
		private final Integer revisionId;

		public FetchMindmapUpdatesRequest(String mapId, Integer revisionId) {
			super(mapId);
			this.revisionId = revisionId;
		}

		public Integer getRevisionId() {
			return revisionId;
		}
	}

	public static class FetchMindmapUpdatesResponse implements Serializable {
		private final Integer currentRevision;
		private final List<String> orderedUpdates;

		public FetchMindmapUpdatesResponse(Integer currentRevision, List<String> orderedUpdates) {
			this.currentRevision = currentRevision;
			this.orderedUpdates = orderedUpdates;
		}

		public List<String> getOrderedUpdates() {
			return orderedUpdates;
		}

		public Integer getCurrentRevision() {
			return currentRevision;
		}
	}
}
