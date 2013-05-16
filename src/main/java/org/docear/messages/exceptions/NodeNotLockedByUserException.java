package org.docear.messages.exceptions;

/**
 *
 * @author Julius
 */
public class NodeNotLockedByUserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NodeNotLockedByUserException(String s) {
        super(s);
    }
    
    public NodeNotLockedByUserException(String message, Throwable cause) {
		super(message,cause);
	}
}
