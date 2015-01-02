import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import models.User;

import org.junit.Test;

import play.Logger;
import play.mvc.Content;
import uk.bl.Const;
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
//    @Test 
//    public void testWhoisUk() {
//        running(fakeApplication(), new Runnable() {
//        	boolean result;
//            public void run() {
//            	try {
//        			result = Scope.checkWhois(URL1);
//        	    	Logger.info("test whois res: " + result);
//        	        assertThat(result).isEqualTo(true);
//        		} catch (WhoisException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
//          });
//    }
//    
//    /**
//     * This is a test for whois service (UK COM domain) that requires Internet connection.
//     */
////    @Test 
//    public void testWhoisUkCom() {
//        running(fakeApplication(), new Runnable() {
//        	boolean result;
//            public void run() {
//            	try {
//            		result = Scope.checkWhois(URL2);
//        	        assertThat(result).isEqualTo(true);
//        		} catch (WhoisException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
//          });
//    }
//    
////    @Test 
//    public void testLondon() {
//        running(fakeApplication(), new Runnable() {
//        	boolean result;
//            public void run() {
//            	try {
//        			result = Scope.checkExt("buydomains.london", "buydomains.london", Const.ScopeCheckType.ALL.name());
//        			assertThat(result).isEqualTo(true);
//        		} catch (WhoisException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
//          });
//
////    	boolean result = "buydomains.london".contains(".london");
////      assertThat(result).isEqualTo(true);
//    }
//
////    @Test 
//    public void testScopeIPLondon() {
//        running(fakeApplication(), new Runnable() {
//        	boolean result;
//            public void run() {
//            	try {
//        			result = Scope.checkScopeIp("buydomains.london", "buydomains.london");
//        	        assertThat(result).isEqualTo(true);
//        		} catch (WhoisException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
//          });
//    }
//    
////  @Test 
//  public void testScot() {
//      running(fakeApplication(), new Runnable() {
//      	boolean result;
//          public void run() {
//          	try {
//      			result = Scope.checkExt("bbc.scot", "bbc.scot", Const.ScopeCheckType.ALL.name());
//      			assertThat(result).isEqualTo(true);
//      		} catch (WhoisException e) {
//      			// TODO Auto-generated catch block
//      			e.printStackTrace();
//      		}
//          }
//        });
//
////  	boolean result = "buydomains.london".contains(".london");
////    assertThat(result).isEqualTo(true);
//  }
//
////  @Test 
//  public void testScopeDomainScot() {
//      running(fakeApplication(), new Runnable() {
//      	boolean result;
//          public void run() {
//          	try {
//      			//result = Scope.checkScopeDomain("https://www.gov.scot", "act-1");
//    			result = Scope.checkScopeIp("bbc.scot", "bbc.scot");
//      	        assertThat(result).isEqualTo(true);
//      		} catch (WhoisException e) {
//      			// TODO Auto-generated catch block
//      			e.printStackTrace();
//      		}
//          }
//        });
//  }
//  
//    /**
//     * This is a test for whois service (not UK domain) that requires Internet connection.
//     */
////    @Test 
//    public void testWhoisNotUk() {
//        running(fakeApplication(), new Runnable() {
//        	boolean result;
//            public void run() {
//            	try {
//            		result = Scope.checkWhois(URL3);
//        	        assertThat(result).isEqualTo(false);
//            	} catch (WhoisException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
//          });    	
//    }
    
//    @Test
    public void renderTemplate() {
        Content html = views.html.about.render("W3ACT", new User("Ross King")); 
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("W3ACT");
    }
  
   
}
