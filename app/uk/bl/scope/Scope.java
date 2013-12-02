package uk.bl.scope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import play.Logger;

/**
 * This class implements scope rule engine.
 * A Target is in scope if any of the following statements is true:
 *
 *   Legal Deposit
 *   =============
 *      1. All URLs for this Target meet at least one of the following automated criteria:
 *          1.a The authority of the URI (i.e. the hostname) end with '.uk'.
 *          1.b The IP address associated with the URI is geo-located in the UK (using this GeoIP2 database, in a manner similar to our H3 GeoIP module).
 *      2. The Target is known to be hosted in the UK (manual boolean field).
 *      3. The Target features an page that specified a UK postal address (a manual boolean field plus a text field to hold a specific URL that contains the address).
 *      4. The Target is known to be a UK publication, according to correspondence with a curator (a manual boolean field plus a text field to hold details of the correspondence).
 *      5. The Target is known to be a UK publication, in the professional judgement of a curator (a manual boolean field plus a text field to hold the justification).
 *  By permission
 *  =============
 *      The Target is one for which we have a license that gives us permission to crawl the site (and make it available), even if the Target does not fall under any Legal Deposit criteria.
 *
 */
public class Scope {

	public static final String UK_DOMAIN      = ".uk";
	public static final String GEO_IP_SERVICE = "http://whatismyipaddress.com/ip/";
	
	/**
	 * This method checks if a given URL is in scope.
	 * @return true if in scope
	 */
	public static boolean check(String url) {
        boolean res = false;
        Logger.info("check url: " + url);
        if (url.contains(UK_DOMAIN)) {
        	res = true;
        }
        // check geo IP
        if (!res) {
        	res = checkGeoIp(url);
        }
        // TODO read configuration files with manual entries and match to the given URL
        return res;
	}
	
	/**
	 * This method check geo IP using remote service.
	 * @param url
	 * @return
	 */
	public static boolean checkGeoIp(String url) {
		boolean res = false;
		String ip = getIpFromUrl(url);
		String query = GEO_IP_SERVICE + ip;
		String response = sendGet(query);
		res = getCountry(response);
		return res;
	}
	
	/**
	 * This method converts URL to IP address.
	 * @param url
	 * @return IP address as a string
	 */
	public static String getIpFromUrl(String url) {
		String ip = "";
		InetAddress address;
		try {
			address = InetAddress.getByName(new URL(url).getHost());
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			Logger.info("ip calculation unknown host error for url=" + url + ". " + e.getMessage());
		} catch (MalformedURLException e) {
			Logger.info("ip calculation error for url=" + url + ". " + e.getMessage());
		}
        return ip;
	}
	
	/**
	 * This method sends HTTP request for given URL
	 * @param url
	 * @return
	 */
	public static String sendGet(String url) {
		String res = "";
		//		String url = "http://www.google.com/search?q=mkyong";
//		http://whatismyipaddress.com/ip/
		 
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
	//		con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
	
			Logger.info("\nSending 'GET' request to URL : " + url);
			Logger.info("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			Logger.info(response.toString());
			res = response.toString();
		} catch (IOException e) {
			Logger.info("HTTP request error: " + e.getMessage());
		}
		return res;
	}

	/**
	 * This method parses HTTP response and extracts country.
	 * @param response
	 * @return true if in scope
	 */
	public static boolean getCountry(String response) {
		boolean res = false;
		 
		if (response.contains("Country")) {
			res = true;
		}
		return res;
	}

	
}

