package uk.bl;


public final class Const {
  
    private Const() {}
	
    // Authentification definitions
    public static final String URI             = "uri";
    public static final String URL             = "url";
    public static final String URL_STR         = "http://www.webarchive.org.uk/act/node.json?type=";
	public static final String AUTH_USER       = "restws_robot";
	public static final String AUTH_PASSWORD   = "r0b0UPDATE";
	public static final String OUT_FILE_PATH   = "res.txt"; // res files are stored in root directory of the project
	
	// Names of the JSON nodes
	public static final String FIRST_PAGE      = "first";
	public static final String LAST_PAGE       = "last";
	public static final String PAGE_IN_URL     = "page=";
	public static final String LIST_NODE       = "list";
	public static final String FIELD_URL_NODE  = "field_url";
	public static final String NODE_TYPE       = "type";
	public static final String EMPTY           = "empty";
	
	// Field position in JSON for similar names
	public static final int URL_FIELD_POS_IN_JSON = 1;
	
	// Types of the JSON nodes
	public enum NodeType {
        URL, 
		COLLECTION,
		ORGANISATION;
    }
		
}