import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.apache.commons.lang3.StringUtils;
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
		fieldUrls.add(new FieldUrl("http://www.islamic-relief.org.uk/"));
//		fieldUrls.add( new FieldUrl("https://www.gov.uk/government/publications"));
		target.fieldUrls = fieldUrls;
	}

	@Test
	public void test() {
		try {
			Boolean valid = Scope.INSTANCE.isUkRegistration(target);
			assertTrue(valid);
			assertTrue(checkWhois("http://www.islamic-relief.org.uk/"));
			assertTrue(checkWhois("http://www.fishbournepreschool.org.uk/"));
//			assertTrue(checkWhois("http://www.westsussex.gov.uk"));
			
		} catch (WhoisException e) {
			e.printStackTrace();
		}
	}

	public boolean checkWhois(String url) {
		boolean res = false;
    	JRubyWhois whoIs = new JRubyWhois();
    	
		try {
			URI uri = new URI(url);
			String domain = uri.getHost();
			if (StringUtils.isNotEmpty(domain)) {
				domain = domain.startsWith("www.") ? domain.substring(4) : domain;
			}
	    	System.out.println("checkWhois: " + whoIs + " " + domain);
	    	WhoisResult whoIsRes = whoIs.lookup(domain);
	    	res = whoIsRes.isUKRegistrant();
	    	System.out.println("isUKRegistrant: " + res);
	    	res = whoIsRes.isUKRegistrant();
		} catch (org.jruby.embed.EvalFailedException | URISyntaxException  e) {
//			e.printStackTrace();
		}
    	System.out.println("isUKRegistrant?: " + res);
        return res;
	}
	
	public boolean isUkRegistration(Target target) throws WhoisException {
        for (FieldUrl fieldUrl : target.fieldUrls) {
        	if (!checkWhois(fieldUrl.url)) return false;
        }
		return true;
	}
}
