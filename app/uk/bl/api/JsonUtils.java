package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
import models.Organisation;
import models.Target;
import models.User;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import uk.bl.Const;
import uk.bl.Const.NodeType;

/**
 * JSON object management.
 */
public class JsonUtils {

    /**
     * This method retrieves JSON data from Drupal for particular domain object type (e.g. Target, Collection...)
     * @param type
     * @return a list of retrieved objects
     */
    public static List<Object> getDrupalData(NodeType type) {
	    List<Object> res = new ArrayList();
	    try {
		    String urlStr = Const.URL_STR + type.toString().toLowerCase();
        	URL url = new URL(urlStr);
			// aggregate data from drupal and store JSON content in a file
			HttpBasicAuth.downloadFileWithAuth(urlStr, Const.AUTH_USER, Const.AUTH_PASSWORD, type.toString().toLowerCase() + Const.OUT_FILE_PATH);
			// read file and store content in String
			String content = JsonUtils.readJsonFromFile(type.toString().toLowerCase() + Const.OUT_FILE_PATH);
			res = JsonUtils.parseJson(content, type);
		} catch (MalformedURLException e) {
			System.out.println("document path error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("document path error: " + e.getMessage());
    	} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return res;
    }
    
    /**
     * This method extracts multiple items for JSON path
     * @param node
     * @param path
     * @param field
     * @return list of String items
     */
    public static List<String> getStringItems(JsonNode node, String path, String field) {
		List<String> res = new ArrayList<String>();
		JsonNode pathNode = node.path(path);
		Iterator<JsonNode> it = pathNode.iterator();
		while (it.hasNext()) {
			JsonNode subNode = it.next();			
			String item = subNode.findPath(field).textValue();
			if(item != null) {
				System.out.println("item: " + item);
				res.add(item);
			}
		}
		return res;
    }
    
    /**
     * This method extracts one item for JSON field
     * @param node
     * @param field
     * @return String item
     */
    public static String getStringItem(JsonNode node, String field) {
		String res = "";
		res = node.findPath(field).textValue();
		if(res != null) {
			System.out.println("item: " + res);
		}
		return res;
    }
    
	/**
	 * This method extracts JSON nodes and passes them to parser
	 * @param content
	 * @param type
	 * @return object list for particualar domain object type
	 */
	public static List<Object> parseJson(String content, NodeType type) {
	    List<Object> res = new ArrayList();
		JsonNode json = Json.parse(content);
		if(json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.iterator();
			System.out.println("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode node = ite.next();
				System.out.println("type: " + type);
				Object obj = null;
				if (type.equals(Const.NodeType.URL)) {					
					obj = new Target();
				} 
				if (type.equals(Const.NodeType.COLLECTION)) {
					obj = new DCollection();
				}
				if (type.equals(Const.NodeType.ORGANISATION)) {
					obj = new Organisation();
				}
				parseJsonNode(node, obj);
				res.add(obj);
			}
		} else {
			System.out.println("json is null");
		}			  
		return res;
	}
	
	/**
	 * This method parses JSON node and extracts fields
	 * @param node
	 * @param obj
	 */
	public static void parseJsonNode(JsonNode node, Object obj) {
		System.out.println("parseJsonTarget: " + obj.getClass());
		Field[] fields = obj.getClass().getFields();
		System.out.println("fields: " + fields.length);
		for (Field f : fields) {
			System.out.println("field name: " + f.getName() + ", class: " + f.getType());
			try {
				String jsonField = getStringItem(node, f.getName());
				if (f.getType().equals(String.class)) {
					if (jsonField.length() > 255) { // TODO
						jsonField = jsonField.substring(0, 254);
					}
					f.set(obj, jsonField);
				}
				if (f.getType().equals(Long.class)) {
					Long jsonFieldLong = new Long(Long.parseLong(jsonField, 10));
					f.set(obj, jsonFieldLong);
				}
				if (f.getType().equals(Boolean.class)) {
					if (jsonField.equals("Yes") 
							|| jsonField.equals("yes") 
							|| jsonField.equals("True") 
							|| jsonField.equals("Y") 
							|| jsonField.equals("y")) {
						jsonField = "true";
					}
					Boolean jsonFieldBoolean = new Boolean(Boolean.parseBoolean(jsonField));
					f.set(obj, jsonFieldBoolean);
				}
				System.out.println("parseJsonCollection: " + jsonField);
			} catch (IllegalArgumentException e) {
				System.out.println("parseJsonCollection error: " + e);
			} catch (IllegalAccessException e) {
				System.out.println("parseJsonCollection error: " + e);
			} catch (Exception e) {
				System.out.println("parseJsonCollection error: " + e);
			}
		}
		System.out.println(obj.toString());
	}

	/**
	 * This method reads JSON content from a file for given path.
	 * @param outFilePath
	 * @return JSON as a String
	 */
	public static String readJsonFromFile(String outFilePath) {
	    String res = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(outFilePath));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				res = sb.toString();
				//System.out.println("JSON output: " + res);
			} finally {
				br.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("JSON content file not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("document path error: " + e.getMessage());
    	} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return res;
	}

}

