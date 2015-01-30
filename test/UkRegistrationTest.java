import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.WhoisResult;


public class UkRegistrationTest {

	Target target = null;
	List<FieldUrl> fieldUrls;

	@Before
	public void setUp() throws Exception {
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("https://www.gov.uk/government/publications"));
		fieldUrls.add( new FieldUrl("https://www.gov.uk/government/publications"));
		target.fieldUrls = fieldUrls;
	}

//	@Test
//	public void test() {
//		try {
//			Boolean valid = Scope.INSTANCE.isUkRegistration(target);
//			assertTrue(valid);
////			Boolean validAgain = this.isUkRegistration(target);
////			assertTrue(validAgain);
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
	
	public boolean isUkRegistration(Target target) throws WhoisException {
        for (FieldUrl fieldUrl : target.fieldUrls) {
        	if (!checkWhois(fieldUrl.url)) return false;
        }
		return true;
	}
}
