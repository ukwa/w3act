import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;

import org.junit.Before;
import org.junit.Test;

import uk.bl.exception.WhoisException;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.WhoisResult;


public class UkRegistrationTest {

	List<FieldUrl> fieldUrls;

	@Before
	public void setUp() throws Exception {
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("https://www.gov.uk/government/publications"));
		fieldUrls.add( new FieldUrl("https://www.gov.uk/government/publications"));
	}

//	@Test
//	public void test() {
//		try {
//			assertTrue(isUkRegistration(fieldUrls));
//		} catch (WhoisException e) {
//			e.printStackTrace();
//		}
//	}

	public boolean checkWhois(String domain) {
		boolean res = false;
    	JRubyWhois whoIs = new JRubyWhois();
    	System.out.println("checkWhois: " + whoIs + " " + domain);
    	WhoisResult whoIsRes = whoIs.lookup(domain);
    	System.out.println("whoIsRes: " + whoIsRes);
//    	res = whoIsRes.isUKRegistrant();
//    	System.out.println("isUKRegistrant?: " + res);
        return res;
	}
	
	public boolean isUkRegistration(List<FieldUrl> fieldUrls) throws WhoisException {
        for (FieldUrl fieldUrl : fieldUrls) {
        	if (!checkWhois(fieldUrl.url)) return false;
        }
		return true;
	}
}
