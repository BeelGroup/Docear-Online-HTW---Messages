package org.docear.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.docear.messages.models.MapIdentifier;
import org.docear.messages.models.UserIdentifier;

@SuppressWarnings("serial")
public class Messages {

	public abstract static class BaseRequest implements Serializable {
		private final UserIdentifier userIdentifier;

		public BaseRequest(UserIdentifier userIdentifier) {
			this.userIdentifier = userIdentifier;
		}

		public UserIdentifier getUserIdentifier() {
			return userIdentifier;
		}

		public String getUsername() {
			return userIdentifier.getUsername();
		}

		public String getSource() {
			return userIdentifier.getSource();
		}

	}

	public abstract static class MindMapRequest extends BaseRequest implements Serializable {
		private final MapIdentifier mapIdentifier;

		public MindMapRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier) {
			super(userIdentifier);
			this.mapIdentifier = mapIdentifier;
		}

		public MapIdentifier getMapIdentifier() {
			return mapIdentifier;
		}

		public String getMapId() {
			return mapIdentifier.getMapId();
		}

		public String getProjectId() {
			return mapIdentifier.getProjectId();
		}

	}

	public static class MindmapAsJsonRequest extends MindMapRequest implements Serializable {
		private final int nodeCount;

		public MindmapAsJsonRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier) {
			this(userIdentifier, mapIdentifier, -1);
		}

		public MindmapAsJsonRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, int nodeCount) {
			super(userIdentifier, mapIdentifier);
			this.nodeCount = nodeCount;
		}

		public int getNodeCount() {
			return nodeCount;
		}
	}

	public static class MindmapAsXmlRequest extends MindMapRequest implements Serializable {

		public MindmapAsXmlRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier) {
			super(userIdentifier, mapIdentifier);
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

		public RemoveNodeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId) {
			super(userIdentifier, mapIdentifier);
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
		private final String side;

		public AddNodeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String parentNodeId) {
			this(userIdentifier, mapIdentifier, parentNodeId, null);
		}
		
		public AddNodeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String parentNodeId, String side) {
			super(userIdentifier, mapIdentifier);
			this.parentNodeId = parentNodeId;
			this.side = side;
		}

		public String getParentNodeId() {
			return parentNodeId;
		}
		
		public String getSide(){
			return side;
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

		public ChangeNodeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId, Map<String, Object> attributeValueMap) {
			super(userIdentifier, mapIdentifier);
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

		public MoveNodeToRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String newParentNodeId, String nodeToMoveId, Integer newIndex) {
			super(userIdentifier, mapIdentifier);
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

		public GetNodeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId, int nodeCount) {
			super(userIdentifier, mapIdentifier);
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

		public CloseMapRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier) {
			super(userIdentifier, mapIdentifier);
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

		public OpenMindMapRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String mindmapFileContent, String mindmapFileName) {
			super(userIdentifier, mapIdentifier);
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

		public CloseAllOpenMapsRequest(UserIdentifier userIdentifier) {
			super(userIdentifier);
		}
	}

	public static class CloseServerRequest extends BaseRequest implements Serializable {

		public CloseServerRequest(UserIdentifier userIdentifier) {
			super(userIdentifier);
		}
	}

	public static class RequestLockRequest extends MindMapRequest implements Serializable {
		private final String nodeId;

		public RequestLockRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId) {
			super(userIdentifier, mapIdentifier);
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

		public ReleaseLockRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId) {
			super(userIdentifier, mapIdentifier);
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

	public static class ChangeEdgeRequest extends MindMapRequest implements Serializable {
		private final String nodeId;
		private final Map<String, Object> attributeValueMap;

		public ChangeEdgeRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, String nodeId, Map<String, Object> attributeValueMap) {
			super(userIdentifier, mapIdentifier);
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

	public static class ChangeEdgeResponse implements Serializable {
		private final boolean success;

		public ChangeEdgeResponse(boolean success) {
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}

	}

	public static class CloseUnusedMaps extends BaseRequest implements Serializable {
		private final long unusedSinceInMs;

		public CloseUnusedMaps(UserIdentifier userIdentifier) {
			this(userIdentifier, 600000);// 10 minutes
		}

		public CloseUnusedMaps(UserIdentifier userIdentifier, long timeInMillis) {
			super(userIdentifier);
			unusedSinceInMs = timeInMillis;
		}

		public long getUnusedSinceInMs() {
			return unusedSinceInMs;
		}
	}

	public static class ListenToUpdateOccurrenceRequest extends MindMapRequest implements Serializable {

		public ListenToUpdateOccurrenceRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier) {
			super(userIdentifier, mapIdentifier);
		}

	}

	public static class ListenToUpdateOccurrenceResponse implements Serializable {
		private final Boolean result;

		public ListenToUpdateOccurrenceResponse(Boolean result) {
			this.result = result;
		}

		public Boolean getResult() {
			return result;
		}
	}

	public static class FetchMindmapUpdatesRequest extends MindMapRequest implements Serializable {
		private final Integer revisionId;

		public FetchMindmapUpdatesRequest(UserIdentifier userIdentifier, MapIdentifier mapIdentifier, Integer revisionId) {
			super(userIdentifier, mapIdentifier);
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
