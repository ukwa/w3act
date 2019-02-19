import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.libs.F.Callback;
import play.test.TestBrowser;
import uk.bl.exception.ActException;




public class QaWaybackTest {

	private static String testURL = "http://www.bl.uk"; 
	private static String email = "niladree.bhattacharj@bl.uk";
	private static String password = "Nil@BL";

	@Before
	public void setUp() throws ActException {	
	}


	@Test
	public void runInBrowser() {
		running(testServer(3333, fakeApplication()), HTMLUNIT, new Callback<TestBrowser>() {
			@Override
			public void invoke(TestBrowser browser) {
				String testPath = "/wayback/*/"+testURL;
				String fullTestUrl = "http://localhost:3333/act"+testPath;
				// Check we cannot access anything yet:
				Logger.info("Going to Wayback page...");
				browser.goTo(fullTestUrl);
				assertThat(browser.url()).isEqualTo("http://localhost:3333/act/login");
				assertThat(browser.pageSource()).contains("form action=\"/act/login\"");
				// Go to login page:
				Logger.info("Going to about page...");
				browser.goTo("http://localhost:3333/act/about");
				assertThat(browser.url()).isEqualTo("http://localhost:3333/act/login");            
				assertThat(browser.pageSource()).contains("Email");                          
				assertThat(browser.pageSource()).contains("Password");
				// Login
				Logger.info("Logging in...");
				browser.fill("#email").with(email);
				browser.fill("#password").with(password);
				browser.submit("#submit");
				Logger.info("Checking we are at the about page...");
				assertThat(browser.url()).isEqualTo("http://localhost:3333/act/about");
				assertThat(browser.title()).isEqualTo("About :: W3ACT");
				browser.getCookies();
				// Check we can now access Wayback:
				Logger.info("Going to Wayback page now we are logged in...");
				browser.goTo(fullTestUrl);
				assertThat(browser.pageSource()).contains("nginx");
				assertThat(browser.pageSource()).doesNotContain("form action=\"/act/login\"");


			}
		});
	}

}
