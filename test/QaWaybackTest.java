import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.io.File;

import models.User;

import org.junit.Before;
import org.junit.Test;

import play.Configuration;
import play.Logger;
import play.libs.F.Callback;
import play.test.TestBrowser;
import uk.bl.exception.ActException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;




public class QaWaybackTest {

	private static String testURL = "http://www.bl.uk"; 
	private static String email = "niladree.bhattacharj@bl.uk";
	private static String password = "Nil@BL";

	@Before
	public void setUp() throws ActException {	
	}


	//	 @Test 
	//	  public void testLogin() {
	//	      running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
	//			
	//			@Override
	//			public void run() {
	//				// TODO Auto-generated method stub
	//				
	//				@SuppressWarnings("unchecked")
	//				Map<String,List<User>> accounts = (Map<String,List<User>>)Yaml.load("accounts.yml");
	//				List<User> users = accounts.get(Const.USERS);
	//				for (User user : users) {
	//					User existingUser = User.findByEmail(user.email);
	//					Logger.info("USERS:::: "+existingUser.email.toString()+" - "+existingUser.password.toString()+" - "+existingUser.affiliation.toString());
	//				}
	//				
	//				
	//			}
	//		});
	//	 }


	@Test
	public void runInBrowser() {
		running(testServer(3333, fakeApplication()), HTMLUNIT, new Callback<TestBrowser>() {
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
				assertThat(browser.pageSource()).contains("Take Me Back");
				assertThat(browser.pageSource()).doesNotContain("form action=\"/act/login\"");


			}
		});
	}

}