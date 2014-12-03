package uk.bl.exception;

public class UrlInvalidException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3215653453644714050L;

	public UrlInvalidException(String message) {
        super(message);
    }
	
	public UrlInvalidException(Throwable exception) {
		super(exception);
	}
}
