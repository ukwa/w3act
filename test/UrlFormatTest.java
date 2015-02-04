import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.FieldUrl;

import org.junit.Before;
import org.junit.Test;


public class UrlFormatTest {

	String dbUrl = null;
	String similarUrl1 = null;
	String similarUrl2 = null;
	String similarUrl3 = null;
	String similarUrl4 = null;
	String similarUrl5 = null;
	String similarUrl6 = null;
	String similarUrl7 = null;
	String similarUrl8 = null;
	String similarUrl9 = null;
	
	List<String> inputUrls = new ArrayList<String>();

	@Before
	public void setUp() throws Exception {
		dbUrl = "http://www.google.co.uk";
		
		similarUrl1 = "http://cafonline.org";
		similarUrl2 = "http://google.co.uk/";
		similarUrl3 = "https://google.co.uk";
		similarUrl4 = "https://google.co.uk/";
		similarUrl5 = "https://google.co.uk/";
		similarUrl6 = "http://www.google.co.uk";
		similarUrl7 = "http://www.google.co.uk/";
		similarUrl8 = "https://www.google.co.uk";
		similarUrl9 = "https://www.google.co.uk/";
		
		inputUrls.add(similarUrl1);
		inputUrls.add(similarUrl2);
		inputUrls.add(similarUrl3);
		inputUrls.add(similarUrl4);
		inputUrls.add(similarUrl5);
		inputUrls.add(similarUrl6);
		inputUrls.add(similarUrl7);
		inputUrls.add(similarUrl8);
		inputUrls.add(similarUrl9);
		
		System.out.println("DB URL: " + dbUrl + "\n");
	}

	@Test
	public void test() {
		for (String inputUrl : this.inputUrls) {
			getVaryingUrls(inputUrl);
		}
	}

	
    public FieldUrl getFieldUrl(String url) {
    	url = url.trim();
    	FieldUrl fieldUrl = this.findByUrl(url);
    	if (fieldUrl == null) {
    		// different variations lookups
    		// not ending in '/' url just in-case
    	}
    	return fieldUrl;
    }
    
    // mirror finder
    public FieldUrl findByUrl(String url) {
		FieldUrl dbFieldUrl = new FieldUrl(dbUrl);
    	if (dbFieldUrl.url.equals(url)) {
    		return dbFieldUrl;
    	}
    	return null;
    }
    
    private String removeSlash(String url) {
		return url.substring(0, url.length()-1);
    }
    
    private String convertHttpToHttps(String url) {
    	return url.replace("http://", "https://");
    }

    private String convertHttpsToHttp(String url) {
    	return url.replace("https://", "http://");
    }

    private String addSlash(String url) {
    	return url + "/";
    }
    
    private String removeWww(String url) {
    	return url.replace("www.", "");
    }
    
    private String addWww(String url) {
    	if (!url.contains("www.")) {
			StringBuilder builder = new StringBuilder(url);
			int offset = 7;
			if (url.startsWith("https://")) {
				offset = 8;
			}
			builder.insert(offset, "www.");
			return builder.toString();
    	}
    	return url;
    }
    
    private Set<String> getVaryingUrls(String url) {
    	Set<String> varyingUrls = new HashSet<String>();
    	// remove all slashes
    	if (url.endsWith("/")) {
    		url = removeSlash(url);
    	}
    	
    	System.out.println("input: " + url + "\n");
    	
		String http = convertHttpToHttps(url); // https://
		varyingUrls.add(http);
		
		String httpSlash = addSlash(http); // https://www.werwer.com/
		varyingUrls.add(httpSlash);
		
		String httpNoWww = removeWww(http); // https://werwer.com
		varyingUrls.add(httpNoWww);
		
		String httpNoWwwSlash = addSlash(httpNoWww); // https://werwer.com/
		varyingUrls.add(httpNoWwwSlash);
		
		String httpWww = addWww(http);
		varyingUrls.add(httpWww);
		
		String httpWwwSlash = addSlash(httpWww); // https://werwer.com/
		varyingUrls.add(httpWwwSlash);
		
		
		String https = convertHttpsToHttp(url);
		varyingUrls.add(https);
		
		String httpsSlash = addSlash(https);
		varyingUrls.add(httpsSlash);

		String httpsNoWww = removeWww(https);
		varyingUrls.add(httpsNoWww);

		String httpsNoWwwSlash = addSlash(httpsNoWww); // https://werwer.com/
		varyingUrls.add(httpsNoWwwSlash);
		
		String httpsWww = addWww(https);
		varyingUrls.add(httpsWww);

		String httpsWwwSlash = addSlash(httpsWww); // https://werwer.com/
		varyingUrls.add(httpsWwwSlash);

    	System.out.println("varyingUrls: " + varyingUrls + "\n");
    	return varyingUrls;
    }
}
