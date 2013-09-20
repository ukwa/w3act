package uk.bl.api;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;
import uk.bl.api.*;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.io.FileNotFoundException;
import play.libs.Json;
import org.codehaus.jackson.JsonNode;
import uk.bl.Const.*;
import uk.bl.Const;

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

	public static List<Object> parseJson(String content, NodeType type) {
	    List<Object> res = new ArrayList();
		JsonNode json = Json.parse(content);
		if(json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.getElements();
			System.out.println("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode temp = ite.next();
				System.out.println("\n\n\n");
			
				String nid = temp.findPath(Const.NID_NODE).getTextValue();
				if(nid != null) {
					System.out.println("nid: " + nid);
				}
				String title = temp.findPath(Const.TITLE_NODE).getTextValue();
				if(title != null) {
					System.out.println("URL node title: " + title);
				}
				String url = "";
				if (type.equals(Const.NodeType.URL)) {
					JsonNode urlNode = temp.path(Const.FIELD_URL_NODE);
					Iterator<JsonNode> itu = urlNode.getElements();
					while (itu.hasNext()) {
						JsonNode tempuri = itu.next();
						//System.out.println("\n\n\n\n\n\ntemp: " + temp.getTextValue());
				
						url = temp.findPath(Const.URL_NODE).getTextValue();
						if(url != null) {
							System.out.println("URL: " + url);
						}
					}
				}
				if (type.equals(Const.NodeType.URL)) {					
					//res.add(new Site(Long(id), title, url, User.find.byId("ross.king@ait.ac.at")));
					res.add(new Site(title, url, User.find.byId("ross.king@ait.ac.at")));
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
				System.out.println("JSON output: " + res);
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

