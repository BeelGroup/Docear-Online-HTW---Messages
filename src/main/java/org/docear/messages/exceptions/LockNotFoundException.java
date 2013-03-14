package org.docear.messages.exceptions;

/**
 *
 * @author Alexander
 */
public class LockNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public LockNotFoundException(String s) {
        super(s);
    }
    
    public LockNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
}
