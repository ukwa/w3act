package api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Configuration;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.WS;
import play.test.*;
import uk.bl.exception.ActException;
import uk.bl.scope.Scope;
import static play.test.Helpers.*;

public class APIIntegrationTests {

    private static String defaultUser = "wa-sysadm@bl.uk";
	private static String defaultPw = "sysAdmin";
	
	private static long timeout_ms = 20*1000; // in milliseconds
	private Configuration additionalConfigurations;

	@Before
	public void initialize() throws ActException{
	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/dev.conf"));
	    additionalConfigurations = new Configuration(additionalConfig);
	}

    @Test
    public void testInServer() {
        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), new Runnable() {
			public void run() {
                assertThat(
                    WS.url("http://localhost:3333/act").get().get(timeout_ms).getStatus()
                ).isEqualTo(OK);
                // Send up test data:
            	sendTestData("http://localhost:3333/act");
            }
			   
        });
       
    }
    

    /**
     * Example from:
     * 
     * https://www.playframework.com/documentation/2.2.x/JavaFunctionalTest
     * 
     */
    //@Test
    public void runInBrowser() {
        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
               browser.goTo("http://localhost:3333"); 
               assertThat(browser.$("#title").getTexts().get(0)).isEqualTo("Hello Guest");
               browser.$("a").click();
               assertThat(browser.url()).isEqualTo("http://localhost:3333/Coco");
               assertThat(browser.$("#title", 0).getText()).isEqualTo("Hello Coco");
            }
        });
    }
    
    /*
     * Method to populate a running system with some test data.
     */
    private static void populate(String host, String username, String password, String title, String url, String scope, String start_date, int expected) {
    	String one = "{\"title\": \""+title+"\", \"field_urls\": [\""+url+"\"],\"field_scope\": \""+scope+"\",\"field_crawl_start_date\": \""+start_date+"\"}";
    	Promise<WS.Response> result = WS.url(host+"/api/targets").setHeader("Content-Type", "application/json").setAuth(username, password).post(one);
    	WS.Response response = result.get(timeout_ms);
    	System.out.println("GOT "+response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(expected);
    }
    
    private static void sendTestData(String host) {
    	populate(host, defaultUser, defaultPw, "anjackson.net", "http://anjackson.net/","root", "", 201 );
    	populate(host, defaultUser, defaultPw, "anjackson.net news", "http://anjackson.net/news/","resource", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "British Library", "http://www.bl.uk","root", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "British Library News", "http://www.bl.uk/news/","plus1", "1425790800", 201 );
    }
    
    /**
     * To populate a specified system with test data.
     * 
     * @param args
     */
    public static void main( String args[] ) {
    	sendTestData("http://localhost:9000/act");
    }
}
