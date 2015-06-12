import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import play.Configuration;
import play.Logger;
import play.libs.WS;
import play.libs.F.Callback;
import play.mvc.Result;
import play.test.TestBrowser;

import com.avaje.ebean.ExpressionList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import uk.bl.Const;
import uk.bl.api.FormHelper;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

public class LicenseInheritanceTest {

	private Target target = null;
	private List<FieldUrl> fieldUrls;
//	private String url = "http://www.bl.uk/";
	private String url = "http://www.parliament.uk/";
//	private String url = "https://www.gov.uk/";  
//	private String url = "http://www.somersetremembers.com/";
	private Configuration additionalConfigurations;
	
	private static Boolean scopeHosting;
	private static Boolean scopeDomain;
	private static Boolean scopeRegistration;
	private static Boolean scopeManual;
	private static Boolean scopeLicense;
	
	@Before
	public void setUp() throws ActException{	
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl(url)); 
		target.fieldUrls = fieldUrls;	
		// Set up a test configuration;
	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/dev.conf"));
	    additionalConfigurations = new Configuration(additionalConfig);
	}

	
		@Test
	    public void test() {
	        running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
	        	
	        	List<Target> list = new ArrayList<Target>();
	        	
	            public void run() {      
	            	
	            	/*****************Checking the NPLD scopes & Licensing of a given URL******************/
	            	
	            	scopeHosting = target.isUkHosting();
					try {
						scopeDomain = target.isTopLevelDomain();
					} catch (ActException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						scopeRegistration = target.isUkRegistration();
					} catch (WhoisException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					scopeManual = target.checkManualScope();
					scopeLicense = target.checkLicense();
				//	scopeLicense = target.indicateUkwaLicenceStatus();
				//	scopeLicense = target.indicateLicenses();
				//	scopeLicense = target.hasLicenses();
					

	        		Logger.info("Scopes and Licensing::::::::::::::: " + target.fieldUrls +  " - " + scopeHosting+ " - " + scopeDomain+ " - " + scopeRegistration+ " - " + scopeManual+ " - " +scopeLicense);
	        		
	        		/***********Fetch the child URLs**************************/
	        		list = Target.filterUrl(url);
	        		Logger.info("Number of child URLs::::::::::::::: " + list.size());
	        		/****************Check child url scopes if the parent url is in scope*****************/
	        		
	        		if(scopeHosting || scopeDomain || scopeRegistration || scopeManual){	        					    	        				
	            			Boolean scopeHosting = null;
	            			Boolean scopeDomain = null;
	            			Boolean scopeRegistration = null;
	            			Boolean scopeManual = null;
	            			
	            	    	for(int i=0; i<list.size(); i++){
	            	    		
	            	    		scopeHosting = list.get(i).isUkHosting();
	            	    		try {
	            	    			scopeDomain = list.get(i).isTopLevelDomain();
	    						} catch (ActException e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	            	    		try {
	            	    			scopeRegistration = list.get(i).isUkRegistration();
	    						} catch (WhoisException e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	            	    		scopeManual = list.get(i).checkManualScope();
	            	    		
	            	    		Logger.info("In NPLD scope:URLs:::::::::: " + list.get(i).fieldUrls.get(0).url +  " - " + scopeHosting+ " - " + scopeDomain+ " - " + scopeRegistration+ " - " + scopeManual);
	            	    		assertThat(scopeHosting || scopeDomain || scopeRegistration || scopeManual).isEqualTo(true);
	            	    	} 
	            	    	
	            	    	
	        		}
	        		
	        		/****************Check child url licensing if the parent url have license*****************/
	        		
	        		if(scopeLicense){	        			
	        			Boolean scopeLicense = null;
	        			
	        	    	for(int i=0; i<list.size(); i++){
	        	    		
	        	    		scopeLicense = list.get(i).checkLicense();
	        	    		
	        	    		Logger.info("In Licensing:URLs:::::::::: " + list.get(i).fieldUrls.get(0).url +  " - " + scopeLicense);
	        	    		assertThat(scopeLicense).isEqualTo(true);
	        	    	} 	        	    	
	        	    	       			
        		}
	            }
	          });
	    	}



}


