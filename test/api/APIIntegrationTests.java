package api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Configuration;
import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.test.*;
import uk.bl.Const;
import uk.bl.Const.ScopeType;
import uk.bl.exception.ActException;
import uk.bl.scope.Scope;
import static play.test.Helpers.*;

public class APIIntegrationTests {

    private static String defaultUser = "wa-sysadm@bl.uk";
	private static String defaultPw = "sysAdmin";
	
	private static long timeout_ms = 60*1000; // in milliseconds
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
            	// Clear out any existing data:
                for( Target t : Target.findAll() ) {
            		t.delete();
            	}
                // Send up test data:
            	try {
            		String host = "http://localhost:3333/act";
            		Logger.info("STEP Sending Test Data...");
					sendTestData(host);
            		Logger.info("STEP Running API Tests...");
					runSomeAPITests(host);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
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
    private static Long populate(String host, String username, String password, String title, String url, String scope, String start_date, int expected) {
    	String one = "{\"title\": \""+title+"\", \"field_urls\": [\""+url+"\"],\"field_scope\": \""+scope+"\",\"field_crawl_start_date\": \""+start_date+"\", \"selector\": 1, \"field_crawl_frequency\": \"MONTHLY\" }";
    	Promise<WS.Response> result = WS.url(host+"/api/targets").setAuth(username, password).setHeader("Content-Type", "application/json").post(one);
    	WS.Response response = result.get(timeout_ms);
    	Logger.info("GOT "+response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(expected);
    	String loc = response.getHeader(LOCATION);
    	Long id = Long.parseLong(loc.substring(loc.lastIndexOf('/')+1));
    	return id;
    }
    
    private static void sendTestData(String host) throws JsonParseException, JsonMappingException, IOException {
    	populate(host, defaultUser, defaultPw, "anjackson.net news", "http://anjackson.net/news/","resource", "", 201 );
    	populate(host, defaultUser, defaultPw, "British Library", "http://www.bl.uk","root", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "British Library News", "http://www.bl.uk/news/","plus1", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "M&S", "http://marksandspencer.com/","subdomains", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "Example", "http://example.com/","subdomains", "1425790800", 201 );
    	populate(host, defaultUser, defaultPw, "Example Subdomain", "http://subdomain.example.com/","subdomains", "1425790800", 201 );
    	
    }
    
    private static void runSomeAPITests(String host) throws JsonParseException, JsonMappingException, IOException {
    	// Push and entry in:
		Logger.info("STEP API New Target...");
    	Long oid = populate(host, defaultUser, defaultPw, "anjackson.net", "http://anjackson.net/","root", "", 201 );

    	// Get it back:
		Logger.info("STEP API Get Target...");
    	Target target = getTargetByID( host, oid);
		Long tid = target.id;
		Logger.info("Checking "+target.toString());
		assertThat(target.title).isEqualTo("anjackson.net");
		assertThat(target.fieldUrls.get(0).url).isEqualTo("http://anjackson.net/");
		assertThat(target.scope).isEqualTo(ScopeType.root.name());
		assertThat(target.crawlStartDate).isNull();
		
		// Now PUT to the same ID, changing some fields:
		Logger.info("STEP API Update Target (1)...");
		String update = "{\"id\": "+tid+", \"field_scope\": \"subdomains\", \"field_crawl_frequency\": \"MONTHLY\" }";
    	Response response = WS.url(host+"/api/targets/"+oid).setAuth(defaultUser, defaultPw).setHeader("Content-Type", "application/json").put(update).get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(OK);
		Logger.info("STEP API Get Target (1)...");
    	Target t2 = getTargetByID( host, oid);
		Logger.info("Now "+t2.toString());
		
		// And change scope back, but leave the frequency:
		Logger.info("STEP API Update Target (2)...");
		String update2 = "{\"id\": "+tid+", \"field_scope\": \"root\" }";
    	response = WS.url(host+"/api/targets/"+oid).setAuth(defaultUser, defaultPw).setHeader("Content-Type", "application/json").put(update2).get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(OK);
    	Target t3 = getTargetByID( host, oid);
		Logger.info("Now "+t2.toString());
		
		// Check the default value for the frequency field in the Target class did not override the original value in the merge.
		assertThat(t3.crawlFrequency).isEqualTo(Const.CrawlFrequency.MONTHLY.name());
    }
    
    private static Target getTargetByID( String host, Long id ) throws JsonParseException, JsonMappingException, IOException {
    	Response response = WS.url(host+"/api/targets/"+id).get().get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	Logger.debug(response.getBody());
    	assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED);
    	response = WS.url(host+"/api/targets/"+id).setAuth(defaultUser, defaultPw).get().get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	Logger.debug(response.getBody());
    	assertThat(response.getStatus()).isEqualTo(OK);
    	ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
		return objectMapper.readValue(response.getBody(), Target.class);
    }
    
    /**
     * To populate a specified system with test data.
     * 
     * @param args
     */
    public static void main( String args[] ) throws Exception {
    	sendTestData("http://localhost:9000/act");
    }
}
