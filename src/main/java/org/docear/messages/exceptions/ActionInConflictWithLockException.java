package org.docear.messages.exceptions;

/**
 *
 * @author Julius
 */
public class ActionInConflictWithLockException extends Exception {
    private static final long serialVersionUID = 1L;

    public ActionInConflictWithLockException(String s) {
        super(s);
    }
    
    public ActionInConflictWithLockException(String message, Throwable cause) {
		super(message,cause);
	}
}
