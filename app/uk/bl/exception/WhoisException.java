package uk.bl.exception;

public class WhoisException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8822959478880012893L;

	public WhoisException(String message) {
        super(message);
    }
	
	public WhoisException(Throwable exception) {
		super(exception);
	}
}
