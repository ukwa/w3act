package uk.bl.exception;

public class TaxonomyNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6395891501401256431L;

	public TaxonomyNotFoundException(String message) {
        super(message);
    }
	
	public TaxonomyNotFoundException(Throwable exception) {
		super(exception);
	}
}
