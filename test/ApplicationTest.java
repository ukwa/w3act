import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import models.User;

import org.junit.Test;

import play.Logger;
import play.mvc.Content;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

	private static String URL1 = "bbc.co.uk";
	private static String URL2 = "marksandspencer.com";
	private static String URL3 = "google.com";

	@Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    /**
     * This is a test for whois service (UK domain) that requires Internet connection.
     */
    @Test 
    public void testWhoisUk() {
    	boolean res;
		try {
			res = Scope.checkWhois(URL1);
	    	Logger.info("test whois res: " + res);
	        assertThat(res).isEqualTo(true);
		} catch (WhoisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * This is a test for whois service (UK COM domain) that requires Internet connection.
     */
    @Test 
    public void testWhoisUkCom() {
    	boolean res;
		try {
			res = Scope.checkWhois(URL2);
	        assertThat(res).isEqualTo(true);
		} catch (WhoisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * This is a test for whois service (not UK domain) that requires Internet connection.
     */
    @Test 
    public void testWhoisNotUk() {
    	boolean res;
		try {
			res = Scope.checkWhois(URL3);
	        assertThat(res).isEqualTo(false);
		} catch (WhoisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void renderTemplate() {
        Content html = views.html.about.render("W3ACT", new User("Ross King")); 
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("W3ACT");
    }
  
   
}
