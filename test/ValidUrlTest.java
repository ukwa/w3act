import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import play.Logger;

public class ValidUrlTest {

	@Test
	public void test() {
		String url = "https://democracy.kent.gov.uk/ieListMeetings.aspx?CId=706&Year=2012";
    	Logger.debug("url: " + url.trim());
//			uri = new URI(url.trim()).normalize().toURL();
//			String extFormUrl = uri.toExternalForm();
//        	System.out.println("" + extFormUrl.trim());
//        	Logger.debug("extFormUrl: " + extFormUrl);
		boolean isValidUrl = validUrl(url);
		if (isValidUrl) {
			System.out.println("valid? " + isValidUrl + " - " + url);
		} else {
			System.out.println("invalid " + url);
		}
////    	    UrlValidator urlValidator = new UrlValidator();
//    	    
//    	    //valid URL
//    	    if (urlValidator.isValid("http://www.mkyong.com"))
	}
	
    public boolean validUrl(String url) {
//    	String reg = "@(https?|ftp)://(-\\.)?([^\\s/?\\.#-]+\\.?)+(/[^\\s]*)?$@iS";
    	String reg = "^http\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(/\\S*)?$";
    	return url.matches(reg);
    }

}
