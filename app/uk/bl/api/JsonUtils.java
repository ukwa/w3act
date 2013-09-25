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
import models.Target;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import uk.bl.Const;
import uk.bl.Const.NodeType;

/**
 * JSON object management.
 */
public class JsonUtils {

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
		Iterator<JsonNode> it = pathNode.getElements();
		while (it.hasNext()) {
			JsonNode subNode = it.next();			
			String item = subNode.findPath(field).getTextValue();
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
		res = node.findPath(field).getTextValue();
		if(res != null) {
			System.out.println("item: " + res);
		}
		return res;
    }
    
	public static List<Object> parseJson(String content, NodeType type) {
	    List<Object> res = new ArrayList();
		JsonNode json = Json.parse(content);
		if(json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.getElements();
			System.out.println("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode node = ite.next();
				//System.out.println("\n");
			
				String nid = node.findPath(Const.NID_NODE).getTextValue();
				if(nid != null) {
					System.out.println("nid: " + nid);
				}
				String title = node.findPath(Const.TITLE_NODE).getTextValue();
				if(title != null) {
					System.out.println("URL node title: " + title);
				}
				String url = "";
				System.out.println("type: " + type);
				if (type.equals(Const.NodeType.URL)) {					
					Target target = new Target(title, url);
					parseJsonTarget(node, target);
					System.out.println(target.toString());
					res.add(target);
				} else {
					//res.add(new DCollection(Long(id), title, url, User.find.byId("ross.king@ait.ac.at")));
					res.add(new DCollection(title, url, User.find.byId("ross.king@ait.ac.at")));
				}
			}
		} else {
			System.out.println("json is null");
		}			  
		return res;
	}
	
	public static void parseJsonTarget(JsonNode node, Target obj) {
		System.out.println("parseJsonTarget: " + obj.getClass());
		Field[] fields = obj.getClass().getFields();
		System.out.println("fields: " + fields.length);
		for (Field f : fields) {
			System.out.println("field name: " + f.getName() + ", class: " + f.getType());
			if (f.getType().equals(String.class)) {
				String jsonField = getStringItem(node, f.getName());
				System.out.println("found value: " + jsonField);
				try {
					f.set(obj, jsonField);
					System.out.println("parseJsonTarget: " + jsonField);
				} catch (IllegalArgumentException e) {
					System.out.println("parseJsonTarget error: " + e);
				} catch (IllegalAccessException e) {
					System.out.println("parseJsonTarget error: " + e);
				}
			}
		}
	}

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

