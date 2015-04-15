import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

public class TopLevelDomainTest {

	public static final String UK_DOMAIN       = ".uk";
	public static final String LONDON_DOMAIN   = ".london";
	public static final String SCOT_DOMAIN     = ".scot";
	public static final String WALES_DOMAIN     = ".wales";
	public static final String CYMRU_DOMAIN     = ".cymru";
	
	Target target = null;
	List<FieldUrl> fieldUrls;
	
	@Before
	public void setUp() throws Exception {
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("http://www.gov.uk"));
		fieldUrls.add(new FieldUrl("http://www.google.scot"));
		target.fieldUrls = fieldUrls;
	}

	@Test
	public void test() throws ActException  {
		Boolean pass = Scope.INSTANCE.isTopLevelDomain(target);
		System.out.println("fieldUrls with valid top level domains: " + target.fieldUrls + " - " + pass);
		assertTrue(pass);
		FieldUrl newFieldUrl = new FieldUrl("http://www.gov.com");
		target.fieldUrls.add(newFieldUrl);
		Boolean fail = Scope.INSTANCE.isTopLevelDomain(target);
		System.out.println("fieldUrls with invalid top level domains (.com): " + target.fieldUrls + " - " + fail);
		assertFalse(fail);
		
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		target.fieldUrls = fieldUrls;
		String url = "http://www.ukbiologycompetitions.org/dfsfsf.uk";
		FieldUrl ukFieldUrl = new FieldUrl(url);
        URL uri;
		try {
			uri = new URI(url).normalize().toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			throw new ActException(e);
		}
		url = uri.toExternalForm();
		System.out.println("extForm: " + url);
    	String domain = Scope.INSTANCE.getDomainFromUrl(url);
		System.out.println("domain: " + domain);

		target.fieldUrls.add(ukFieldUrl);
		Boolean failedAgain = Scope.INSTANCE.isTopLevelDomain(target);
		System.out.println("fieldUrls with invalid top level domains (.org): " + target.fieldUrls + " - " + failedAgain);
		assertFalse(failedAgain);
	}
}
