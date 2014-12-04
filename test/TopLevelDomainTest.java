import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;

import org.junit.Before;
import org.junit.Test;

public class TopLevelDomainTest {

	public static final String UK_DOMAIN       = ".uk";
	public static final String LONDON_DOMAIN   = ".london";
	public static final String SCOT_DOMAIN     = ".scot";
	
	List<FieldUrl> fieldUrls;
	
	@Before
	public void setUp() throws Exception {
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("https://www.gov.uk/government/publications"));
		fieldUrls.add(new FieldUrl("https://www.gov.uk/government/publications"));
	}

	@Test
	public void test() {
		assertTrue(isTopLevelDomain(fieldUrls));
		fieldUrls.add(new FieldUrl("https://www.gov.com/government/publications"));
		assertFalse(isTopLevelDomain(fieldUrls));
	}

	public boolean isTopLevelDomain(List<FieldUrl> fieldUrls) {
        for (FieldUrl fieldUrl : fieldUrls) {
            URL uri = null;
			try {
				uri = new URI(fieldUrl.url).normalize().toURL();
			} catch (MalformedURLException | URISyntaxException e) {
				e.printStackTrace();
			}
			String url = uri.toExternalForm();
            System.out.println("Normalised " + url);
            // Rule 3.1: check domain name
	        if (!url.contains(UK_DOMAIN) && !url.contains(LONDON_DOMAIN) && !url.contains(SCOT_DOMAIN)) return false;
        }
//		Logger.info("lookup entry for '" + url + "' regarding domain has value: " + res);        
        return true;
	}
	
}
