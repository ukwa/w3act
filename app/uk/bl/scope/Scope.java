package uk.bl.scope;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import models.FieldUrl;
import models.Target;
import play.Logger;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.record.WhoisContact;
import uk.bl.wa.whois.record.WhoisResult;

/**
 * This class implements scope rule engine.
 * A Target is in scope if any of the following statements is true:
 *
 *   Legal Deposit
 *   =============
 *   1. Manual rules settings in Target edit page
 *      1.1. The Target is known to be hosted in the UK (manual boolean field).
 *      1.2. The Target features an page that specified a UK postal address 
 *      	 (a manual boolean field plus a text field to hold a specific URL that contains the address).
 *      1.3. The Target is known to be a UK publication, according to correspondence with a curator 
 *      	 (a manual boolean field plus a text field to hold details of the correspondence).
 *      1.4. The Target is known to be a UK publication, in the professional judgement of a curator 
 *      	 (a manual boolean field plus a text field to hold the justification).
 *      1.5. If no LD criteria met - checking result for scope is negativeTarget
 *      	 (a manual boolean field).
 *      
 *   2. By permission
 *      The Target is one for which we have a license that gives us permission to crawl the site 
 *      (and make it available), even if the Target does not fall under any Legal Deposit criteria.
 *
 *   3. All URLs for this Target meet at least one of the following automated criteria:
 *      3.1 The authority of the URI (i.e. the hostname) end with '.uk' or other acceptable TLD (e.g. '.scot').
 *      3.2 The IP address associated with the URI is geo-located in the UK 
 *          (using this GeoIP2 database, in a manner similar to our H3 GeoIP module).
 *      3.3 Use whois lookup service to check whether the given domain name is associated with a UK registrant. 
 *          
 */
public enum Scope {

	INSTANCE;
	
	private static final String UK_DOMAIN       = ".uk";
	private static final String LONDON_DOMAIN   = ".london";
	private static final String SCOT_DOMAIN     = ".scot";
	private static final String WALES_DOMAIN     = ".wales";
	private static final String CYMRU_DOMAIN     = ".cymru";
	
	public static List<String> DOMAINS;

	static {
		DOMAINS = new ArrayList<String>();
		DOMAINS.add(UK_DOMAIN);
		DOMAINS.add(LONDON_DOMAIN);
		DOMAINS.add(SCOT_DOMAIN);
		DOMAINS.add(WALES_DOMAIN);
		DOMAINS.add(CYMRU_DOMAIN);
	}
	
	public static final String GEO_IP_SERVICE  = "GeoLite2-City.mmdb";
	public static final String UK_COUNTRY_CODE = "GB";
	public static final String HTTP            = "http://";
	public static final String HTTPS           = "https://";
	public static final String WWW             = "www.";
	public static final String END_STR         = "/";
	
	private static final int   WHOIS_TIMEOUT   = 15; // Whois lookup timeout (seconds)
	public  static boolean     WHOIS_ENABLED   = false; // Should whois be used at all?
	
	public static DatabaseReader databaseReader;
	
	static {
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File(GEO_IP_SERVICE);
		
		// This creates the DatabaseReader object, which should be reused across
		// lookups.
		try {
			databaseReader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			Logger.warn("Can't read database file. " + e);
		}
	}
	
	/**
	 * This method is the rule engine for checking if a given URL is in scope.
	 * 
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param whether to include by-permission as acceptable
	 * 
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean check(String url, Target target, boolean includedByPermission) {
        url = normalizeUrl(url);
        Logger.debug("Scope.check url: " + url);
        
        /**
         * Check if given URL is already in project database in a table LookupEntry. 
         * If this is in return associated value, otherwise process lookup using expert rules.
         */
        /*
        boolean inProjectDb = false;
        if (url != null && url.length() > 0) {
//        	List<LookupEntry> lookupEntryCount = LookupEntry.filterByName(url);
        	LookupEntry resLookupEntry = LookupEntry.findBySiteName(url);   
        	if (resLookupEntry != null && !resLookupEntry.name.toLowerCase().equals(Const.NONE)) {
//        	if (lookupEntryCount.size() > 0) {
        		inProjectDb = true;
        		res = LookupEntry.getValueByUrl(url);
        		Logger.debug("check lookup entry for '" + url + "' is in database with value: " + res);
        	}
        }
        return res;
    	Logger.debug("URL not in database - calculate scope");
        */
        
        // read Target fields with manual entries and match to the given NID URL (Rules 1.1 - 1.5)
        if (target != null && target.checkManualScope() ) {
        	return true;
        }

    	// Rule 2: by permission
        if (includedByPermission && target != null && target.checkLicense() ) {
        	return true;
        }

