package uk.bl;


public final class Const {
  
    private Const() {}
	
    // Authentification definitions
    public static final String URL_STR         = "http://www.webarchive.org.uk/act/node.json?type=";
	public static final String AUTH_USER       = "restws_robot";
	public static final String AUTH_PASSWORD   = "r0b0UPDATE";
	public static final String OUT_FILE_PATH   = "res.txt"; // res files are stored in root directory of the project
	
	// Names of the JSON nodes
	public static final String FIRST_PAGE = "first";
	public static final String LAST_PAGE  = "last";
	public static final String PAGE_IN_URL = "page=";
	public static final String LIST_NODE       = "list";
	public static final String FIELD_URL_NODE  = "field_url";
	
	// Types of the JSON nodes
	public enum NodeType {
        URL, 
		COLLECTION,
		ORGANISATION;
    }
		
}