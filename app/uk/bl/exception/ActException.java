package uk.bl.exception;

public class ActException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 866007261538659310L;

	public ActException(String message) {
        super(message);
    }
	
	public ActException(Throwable exception) {
		super(exception);
	}
}