        // Rule 3.1: check domain name
        if (url != null && url.length() > 0 && checkScopeDomain(url)) {
        	return true;
        }
        
        // Rule 3.2: check geo IP
        if ( url != null && url.length() > 0 && checkGeoIp(url) ) {
        	return true;
        }
        
        // Rule 3.3: check whois lookup service
        if ( url != null && url.length() > 0 ) {
        	if(checkWhois(url, target) ) {
        		return true;
			}
        }
        return false;
	}
	
	/**
	 * 
	 * Checks if a Target is in NPLD scope by running each of it's URL fields through the checks.
	 * 
	 * @param target
	 * @return
	 * @throws WhoisException
	 */
	public boolean check(Target target, boolean includedByPermission ) {
		for( FieldUrl url : target.fieldUrls) {
			if( ! check( url.url, target, includedByPermission) ) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * This method queries geo IP from database
	 * 
	 * Synchronized in case the underlying database is not thread-safe.
	 * 
	 * @param ip - The host IP
	 * @return true if in UK domain
	 */
	public synchronized boolean queryDb(String ip) {
		boolean res = false;
		
		try {		
			// Find city by given IP
			CityResponse response = databaseReader.city(InetAddress.getByName(ip));
			Logger.info(response.getCountry().getIsoCode()); 
			Logger.info(response.getCountry().getName()); 
			// Check country code in city response
			if (response.getCountry().getIsoCode().equals(UK_COUNTRY_CODE)) {
				res = true;
			}
		} catch (Exception e) {
            Logger.warn("GeoIP error. ", e);
		}
		Logger.debug("Geo IP query result: " + res);
		return res;
	}
	
	/**
	 * This method normalizes passed URL that it is appropriate for IP calculation.
	 * @param url The passed URL
	 * @return normalized URL
	 */
	public static String normalizeUrl(String url, boolean slash) {
		String res = url;
		if (res != null && res.length() > 0) {
	        //if (!res.contains(WWW) && !res.contains(HTTP) && !res.contains(HTTPS)) {
	        //	res = WWW + res;
	        //}
	        if (!res.contains(HTTP)) {
		        if (!res.contains(HTTPS)) {
		        	res = HTTP + res;
		        }
	        }
	        if (slash && !res.endsWith(END_STR)) {
	        	res = res + END_STR;
	        }
		}
//        Logger.debug("normalized URL: " + res);
		return res;
	}

	public static String normalizeUrl(String url) {
		return normalizeUrl(url, true);
	}

	public static String normalizeUrlNoSlash(String url) {
		return normalizeUrl(url, false);
	}
	
	

	/**
	 * This method comprises rule engine for checking if a given URL is in scope for rules 
	 * associated with Domain analysis.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public static boolean checkScopeDomain(String ourl) {
		// Grab the domain part:
        String domain;
        try {
			domain = getDomainFromUrl(normalizeUrl(ourl));
			Logger.debug("Checking domain: "+domain);
		} catch (ActException e) {
			Logger.error("Exception when normalising "+ourl, e);
			return false;
		}
        
        // Rule 3.1: check domain name ends with an acceptable suffix:
        if ( domain != null ) {
        	domain = domain.toLowerCase();
        	for( String okd : DOMAINS ) {
        		if ( domain.endsWith(okd)) {
	        		return true;
	        	}
        	}
        }
        return false;
	}

	/**
	 * This method extracts host from the given URL and checks geo IP using geo IP database.
	 * @param url
	 * @return true if in UK domain
	 */
	public boolean checkGeoIp(String url) {
		boolean res = false;
		String ip = getIpFromUrl(url);
		Logger.debug("ip: " + ip);
		res = queryDb(ip);
		return res;
	}
	
	/**
	 * Check parsed WHOIS result for UK/GB.
	 * 
	 * @param whoIsRes
	 * @return
	 */
	public static boolean isUKRegistrant( WhoisResult whoIsRes ) {
		boolean isUK = false;

		for( WhoisContact c : whoIsRes.getRegistrantContacts() ) {
			if( "uk".equalsIgnoreCase(c.getCountry_code()) || 
				"gb".equalsIgnoreCase(c.getCountry_code()) ) {
				isUK = true;
				break;
			}
			if( "united kingdom".equalsIgnoreCase(c.getCountry()) || 
				"great britain".equalsIgnoreCase(c.getCountry()) ) {
				isUK = true;
				break;
			}
		}
		
		return isUK;
	}
	
	/**
	 * This method extracts domain name from the given URL and checks country or country code
	 * in response using whois lookup service.
	 * @param url
	 * @return true if in UK domain
	 * @throws WhoisException 
	 */
	public boolean checkWhois(String url, Target target) {
		if( WHOIS_ENABLED != true ) {
			Logger.warn("WHOIS is currently disabled!");
			return false;
		}
		// Perform whois check:
		Logger.info("Performing whois lookup on "+url);
		boolean res = false;
    	try {
    		System.getProperties().put("JRUBY_OPTS", "--1.9");
        	JRubyWhois whoIs = new JRubyWhois();
        	Logger.debug("checkWhois: " + url);
        	WhoisResult whoIsRes = whoIs.lookup(getDomainFromUrl(url), WHOIS_TIMEOUT);
        	res = isUKRegistrant(whoIsRes);
        	Logger.debug("isUKRegistrant?: " + res);
        	if( whoIsRes.getRegistrantContacts() != null ) {
        		for( WhoisContact wrc : whoIsRes.getRegistrantContacts()) {
        			Logger.debug("WhoIsRes: "+wrc.getName()+" "+wrc.getCountry()+" "+wrc.getCountry_code());
        		}
        	}
	        if( target != null )
	        	ScopeLookupEntries.storeInProjectDb(url, "WHOIS", res, target);
    	} catch (Exception e) {
    		Logger.warn("whois lookup message: " + e.getMessage(),e);
	        if( target != null ) 
	        	ScopeLookupEntries.storeInProjectDb(url, "WHOIS", false, target);
    	}
    	Logger.debug("whois res: " + res);
		return res;
	}
	
	
	/**
	 * This method converts URL to IP address.
	 * @param url
	 * @return IP address as a string
	 */
	public String getIpFromUrl(String url) {
		String ip = "";
		InetAddress address;
		try {
			address = InetAddress.getByName(new URL(url).getHost());
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			Logger.debug("ip calculation unknown host error for url=" + url + ". " + e.getMessage());
		} catch (MalformedURLException e) {
			Logger.debug("ip calculation error for url=" + url + ". " + e.getMessage());
		}
        return ip;
	}
	

	/**
	 * Actually gets the host.
	 * 
	 * @param url
	 * @return
	 * @throws ActException
	 */
	public static String getDomainFromUrl(String url) throws ActException {
	    URL uri;
		try {
			uri = new URL(url);
			Logger.debug("getDomainFromUrl: "+uri);
			String domain = uri.getHost();
			Logger.debug("getDomainFromUrl GOT: "+domain);
			if (StringUtils.isNotEmpty(domain)) {
				return domain.startsWith(WWW) ? domain.substring(4) : domain;
			}
		} catch (MalformedURLException e) {
			throw new ActException(e);
		}
		return null;
	}
	
	public boolean isUkHosting(String url) {
		if (this.checkGeoIp(url)) {
			return true;
		}
		return false;
	}

	public boolean isInScopeUkRegistration(String url, Target target) throws WhoisException {
		return checkWhois(url, target);
	}

	//	UK GeoIP
	public boolean isUkHosting(Target target) {
		for (FieldUrl fieldUrl : target.fieldUrls) {
			if (!this.checkGeoIp(fieldUrl.url)) return false;
		}
		return true;
	}
	
	//	UK Domain 
	public static boolean isTopLevelDomain(Target target) {
		for (FieldUrl fieldUrl : target.fieldUrls) {
			if( !checkScopeDomain(fieldUrl.url)) return false;
	    }
        return true;
	}
	
	
	public boolean isUkRegistration(Target target) {
        for (FieldUrl fieldUrl : target.fieldUrls) {
        	if (!checkWhois(fieldUrl.url, target)) return false;
        }
		return true;
	}

	/**
	 * 
	 * @param number
	 * @return
	 * @throws WhoisException
	 */
	public WhoIsData checkWhois(int number) throws WhoisException {
    	Logger.debug("checkWhoisThread: " + number);
		boolean res = false;
		List<Target> targets = new ArrayList<Target>();
		int ukRegistrantCount = 0;
		int nonUKRegistrantCount = 0;
		int failedCount = 0;
    	JRubyWhois whoIs = new JRubyWhois();
    	List<Target> targetList = Target.findLastActive(number);
    	Logger.debug("targetList: " + targetList.size());
    	Iterator<Target> itr = targetList.iterator();
    	while (itr.hasNext()) {
    		Target target = itr.next();
    		for (FieldUrl fieldUrl : target.fieldUrls) {
	        	try {
	//	        	Logger.debug("checkWhoisThread URL: " + target.field_url + ", last update: " + String.valueOf(target.lastUpdate));
		        	WhoisResult whoIsRes = whoIs.lookup(getDomainFromUrl(fieldUrl.url));
	//	        	Logger.debug("whoIsRes: " + whoIsRes);
		        	// DOMAIN A UK REGISTRANT?
		        	res = isUKRegistrant(whoIsRes);
		        	if (res) ukRegistrantCount++;
		        	else nonUKRegistrantCount++;
	//	        	Logger.debug("isUKRegistrant?: " + res);
		        	// STORE
		        	Logger.debug("CHECK TO SAVE " + target.fieldUrl());
		        	ScopeLookupEntries.storeInProjectDb(fieldUrl.url, "WHOIS", res, target);
		        	// ASSIGN TO TARGET
		        	target.isUkRegistration = res;
		        	ukRegistrantCount++;
		    	} catch (Exception e) {
		    		Logger.debug("whois lookup message: " + e.getMessage());
			        // store in project DB
		    		// FAILED - UNCHECKED
		    		ScopeLookupEntries.storeInProjectDb(fieldUrl.url, "WHOIS", false, target);
			        // FALSE - WHAT'S DIFF BETWEEN THAT AND NON UK? create a transient field?
			        target.isUkRegistration = false;
		        	failedCount++;
		    	}
    		}
        	Ebean.update(target);
        	targets.add(target);
    	}
    	
    	
//        List<Target> result = Target.find.select("title").where().eq(Const.ACTIVE, true).orderBy(Const.LAST_UPDATE + " " + Const.DESC).setMaxRows(number).findList();

//    	LookupEntry.find.fetch("target").where().select("name").select("target.title")
//    	
//
		StringBuilder lookupSql = new StringBuilder("select l.name as lookup_name, t.title as title, t.updated_at as target_date, l.updated_at as lookup_date, (l.updated_at::timestamp - t.updated_at::timestamp) as diff from Lookup_entry l, Target t "); 
		lookupSql.append(" where l.name in (select f.url from field_url as f, target tar where tar.active = true and tar.id = f.target_id order by tar.updated_at desc ");
		lookupSql.append(" limit ").append(number).append(") and l.target_id = t.id order by diff desc");

		List<SqlRow> results = Ebean.createSqlQuery(lookupSql.toString()).findList();
    	
		
//		for (SqlRow row : results) {
//			Logger.debug("row: " + row.getString("name") + " - " + row.get("diff"));
//		}
//    	List<LookupEntry> lookupEntries = LookupEntry.find.where().in("name", result).findList();
//    	StringBuilder builder = new StringBuilder("name in (select tar.field_url from target tar where tar.active = true order by tar.last_update desc)");
//    	List<LookupEntry> lookupEntries = LookupEntry.find.where().raw(builder.toString()).findList();


//    	Logger.debug("lookupEntries: " + lookupEntries.size());
    	WhoIsData whoIsData = new WhoIsData(targets, results, ukRegistrantCount, nonUKRegistrantCount, failedCount);

//    	Logger.debug("whois res: " + res);        	
		return whoIsData;
	}
	
	/**
	 * This method extracts domain name from the given URL and checks country or country code
	 * in response using whois lookup service.
	 * @param number The number of targets for which the elapsed time since the last check is greatest
	 * @return true if in UK domain
	 * @throws ActException 
	 * @throws WhoisException 
	 */
	public boolean checkWhoisThread(int number) throws ActException {
    	Logger.debug("checkWhoisThread: " + number);
		boolean res = false;
    	JRubyWhois whoIs = new JRubyWhois();
    	List<Target> targetList = Target.findLastActive(number);
    	Logger.debug("targetList: " + targetList.size());
    	Iterator<Target> itr = targetList.iterator();
    	while (itr.hasNext()) {
    		Target target = itr.next();
    		for (FieldUrl fieldUrl : target.fieldUrls) {
		        	Logger.debug("checkWhoisThread URL: " + target.fieldUrl() + ", last update: " + String.valueOf(target.updatedAt));
		        	WhoisResult whoIsRes = whoIs.lookup(getDomainFromUrl(fieldUrl.url));
		        	Logger.debug("whoIsRes: " + whoIsRes);
		        	// DOMAIN A UK REGISTRANT?
		        	res = isUKRegistrant(whoIsRes);
		        	Logger.debug("isUKRegistrant?: " + res);
		        	// STORE
		        	ScopeLookupEntries.storeInProjectDb(fieldUrl.url, "WHOIS", res, target);
		        	// ASSIGN TO TARGET
		        	target.isUkRegistration = res;

//		        	Logger.debug("whois lookup message: " + e.getMessage());
//			        // store in project DB
//		    		// FAILED - UNCHECKED
//			        storeInProjectDb(fieldUrl.url, false);
//			        // FALSE - WHAT'S DIFF BETWEEN THAT AND NON UK? create a transient field?
//			        target.isInScopeUkRegistration = false;
		    
    		}
        	Ebean.update(target);
    	}
//    	Logger.debug("whois res: " + res);        	
		return res;
	}
	
}

