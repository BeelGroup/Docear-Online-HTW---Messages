package org.docear.messages.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MapIdentifier implements Serializable {

	private final String projectId;
	private final String mapId;

	public MapIdentifier(String projectId, String mapId) {
		super();
		this.projectId = projectId;
		this.mapId = mapId;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getMapId() {
		return mapId;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MapIdentifier) {
			final MapIdentifier mapIdentifier = (MapIdentifier) obj;
			return mapIdentifier.getMapId().equals(mapId) && mapIdentifier.getProjectId().equals(projectId);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (projectId+mapId).hashCode();
	}
}
