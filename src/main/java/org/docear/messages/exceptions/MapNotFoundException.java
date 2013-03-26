package org.docear.messages.exceptions;

public class MapNotFoundException extends Exception {
    private static final long serialVersionUID = 5455448353150522704L;

    public MapNotFoundException(String s) {
        super(s);
    }
    
    public MapNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
}