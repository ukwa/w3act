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
import com.ning.http.client.Body;

import play.libs.Json;
import uk.bl.Const;
import uk.bl.Const.NodeType;

import models.*;

import play.Logger;

/**
 * JSON object management.
 */
public class JsonUtils {

	/**
	 * This method extracts page number from the JSON in order to evaluate first and last page numbers.
	 * @param node
	 * @param field
	 * @return page number as int
	 */
	private static int getPageNumber(JsonNode node, String field) {
		String page = getStringItem(node, field);
		Logger.info("page url: " + page);
		int idxPage = page.indexOf(Const.PAGE_IN_URL) + Const.PAGE_IN_URL.length();
		return Integer.parseInt(page.substring(idxPage));
	}
	
    /**
     * This method retrieves JSON data from Drupal for particular domain object type (e.g. Target, Collection...)
     * @param type
     * @return a list of retrieved objects
     */
    public static List<Object> getDrupalData(NodeType type) {
	    List<Object> res = new ArrayList<Object>();
	    try {
		    String urlStr = Const.URL_STR + type.toString().toLowerCase();
			// aggregate data from drupal and store JSON content in a file
			HttpBasicAuth.downloadFileWithAuth(urlStr, Const.AUTH_USER, Const.AUTH_PASSWORD, type.toString().toLowerCase() + Const.OUT_FILE_PATH);
			// read file and store content in String
			String content = JsonUtils.readJsonFromFile(type.toString().toLowerCase() + Const.OUT_FILE_PATH);
			// extract page information
			JsonNode mainNode = Json.parse(content);	
			if(mainNode != null) {
				int firstPage = getPageNumber(mainNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(mainNode, Const.LAST_PAGE);
				Logger.info("pages: " + firstPage + ", " + lastPage);
				// aggregate data from drupal for all pages 
				for (int i = firstPage; i <= lastPage; i++) {
					if (i == 1) {
						break; // TODO just now for test take only the first page
					}
					HttpBasicAuth.downloadFileWithAuth(
						urlStr + "&" + Const.PAGE_IN_URL + String.valueOf(i), Const.AUTH_USER, Const.AUTH_PASSWORD, type.toString().toLowerCase() + Const.OUT_FILE_PATH);
					String pageContent = JsonUtils.readJsonFromFile(type.toString().toLowerCase() + Const.OUT_FILE_PATH);
					List<Object> pageList = JsonUtils.parseJson(pageContent, type);
					res.addAll(pageList);
				}
			}
    	} catch (Exception e) {
			Logger.info("data aggregation error: " + e);
		}
    	Logger.info("list size: " + res.size());
		Iterator<Object> itr = res.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
//			Logger.info("res getDrupalData: " + obj.toString());
		}
		return res;
    }
    
    /**
     * This method extracts multiple items for JSON path
     * @param node
     * @param path
     * @return list of String items
     */
    public static List<String> getStringItems(JsonNode node, String path) {
		List<String> res = new ArrayList<String>();
		JsonNode pathNode = node.path(path);
		Iterator<JsonNode> it = pathNode.iterator();
		while (it.hasNext()) {
			JsonNode subNode = it.next();	
			String field = Const.URI;
			if (path.equals(Const.FIELD_URL_NODE)) {
				field = Const.URL;
			}
			String item = subNode.findPath(field).textValue();
			if(item != null) {
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
    public static String getStringItem(JsonNode node, String fieldName) {
		String res = "";
		String nodeType = node.findPath(Const.NODE_TYPE).textValue();
		if (fieldName.equals(Const.URL) && nodeType.equals(Const.NodeType.URL)) {
			List<String> urls = node.findValuesAsText(fieldName);
			res = urls.get(Const.URL_FIELD_POS_IN_JSON);
		} else {
			res = node.findPath(fieldName).textValue();
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
	    List<Object> res = new ArrayList<Object>();
		JsonNode json = Json.parse(content);
		if(json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.iterator();
			Logger.info("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode node = ite.next();
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
			Logger.info("json is null");
		}			  
		return res;
	}
	
	/**
	 * This method parses JSON node and extracts fields
	 * @param node
	 * @param obj
	 */
	public static void parseJsonNode(JsonNode node, Object obj) {
		Field[] fields = obj.getClass().getFields();
		for (Field f : fields) {
			try {
				if (f.getType().equals(java.util.List.class)) {
					List<String> jsonFieldList = new ArrayList<String>();
					jsonFieldList.add(Const.EMPTY);
					jsonFieldList = getStringItems(node, f.getName());
				    if (f.getGenericType().toString().equals("java.util.List<models.Item>")) {
						List<Item> itemList = new ArrayList<Item>();
						if (jsonFieldList.size() > 0) {
							Iterator<String> itemItr = jsonFieldList.iterator();
							while (itemItr.hasNext()) {
								Item item = new Item();
								item.value = itemItr.next();
								itemList.add(item);
							}
						}
						f.set(obj, itemList);
					} else {
						f.set(obj, jsonFieldList);
					}
					jsonFieldList.clear();
				} else {
					String jsonField = getStringItem(node, f.getName());
					if (f.getType().equals(String.class)) {
						if (jsonField == null || jsonField.length() == 0) {
							jsonField = "";
						}
						f.set(obj, jsonField);
					}
					if (f.getType().equals(Long.class)) {
						if (jsonField == null || jsonField.length() == 0) {
							jsonField = "0";
						}
						Long jsonFieldLong = new Long(Long.parseLong(jsonField, 10));
						f.set(obj, jsonFieldLong);
					}
					if (f.getType().equals(Boolean.class)) {
						if (jsonField == null || jsonField.length() == 0) {
							jsonField = "false";
						}
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
				}
			} catch (IllegalArgumentException e) {
				Logger.info("parseJsonNode IllegalArgument error: " + e); 
			} catch (IllegalAccessException e) {
				Logger.info("parseJsonNode IllegalAccess error: " + e); 
			} catch (Exception e) {
				Logger.info("parseJsonNode error: " + e); 
			}
		}
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
			} finally {
				br.close();
			}
		} catch (FileNotFoundException e) {
			Logger.info("JSON content file not found: " + e.getMessage());
		} catch (IOException e) {
			Logger.info("document path error: " + e.getMessage());
    	} catch (Exception e) {
			Logger.info("error: " + e);
		}
		return res;
	}

}

