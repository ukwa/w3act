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
import play.Play;
import play.libs.Json;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.test.*;
import uk.bl.Const;
import uk.bl.Const.ScopeType;
import uk.bl.exception.ActException;
import uk.bl.scope.Scope;
import static play.test.Helpers.*;

public class APIIntegrationTests {

	private static long timeout_ms = 60*1000; // in milliseconds

	@Before
	public void initialize() {
	}

    @Test
    public void testInServer() {
        final String defaultUser = "wa-sysadm@bl.uk";
    	final String defaultPw = "sysAdmin";
    	
        running(testServer(3333, fakeApplication()), new Runnable() {
			@Override
			public void run() {
        		String act_url = "http://localhost:3333/act";
        		Logger.info("STEP Getting the homepage...");

        		String act_user = defaultUser;
        		String act_pw = defaultPw;
        		Logger.info("Logging into "+act_url);
        		Logger.info("Logging in as "+act_user+" "+act_pw);
        		JsonNode json = Json.newObject()
                        .put("email",act_user)
                        .put("password",act_pw)
                        .put("redirectToUrl", "");
        		
        		Promise<WSResponse> login = WS
        				.url(act_url + "/login")
        				.setContentType("application/x-www-form-urlencoded")
        				.setFollowRedirects(false)
        				.post(json);
        		WSResponse r = login.get(timeout_ms);
                
        		assertThat( r.getStatus() ).isLessThan(400);
                
        		if( r.getStatus() >= 400 ) {
        			Logger.error("Login failed.");
        		} else {
        			Logger.info("Login succeeded.");
        		}
        		String cookie = r.getHeader("Set-Cookie");
        		
        		Logger.info("STEP Clearing out Test Data...");
            	// Clear out any existing data:
                for( Target t : Target.findAll() ) {
            		t.delete();
            	}
                // Send up test data:
            	try {
            		Logger.info("STEP Sending Test Data...");
					sendTestData(act_url,cookie);
            		Logger.info("STEP Running API Tests...");
					runSomeAPITests(act_url,cookie);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
            }
			   
        });
       
    }
    
    
    /*
     * Method to populate a running system with some test data.
     */
    private static Long populate(String host, String cookie, String title, String url, String scope, String start_date, int expected) {
    	String one = "{\"title\": \""+title+"\", \"field_urls\": [\""+url+"\"],\"field_scope\": \""+scope+"\",\"field_crawl_start_date\": \""+start_date+"\", \"selector\": 1, \"field_crawl_frequency\": \"MONTHLY\" }";
    	Promise<WSResponse> result = WS.url(host+"/api/targets").setHeader("Cookie", cookie).setHeader("Content-Type", "application/json").post(one);
    	WSResponse response = result.get(timeout_ms);
    	Logger.info("populate GOT "+response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(expected);
    	if( response.getAllHeaders().containsKey(LOCATION)) {
        	String loc = response.getHeader(LOCATION);
        	Long id = Long.parseLong(loc.substring(loc.lastIndexOf('/')+1));
    	return id;
    	} else {
    		return null;
    	}
    }
    
    private static void sendTestData(String host, String cookie) throws JsonParseException, JsonMappingException, IOException {
    	populate(host, cookie, "anjackson.net news", "http://anjackson.net/news/","resource", "", 201 );
    	populate(host, cookie, "British Library", "http://www.bl.uk","root", "1425790800", 201 );
    	populate(host, cookie, "British Library News", "http://www.bl.uk/news/","plus1", "1425790800", 201 );
    	populate(host, cookie, "M&S", "http://marksandspencer.com/","subdomains", "1425790800", 201 );
    	populate(host, cookie, "Example", "http://example.com/","subdomains", "1425790800", 201 );
    	populate(host, cookie, "Example Subdomain", "http://subdomain.example.com/","subdomains", "1425790800", 201 );
    	
    }
    
    private static void runSomeAPITests(String host, String cookie) throws JsonParseException, JsonMappingException, IOException {
    	// Push and entry in:
		Logger.info("STEP API New Target...");
    	Long oid = populate(host, cookie, "anjackson.net", "http://anjackson.net/","root", "", 201 );
    	Logger.info("STEP Check target is not easily duplicated (without a slash)...");
    	Long noid = populate(host, cookie, "anjackson.net noslash", "http://anjackson.net","root", "", 409 );

    	// Get it back:
		Logger.info("STEP API Get Target...");
    	Target target = getTargetByID( host, oid, cookie);
		Long tid = target.id;
		Logger.info("Checking "+target.toString());
		assertThat(target.title).isEqualTo("anjackson.net");
		assertThat(target.fieldUrls.get(0).url).isEqualTo("http://anjackson.net/");
		assertThat(target.scope).isEqualTo(ScopeType.root.name());
		assertThat(target.crawlStartDate).isNull();
		
		// Now PUT to the same ID, changing some fields:
		Logger.info("STEP API Update Target (1)...");
		String update = "{\"id\": "+tid+", \"field_scope\": \"subdomains\", \"field_crawl_frequency\": \"MONTHLY\" }";
    	WSResponse response = WS.url(host+"/api/targets/"+oid).setHeader("Cookie", cookie).setHeader("Content-Type", "application/json").put(update).get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(OK);
		Logger.info("STEP API Get Target (1)...");
    	Target t2 = getTargetByID( host, oid, cookie);
		Logger.info("Now "+t2.toString());
		
		// And change scope back, but leave the frequency:
		Logger.info("STEP API Update Target (2)...");
		String update2 = "{\"id\": "+tid+", \"field_scope\": \"root\" }";
    	response = WS.url(host+"/api/targets/"+oid).setHeader("Cookie", cookie).setHeader("Content-Type", "application/json").put(update2).get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	assertThat(response.getStatus()).isEqualTo(OK);
    	Target t3 = getTargetByID( host, oid, cookie);
		Logger.info("Now "+t2.toString());
		
		// Check the default value for the frequency field in the Target class did not override the original value in the merge.
		assertThat(t3.crawlFrequency).isEqualTo(Const.CrawlFrequency.MONTHLY.name());
    }
    
    private static Target getTargetByID( String host, Long id, String cookie ) throws JsonParseException, JsonMappingException, IOException {
    	WSResponse response = WS.url(host+"/api/targets/"+id).setFollowRedirects(false).get().get(timeout_ms);
    	Logger.info(response.getStatus()+" "+response.getStatusText());
    	Logger.debug(response.getBody());
    	assertThat(response.getStatus()).isNotEqualTo(OK);
    	response = WS.url(host+"/api/targets/"+id).setHeader("Cookie", cookie).get().get(timeout_ms);
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
    	//sendTestData("http://localhost:9000/act");
    }
}
