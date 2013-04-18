package org.docear.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Messages {

	public abstract static class BaseRequest implements Serializable {
		private final String username;
		private final String source;

		public BaseRequest(String source, String username) {
			super();
			this.username = username;
			this.source = source;
		}

		public String getUsername() {
			return username;
		}

		public String getSource() {
			return source;
		}

	}

	public abstract static class MindMapRequest extends BaseRequest implements Serializable {
		private final String mapId;

		public MindMapRequest(String source, String username, String mapId) {
			super(source, username);
			this.mapId = mapId;
		}

		public String getMapId() {
			return mapId;
		}
	}

	public static class MindmapAsJsonRequest extends MindMapRequest implements Serializable {
		private final int nodeCount;

		public MindmapAsJsonRequest(String source, String username, String mapId) {
			this(source, username, mapId, -1);
		}

		public MindmapAsJsonRequest(String source, String username, String mapId, int nodeCount) {
			super(source, username, mapId);
			this.nodeCount = nodeCount;
		}

		public int getNodeCount() {
			return nodeCount;
		}
	}

	public static class MindmapAsXmlRequest extends MindMapRequest implements Serializable {

		public MindmapAsXmlRequest(String source, String username, String mapId) {
			super(source, username, mapId);
		}
	}

	public static class MindmapAsXmlResponse implements Serializable {
		private final int currentRevision;
		private final String xmlString;

		public MindmapAsXmlResponse(String xmlString, int currentRevision) {
			super();
			this.xmlString = xmlString;
			this.currentRevision = currentRevision;
		}

		public String getXmlString() {
			return xmlString;
		}

		public int getCurrentRevision() {
			return currentRevision;
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

		public RemoveNodeRequest(String source, String username, String mapId, String nodeId) {
			super(source, username, mapId);
			this.nodeId = nodeId;
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

	public static class AddNodeRequest extends MindMapRequest implements Serializable {
		private final String parentNodeId;

		public AddNodeRequest(String source, String username, String mapId, String parentNodeId) {
			super(source, username, mapId);
			this.parentNodeId = parentNodeId;
		}

		public String getParentNodeId() {
			return parentNodeId;
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

		public ChangeNodeRequest(String source, String username, String mapId, String nodeId, Map<String, Object> attributeValueMap) {
			super(source, username, mapId);
			this.nodeId = nodeId;
			this.attributeValueMap = attributeValueMap;

		}

		public String getNodeId() {
			return nodeId;
		}

		public Map<String, Object> getAttributeValueMap() {
			return attributeValueMap;
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

		public MoveNodeToRequest(String source, String username, String mapId, String newParentNodeId, String nodeToMoveId, Integer newIndex) {
			super(source, username, mapId);
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

		public GetNodeRequest(String source, String username, String mapId, String nodeId, int nodeCount) {
			super(source, username, mapId);
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

		public CloseMapRequest(String source, String username, String mapId) {
			super(source, username, mapId);
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

		public OpenMindMapRequest(String source, String username, String mapId, String mindmapFileContent, String mindmapFileName) {
			super(source, username, mapId);
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

	public static class CloseAllOpenMapsRequest extends BaseRequest implements Serializable {

		public CloseAllOpenMapsRequest(String source, String username) {
			super(source, username);
		}
	}

	public static class CloseServerRequest extends BaseRequest implements Serializable {

		public CloseServerRequest(String source, String username) {
			super(source, username);
		}
	}

	public static class RequestLockRequest extends MindMapRequest implements Serializable {
		private final String nodeId;

		public RequestLockRequest(String source, String username, String mapId, String nodeId) {
			super(source, username, mapId);
			this.nodeId = nodeId;
		}

		public String getNodeId() {
			return nodeId;
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

		public ReleaseLockRequest(String source, String username, String mapId, String nodeId) {
			super(source, username, mapId);
			this.nodeId = nodeId;
		}

		public String getNodeId() {
			return nodeId;
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

	public static class CloseUnusedMaps extends BaseRequest implements Serializable {
		private final long unusedSinceInMs;

		public CloseUnusedMaps(String source, String username) {
			this(source, username, 600000);// 10 minutes
		}

		public CloseUnusedMaps(String source, String username, long timeInMillis) {
			super(source, username);
			unusedSinceInMs = timeInMillis;
		}

		public long getUnusedSinceInMs() {
			return unusedSinceInMs;
		}
	}

	public static class ListenToUpdateOccurrenceRequest extends MindMapRequest implements Serializable {

		public ListenToUpdateOccurrenceRequest(String source, String username, String mapId) {
			super(source, username, mapId);
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

		public FetchMindmapUpdatesRequest(String source, String username, String mapId, Integer revisionId) {
			super(source, username, mapId);
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
