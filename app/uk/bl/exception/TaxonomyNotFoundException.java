package uk.bl.exception;

public class TaxonomyNotFoundException extends Exception {
	
	public TaxonomyNotFoundException(String message) {
        super(message);
    }
	
	public TaxonomyNotFoundException(Throwable exception) {
		super(exception);
	}
}
