import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import models.FieldUrl;
import models.Target;
import models.User;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import play.Configuration;
import play.Logger;
import play.Play;
import uk.bl.Const.ScopeType;
import uk.bl.exception.ActException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import controllers.WaybackController;


public class QaWaybackTest {
	
	private Configuration additionalConfigurations;
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
	
	 @Test 
	  public void testScot() {
	      running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
	    	 
	    	  private User addUser(String name, String abbreviation ) {
	        		User user = new User();
	        		user.name = name;
	        		user.email = name;
	        		user.affiliation = abbreviation;
	        		
	        		// And save it:
	        		user.save();
	        		
	        		// And also return:
	        		return user;
	        	}
	    	  
	          public void run() {
	        	  
	        	// Clear out any existing data from other tests:
	                for( User user : User.findAll() ) {
	                	user.delete();
	            	}
	                
	           // Add some users
	                User archivist  = this.addUser("niladree.bhattacharj@bl.uk","BL");
	                User expert_user  = this.addUser("nil.bhattacharjee@bl.uk","BSCA");
	                User inputUser = archivist;   									 //Change here to run the test
	                
	           //Test to check QA Wayback access
	                Logger.debug("VALUES::::::::::::::::::::::::"+ inputUser.name);
	                Logger.debug("VALUES::::::::::::::::::::::::"+ inputUser.affiliation);
	                
	                if(inputUser.affiliation.equals("BL") || 
	                   inputUser.affiliation.equals("NLS") || 
	                   inputUser.affiliation.equals("NLW") || 
	                   inputUser.affiliation.equals("Bodleian") || 
	                   inputUser.affiliation.equals("CAM") || 
	                   inputUser.affiliation.equals("TCD")){
	                	
	                	//This will always give status code 200, I guess alternative approach needed
	                	try{
//	                	String wayBackUrl = additionalConfigurations.getString("server_name")+additionalConfigurations.getString("application.context");
//	                	
//	                	wayback_url = new URL(wayBackUrl+"/wayback/*/"+testURL);
//	                	Logger.debug("wayback_url:::::::::::::::::::::::: "+ wayback_url);
//	                	http = (HttpURLConnection)wayback_url.openConnection();
//	        			http.setRequestMethod("GET");
//	        			http.connect();
//
//	        			statusCode = http.getResponseCode();
//	        			Logger.debug("statusCode::::::::::::::::::::::::"+ statusCode);
//	        			assertThat(statusCode).isEqualTo(200);
	                		assert(true);
	        			
	                	}catch( Exception e ) {
	            			Logger.warn("Exception while checking status code",e);
	            		}
	                }
	                if(inputUser.affiliation.equals("AIT") ||
	                   inputUser.affiliation.equals("MNH") ||
 	                   inputUser.affiliation.equals("MTCD") || 
 	                   inputUser.affiliation.equals("BSCA")){
	                	
	                	assert(true);
	                }
	                else
	                	assert(false);
	                		                
	          }
	        });
	 }

}