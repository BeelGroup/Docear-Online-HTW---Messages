package messages.exceptions;


public class NodeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NodeNotFoundException(String message) {
		super(message);
	}
}
