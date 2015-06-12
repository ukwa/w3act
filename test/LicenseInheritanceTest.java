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

import org.junit.After;
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
import uk.bl.Const.ScopeType;
import uk.bl.api.FormHelper;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.record.WhoisResult;

public class LicenseInheritanceTest {

	private Target target = null;
	private List<FieldUrl> fieldUrls;
	private String url = "http://www.bl.uk/";
//	private String url = "http://www.parliament.uk/";
//	private String url = "https://www.gov.uk/";  
//	private String url = "http://www.somersetremembers.com/";
	private Configuration additionalConfigurations;
	
	private static Boolean scopeHosting;
	private static Boolean scopeDomain;
	private static Boolean scopeRegistration;
	private static Boolean scopeManual;
	private static Boolean scopeLicense;
	
	@Before
	public void setUp() throws ActException {	
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl(url)); 
		target.fieldUrls = fieldUrls;
		// Set up a test configuration;
	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/dev.conf"));
	    additionalConfigurations = new Configuration(additionalConfig);
	}
	
		@Test
	    public void testLicenseInheritance() {
	        running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
	        	
	        	private Target addTarget(String title, String[] urls, ScopeType scope ) {
	        		Target t = new Target();
	        		t.title = title;
	        		t.active = true;
	        		t.fieldUrls = new ArrayList<FieldUrl>();
	        		for( String nurl : urls ) {
	        			try {
							t.fieldUrls.add(new FieldUrl(nurl));
						} catch (ActException e) {
							throw(new RuntimeException(e));
						} 
	        		}
	        		if( scope != null ) 
	        			t.scope = scope.name();
	        		// And save it:
	        		t.save();
	        		
	        		// And also return:
	        		return t;
	        	}

	        	
	            public void run() {
	            	
	            	/***************** Add some test data ******************/
	            	
					Target bl  = this.addTarget("British Library", new String[]{ "http://www.bl.uk" }, ScopeType.subdomains);
					Target bln = this.addTarget("British Library News", new String[]{ "http://www.bl.uk/news/" }, ScopeType.subdomains);
					Target bld = this.addTarget("British Library Datasets", new String[]{ "http://data.bl.uk/" }, null);
					Target eg  = this.addTarget("Example", new String[]{ "http://example.com/" }, ScopeType.subdomains);
					Target egs  = this.addTarget("Example Subdomain", new String[]{ "http://subdomain.example.com/" }, null);
					Target egss = this.addTarget("Example Subsection", new String[]{ "http://example.com/subsection/" }, null);
	            	
	            	/***************** Perform some basic tests ******************/
	            	assertThat(eg.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	assertThat(egs.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	assertThat(egss.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	eg.setProfessionalJudgement(true);
	            	// If it was smarter, it would check that a professional reason was required.
	            	//assertThat(eg.isInScopeAllWithoutLicense()).isFalse();
	            	//assertThat(egs.isInScopeAllWithoutLicense()).isFalse();
	            	//eg.setProfessionalJudgementExp("Because I say so!");
	            	assertThat(eg.isInScopeAllOrInheritedWithoutLicense()).isTrue();
	            	// This won't pick up yet:
	            	assertThat(egs.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	assertThat(egss.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	eg.save();
	            	// Now it should pick up the subdomain inheritance:
	            	assertThat(egs.isInScopeAllOrInheritedWithoutLicense()).isTrue();
	            	assertThat(egss.isInScopeAllOrInheritedWithoutLicense()).isTrue();
	            	// Now switch to root scope and check the inheritance is lost:
	            	eg.scope = ScopeType.root.name();
	            	eg.save();
	            	assertThat(egs.isInScopeAllOrInheritedWithoutLicense()).isFalse();
	            	assertThat(egss.isInScopeAllOrInheritedWithoutLicense()).isTrue();
	            	
	            	Logger.info("More fine-grained tests...");
	            	
	            	/*****************Checking the NPLD scopes & Licensing of a given URL******************/
	            	
	            	scopeHosting = target.isUkHosting();
	            	scopeDomain = target.isTopLevelDomain();
					try {
						scopeRegistration = target.isUkRegistration();
					} catch (WhoisException e) {
						throw(new RuntimeException(e));
					}
					scopeManual = target.checkManualScope();
					scopeLicense = target.checkLicense();
				//	scopeLicense = target.indicateUkwaLicenceStatus();
				//	scopeLicense = target.indicateLicenses();
				//	scopeLicense = target.hasLicenses();
					

	        		Logger.info("Scopes and Licensing::::::::::::::: " + target.fieldUrls +  " - " + scopeHosting+ " - " + scopeDomain+ " - " + scopeRegistration+ " - " + scopeManual+ " - " +scopeLicense);
	        		
	        		/***********Fetch the child URLs**************************/
		        	List<Target> list = Target.filterUrl(url);
	        		Logger.info("Number of child URLs::::::::::::::: " + list.size());
	        		/****************Check child url scopes if the parent url is in scope*****************/
	        		
	        		if(scopeHosting || scopeDomain || scopeRegistration || scopeManual){	        					    	        				
	            			Boolean scopeHosting = null;
	            			Boolean scopeDomain = null;
	            			Boolean scopeRegistration = null;
	            			Boolean scopeManual = null;
	            			
	            	    	for(int i=0; i<list.size(); i++){
	            	    		Logger.info("Looking at: "+list.get(i).title);
	            	    		
	            	    		scopeHosting = list.get(i).isUkHosting();
	            	    		scopeDomain = list.get(i).isTopLevelDomain();
	            	    			
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

		@Test
		public void testWhois() throws ActException {
			String url = "http://bl.uk/";
			boolean wr = Scope.INSTANCE.checkWhois(url, null);
			assertThat(wr).isTrue();
		}
		
		@After
		public void clearTarget() {
        	List<Target> list = Target.findAll();
        	for( Target t : list ) {
        		t.delete();
        	}
		}


}


