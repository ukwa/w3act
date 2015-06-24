import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.FieldUrl;
import models.Target;
import models.User;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import static play.test.Helpers.*;
import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.Yaml;
import play.libs.F.Callback;
import play.mvc.Result;
import play.test.FakeRequest;
import play.test.TestBrowser;
import uk.bl.Const;
import uk.bl.Const.ScopeType;
import uk.bl.exception.ActException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import controllers.ApplicationController;
import controllers.WaybackController;
import static controllers.routes.ref.ApplicationController;




public class QaWaybackTest {
		
	private Configuration additionalConfigurations;
	private Configuration accountConfigurations;
	User user = new User();
	User ldlUser = new User();
	private static String testURL = "http://www.bl.uk";  
	private static HttpURLConnection http;
	private static URL wayback_url;
	private static int statusCode = 0;
	
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
            	browser.goTo("http://localhost:3333/test");// + ApplicationController.login()).url();//example for reverse route, alternatively use something like "http://localhost:3333/login"
            	browser.fill("#email").with("niladree.bhattacharj@bl.uk");
            	browser.fill("#password").with("Nil@BL");            	
            	browser.submit("#submit");//trigger submit button on the form
            	//after finished request: http://www.playframework.org/documentation/api/2.0.4/java/play/test/TestBrowser.html
            	browser.getCookies(); //read only cookies
            }
        });
    }
	 
//	 @Test
//	  public void testInServer() {
//	    running(testServer(3333,fakeApplication(additionalConfigurations.asMap())), new Runnable() {
//	        public void run() {
//	        	
//	            Map<String, String> parameters = new HashMap<String, String>();
//	            parameters.put("email", "niladree.bhattacharj@bl.uk");
//	            parameters.put("password", "Nil@BL");
//	            FakeRequest fakeRequest = new FakeRequest().withSession("key", "value").withCookies().withFormUrlEncodedBody(parameters);  //name, value, maxAge, path, domain, secure, httpOnly
//	            Result result = callAction(ApplicationController.login(), fakeRequest);
//	            int responseCode = status(result);
//	            
//	            Logger.info("responseCode:::::   "+ responseCode);
//	            assertThat(responseCode).isEqualTo(OK);
//	        }
//	    });
//	  }
	
//	 @Test 
//	  public void testScot() {
//	      running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
//	    	 
//	    	  private User addUser(String name, String abbreviation ) {
//	        		User user = new User();
//	        		user.name = name;
//	        		user.affiliation = abbreviation;
//	        		
//	        		// And save it:
//	        		user.save();
//	        		
//	        		// And also return:
//	        		return user;
//	        	}
//	    	  
//	          public void run() {
//	        	  
//	        	// Clear out any existing data from other tests:
//	                for( User user : User.findAll() ) {
//	                	user.delete();
//	            	}
//	                
//	           // Add some users
//	                User archivist  = this.addUser("niladree.bhattacharj@bl.uk","BL");
//	                User expert_user  = this.addUser("nil.bhattacharjee@bl.uk","BSCA");
//	                User inputUser = archivist;   									 //Change here to run the test
//	                
//	           //Test to check QA Wayback access
//	                Logger.debug("VALUES::::::::::::::::::::::::"+ inputUser.name);
//	                Logger.debug("VALUES::::::::::::::::::::::::"+ inputUser.affiliation);
//	                
//	                if(inputUser.affiliation.equals("BL") || 
//	                   inputUser.affiliation.equals("NLS") || 
//	                   inputUser.affiliation.equals("NLW") || 
//	                   inputUser.affiliation.equals("Bodleian") || 
//	                   inputUser.affiliation.equals("CAM") || 
//	                   inputUser.affiliation.equals("TCD")){
//	                	
//	                	//This will always give status code 200, I guess alternative approach needed
//	                	try{
////	                	String wayBackUrl = additionalConfigurations.getString("server_name")+additionalConfigurations.getString("application.context");
////	                	
////	                	wayback_url = new URL(wayBackUrl+"/wayback/*/"+testURL);
////	                	Logger.debug("wayback_url:::::::::::::::::::::::: "+ wayback_url);
////	                	http = (HttpURLConnection)wayback_url.openConnection();
////	        			http.setRequestMethod("GET");
////	        			http.connect();
////
////	        			statusCode = http.getResponseCode();
////	        			Logger.debug("statusCode::::::::::::::::::::::::"+ statusCode);
////	        			assertThat(statusCode).isEqualTo(200);
//	                		assert(true);
//	        			
//	                	}catch( Exception e ) {
//	            			Logger.warn("Exception while checking status code",e);
//	            		}
//	                }
//	                if(inputUser.affiliation.equals("AIT") ||
//	                   inputUser.affiliation.equals("MNH") ||
// 	                   inputUser.affiliation.equals("MTCD") || 
// 	                   inputUser.affiliation.equals("BSCA")){
//	                	
//	                	assert(true);
//	                }
//	                else
//	                	assert(false);
//	                		                
//	          }
//	        });
//	 }

}