package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.User;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WSResponse;
import play.libs.ws.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.exception.ActException;

public class WaybackController extends Controller {
	
	private static DocumentBuilder db = null;
	private static Document doc = null;
	private static HttpURLConnection http;
	private static URL wayback_url;
	private static int statusCode = 0;

	@Security.Authenticated(SecuredController.class)
	public static Result wayback(String url) throws ActException, ClientProtocolException, IOException {
		
		User user = User.findByEmail(session().get("email"));
		if( ! user.isLDLMember() ) {
			return unauthorized(
					"unauthorized - you must be a member of a Legal Deposit library organisation to view the crawled resources"
					);
		}
				
		String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
		
		// Build up the wayback query:
		String waybackBuilder = wayBackUrl + "/" + url;
		String q = ctx()._requestHeader().rawQueryString();
		if( q != null && q.length() > 0 ) {
			Logger.info("Passing through raw Query String: "+q);
			waybackBuilder += "?"+q;
		}
		final String wayback = waybackBuilder;
		Logger.info("Using URL: "+wayback);

		// Build up URL and copy over query parameters:		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(wayback);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		System.out.println(response.getStatusLine());
		   // do something useful with the response body
	    // and ensure it is fully consumed
		if ( response.getFirstHeader(LOCATION) != null ) {
			Logger.debug("Copying over Location header: "+response.getFirstHeader(LOCATION));
			response().setHeader(LOCATION, response.getFirstHeader(LOCATION).getValue());
			return status(response.getStatusLine().getStatusCode());
		}
		String contentType = response.getFirstHeader(CONTENT_TYPE).getValue();
		Logger.debug("content type: " + contentType);
		return status(response.getStatusLine().getStatusCode(), entity.getContent()).as(contentType);
	}
	
	public static Result waybackRoot() throws ActException, ClientProtocolException, IOException {
		return wayback("");
	}

	
	/**Method to fetch number of crawled urls**/
	public static String getTotalCrawledUrls(String url) {
		Logger.debug("getTotalCrawledUrls url:"+url);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
			db = dbf.newDocumentBuilder();

			/***Check the http status code***/

			wayback_url = new URL(wayBackUrl+"/xmlquery.jsp?type=prefixquery&url="+url);

			http = (HttpURLConnection)wayback_url.openConnection();
			http.setRequestMethod("GET");
			http.connect();

			statusCode = http.getResponseCode();
			Logger.debug("getTotalCrawledUrls statusCode:"+ statusCode);

			/********************************/

			if(statusCode==200){
				Logger.debug("getTotalCrawledUrls parsing XML...");
				doc = db.parse(http.getInputStream());

				Logger.debug("getTotalCrawledUrls getting values from XML...");
				NodeList nl = doc.getElementsByTagName("request");
				Node n = nl.item(0).getChildNodes().item(9);  
				Logger.debug("getTotalCrawledUrls = "+ n.getTextContent());

				return n.getTextContent();

			}
		}catch( Exception e ) {
			Logger.warn("Exception while lookup up getTotalCrawledUrls",e);
		}
		
		return "0";
	}

	/**Method to fetch number of times the specific url has been crawled**/
	public static String getTotalCrawledInstances(String url) {
		Logger.debug("getTotalCrawledInstances url:"+url);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
			db = dbf.newDocumentBuilder();

			/***Check the http status code***/

			wayback_url = new URL(wayBackUrl + "/xmlquery.jsp?type=urlquery&url=" + url);

			http = (HttpURLConnection)wayback_url.openConnection();
			http.setRequestMethod("GET");
			http.connect();

			statusCode = http.getResponseCode();
			Logger.debug("getTotalCrawledInstances statusCode:"+ statusCode);

			/********************************/

			if(statusCode==200){
				doc = db.parse(http.getInputStream());

				NodeList nl = doc.getElementsByTagName("request");
				Node n = nl.item(0).getChildNodes().item(9);  

				return n.getTextContent();

			}
		} catch (Exception e) {
			Logger.warn("Exception while lookup up getTotalCrawledInstances",e);
		}
		
		return "0";
	}
}

