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
import play.libs.F.Callback;
import play.test.TestBrowser;
import uk.bl.exception.ActException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;




public class QaWaybackTest {
		
	private Configuration additionalConfigurations;
	User user = new User();
	User ldlUser = new User();
	private static String testURL = "http://www.bl.uk"; 
	private static String email = "niladree.bhattacharj@bl.uk";
	private static String password = "Nil@BL";
	
	@Before
	public void setUp() throws ActException {	
		
		// Set up a test configuration;
	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/dev.conf"));
	    additionalConfigurations = new Configuration(additionalConfig);
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
        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
               // Check we cannot access anything yet:
               browser.goTo("http://localhost:3333/act"+"/wayback/*/"+testURL);
               assertThat(browser.pageSource()).contains("form action=\"/act/login\"");
               // Go to login page:
               browser.goTo("http://localhost:3333/test");
               browser.$("a").click();
               assertThat(browser.url()).isEqualTo("http://localhost:3333/act/login");            
               assertThat(browser.pageSource()).contains("Email");                          
               assertThat(browser.pageSource()).contains("Password");
               // Login
               browser.fill("#email").with(email);
          	   browser.fill("#password").with(password);
           	   browser.submit("#submit");
               assertThat(browser.url()).isEqualTo("http://localhost:3333/act");
               assertThat(browser.title()).isEqualTo("About :: W3ACT");
               browser.getCookies();
               // Check we can now access Wayback:
               browser.goTo("http://localhost:3333/act"+"/wayback/*/"+testURL);
               assertThat(browser.pageSource()).contains("Take Me Back");
               assertThat(browser.pageSource()).doesNotContain("form action=\"/act/login\"");

           
            }
        });
    }
	 

	

}