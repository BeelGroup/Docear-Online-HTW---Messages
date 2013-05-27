package org.docear.messages.exceptions;

import org.docear.messages.models.MapIdentifier;

public class MapNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5455448353150522704L;

	private final MapIdentifier mapIdentifier;

	public MapNotFoundException(String s, MapIdentifier mapIdentifier) {
		super(s);
		this.mapIdentifier = mapIdentifier;
	}

	public MapNotFoundException(String message, Throwable cause, MapIdentifier mapIdentifier) {
		super(message, cause);
		this.mapIdentifier = mapIdentifier;
	}

	public MapIdentifier getMapIdentifier() {
		return mapIdentifier;
	}

}
