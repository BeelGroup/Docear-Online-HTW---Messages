package org.docear.messages.exceptions;

public class MapNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5455448353150522704L;

    private final String mapId;
    
    public MapNotFoundException(String s, String mapId) {
        super(s);
        this.mapId = mapId;
    }
    
    public MapNotFoundException(String message, Throwable cause, String mapId) {
		super(message,cause);
		this.mapId = mapId;
	}

	public String getMapId() {
		return mapId;
	}
    
    
}
