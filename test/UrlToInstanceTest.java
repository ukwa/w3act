import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.bl.api.models.Wayback;
import uk.bl.exception.ActException;
import uk.bl.export.WaybackExport;

/**
 * @author kli
 * Unmarshalling XML Test
 */
public class UrlToInstanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		try {
            String urlValue = "https://www.webarchive.org.uk/wayback/archive/xmlquery.jsp?url=http://www.bl.uk/bibliographic/ukmarc.html";
			Wayback wayback = WaybackExport.INSTANCE.export(urlValue);
            assertNotNull(wayback);
		} catch (ActException e) {
			e.printStackTrace();
		}
	}

}
