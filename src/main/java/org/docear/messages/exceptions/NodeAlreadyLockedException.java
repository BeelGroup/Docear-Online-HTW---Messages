package org.docear.messages.exceptions;

/**
 *
 * @author Alexander
 */
public class NodeAlreadyLockedException extends Exception {
    private static final long serialVersionUID = 1L;

    public NodeAlreadyLockedException(String message) {
        super(message);
    }
    
    public NodeAlreadyLockedException(String message, Throwable cause) {
		super(message,cause);
	}
}
