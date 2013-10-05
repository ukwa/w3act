package uk.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import models.Item;


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
	public static final String ITEM            = "item";
	
	// Body elements in JSON node
	public static final String VALUE           = "value";
	public static final String SUMMARY         = "summary";
	public static final String FORMAT          = "format";
	public static final String BODY            = "body";

	// Field position in JSON for similar names
//	public static final int URL_FIELD_POS_IN_JSON = 1; // for simple URL field
	
	// Types of the JSON nodes
	public enum NodeType {
        URL, 
		COLLECTION,
		ORGANISATION;
    }
	
    public static final Map<String, Integer> targetMap = new HashMap<String, Integer>();
    	static {
    	targetMap.put("field_url", 0);
    	targetMap.put("field_description", 1);
    	targetMap.put("field_uk_postal_address_url", 2);
    	targetMap.put("field_suggested_collections", 3);
    	targetMap.put("field_collections", 4);
    	targetMap.put("field_license", 5);
    	targetMap.put("field_collection_categories", 6);
    	targetMap.put("field_notes", 7);
    	targetMap.put("field_instances", 8);
    	targetMap.put("field_subject", 9);
    }
		
    public static final Map<String, Integer> collectionMap = new HashMap<String, Integer>();
    	static {
    	collectionMap.put("field_targets", 0);
    	collectionMap.put("field_sub_collections", 1);
    }
		
}