package uk.bl.scope;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import models.FieldUrl;
import models.LookupEntry;
import models.Target;
import play.Logger;
import uk.bl.Const;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.WhoisResult;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

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
 *      3.1 The authority of the URI (i.e. the hostname) end with '.uk'.
 *      3.2 The IP address associated with the URI is geo-located in the UK 
 *          (using this GeoIP2 database, in a manner similar to our H3 GeoIP module).
 *      3.3 Use whois lookup service to check whether the given domain name is associated with the UK. 
 *          
 */
public enum Scope {

	INSTANCE;
	
	public static final String UK_DOMAIN       = ".uk";
	public static final String LONDON_DOMAIN   = ".london";
	public static final String SCOT_DOMAIN     = ".scot";
	public static final String WALES_DOMAIN     = ".wales";
	public static final String CYMRU_DOMAIN     = ".cymru";
	
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

	/**
	 * This method queries geo IP from database
	 * @param ip - The host IP
	 * @return true if in UK domain
	 */
	public boolean queryDb(String ip) {
		boolean res = false;
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File(GEO_IP_SERVICE);
	
		try {
			// This creates the DatabaseReader object, which should be reused across
			// lookups.
			DatabaseReader reader = new DatabaseReader.Builder(database).build();
		
			// Find city by given IP
			CityResponse response = reader.city(InetAddress.getByName(ip));
			Logger.debug(response.getCountry().getIsoCode()); 
			Logger.debug(response.getCountry().getName()); 
			// Check country code in city response
			if (response.getCountry().getIsoCode().equals(UK_COUNTRY_CODE)) {
				res = true;
			}
		} catch (Exception e) {
			Logger.warn("GeoIP error. " + e);
		}
		Logger.debug("Geo IP query result: " + res);
		return res;
	}
	
	/**
	 * This method normalizes passed URL that it is appropriate for IP calculation.
	 * @param url The passed URL
	 * @return normalized URL
	 */
	public String normalizeUrl(String url, boolean slash) {
		String res = url;
		if (res != null && res.length() > 0) {
	        if (!res.contains(WWW) && !res.contains(HTTP) && !res.contains(HTTPS)) {
	        	res = WWW + res;
	        }
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

	public String normalizeUrl(String url) {
		return normalizeUrl(url, true);
	}

	public String normalizeUrlNoSlash(String url) {
		return normalizeUrl(url, false);
	}

	/**
	 * This method comprises rule engine for checking if a given URL is in scope.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param mode The mode of checking
	 * @return true if in scope
	 * @throws WhoisException 
	 */
	public boolean check(String url, Target target) throws WhoisException {
	    return checkExt(url, target, Const.ScopeCheckType.ALL.name());
	}
	
	/**
	 * This method comprises rule engine for checking if a given URL is in scope for particular mode
	 * e.g. ALL, IP or DOMAIN.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param mode The mode of checking
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean isLookupExistsInDb(String url) {
        boolean res = false;
        url = normalizeUrlNoSlash(url);        
        if (url != null && url.length() > 0) {
        	Logger.debug("normalizeUrl: " + url);
        	List<LookupEntry> lookupEntryCount = LookupEntry.filterByName(url);
        	if (lookupEntryCount.size() > 0) {
        		res = true;
        	}
        }
        Logger.debug("isLookupExistsInDb() url: " + url + ", res: " + res);
        return res;
	}
	
	/**
	 * This method updates the lookup entry for given URL with
	 * the new QA status value.
	 * @param target The target object
	 * @param newStatus The QA status value
	 */
	public void updateLookupEntry(Target target, boolean newStatus) {
        boolean res = false;
        Logger.debug("updateLookupEntry() field URL: " + target.fieldUrl() + ", new QA status: " + newStatus);
        String url = normalizeUrl(target.fieldUrl());
        
        /**
         * Check for fields of target that not yet stored in database.
         */
        if (target != null
        		&& (target.ukPostalAddress 
        		|| target.viaCorrespondence
        		|| target.professionalJudgement)) {
        	Logger.debug("updateLookupEntry(): " + target.ukPostalAddress + ", " + 
        		target.viaCorrespondence + ", " + target.professionalJudgement);
        	res = true;
        }
        if (target != null && target.noLdCriteriaMet) {
        	res = false;
        }
        
//        if (target != null 
//        		&& target.fieldLicense != null 
//        		&& target.fieldLicense.length() > 0 
//        		&& !target.fieldLicense.toLowerCase().contains(Const.NONE)) {
//        	res = true;
//        }

        Logger.debug("updateLookupEntry() new scope: " + newStatus + ", fields check: " + res);
        if (!newStatus && res) {
        	newStatus = true;
            Logger.debug("updateLookupEntry() update new scope: " + newStatus);
        }
        
        /**
         * Check if given URL is already in project database in a table LookupEntry. 
         * If this is in and its value differs from new value - update value.
         */
        boolean inProjectDb = false;
        if (url != null && url.length() > 0) {
        	LookupEntry resLookupEntry = LookupEntry.findBySiteName(url);   
        	if (resLookupEntry != null && !resLookupEntry.name.toLowerCase().equals(Const.NONE)) {
        		inProjectDb = true;
        		res = LookupEntry.getValueByUrl(url);
        		Logger.debug("updateLookupEntry lookup entry for '" + url + "' is in database with value: " + res);
        		if (newStatus != res) {
        			resLookupEntry.scopevalue = newStatus;
            		Logger.debug("updateLookupEntry lookup entry for '" + url + "' changed to value: " + newStatus);
        			Ebean.update(resLookupEntry);
        		}
        	}
        }

        if (!inProjectDb) {
	        storeInProjectDb(url, newStatus, target);
	    }
	}
	
	/**
	 * This method comprises rule engine for checking if a given URL is in scope for particular mode
	 * e.g. ALL, IP or DOMAIN.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param mode The mode of checking
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean checkExt(String url, Target target, String mode) throws WhoisException {
        boolean res = false;
        Logger.debug("check url: " + url + ", nid: " + target.id);
        url = normalizeUrl(url);
        
        /**
         * Check if given URL is already in project database in a table LookupEntry. 
         * If this is in return associated value, otherwise process lookup using expert rules.
         */
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
        
        if (!inProjectDb) {
        	Logger.debug("URL not in database - calculate scope");
	        /**
	         *  Rule 1: check manual scope settings because they have more severity. If one of the fields:
	         *
	         *  Rule 1.1: "field_uk_domain"
	         *  Rule 1.2: "field_uk_postal_address"
	         *  Rule 1.3: "field_via_correspondence"
	         *  Rule 1.4: "field_professional_judgement"
	         *  
	         *  is true - checking result is positive.
	         *  
	         *  Rule 1.5: if the field "field_no_ld_criteria_met" is true - checking result is negative
	         * 
	         */
	        // read Target fields with manual entries and match to the given NID URL (Rules 1.1 - 1.5)
	        if (target != null 
	        		&& (mode.equals(Const.ScopeCheckType.ALL.name())
	        		|| mode.equals(Const.ScopeCheckType.IP.name()))) {
	        	if (!res) {
	        		res = target.checkManualScope();
	        	}
	        }
	
	    	// Rule 2: by permission
	        if (!res && target != null
	        		&& (mode.equals(Const.ScopeCheckType.ALL.name())
	    	        		|| mode.equals(Const.ScopeCheckType.IP.name()))) {
	        	res = target.checkLicense();
	        }
	
	        // Rule 3.1: check domain name
	        if (!res && url != null && url.length() > 0
	        		&& (mode.equals(Const.ScopeCheckType.ALL.name())
	    	        		|| mode.equals(Const.ScopeCheckType.DOMAIN.name()))) {
		        if (url.contains(UK_DOMAIN) || url.contains(LONDON_DOMAIN) || url.contains(SCOT_DOMAIN) || url.contains(WALES_DOMAIN) || url.contains(CYMRU_DOMAIN)) {
		        	res = true;
		        }
	        }
	        
	        // Rule 3.2: check geo IP
	        if (!res && url != null && url.length() > 0
	        		&& (mode.equals(Const.ScopeCheckType.ALL.name())
	    	        		|| mode.equals(Const.ScopeCheckType.IP.name()))) {
	        	res = checkGeoIp(url);
	        }
	        
	        // Rule 3.3: check whois lookup service
	        if (!res && url != null && url.length() > 0
	        		&& (mode.equals(Const.ScopeCheckType.ALL.name())
	    	        		|| mode.equals(Const.ScopeCheckType.IP.name()))) {
	        	res = checkWhois(url, target);
	        }
	        // store in project DB
	        storeInProjectDb(url, res, target);
        }
		Logger.debug("lookup entry for '" + url + "' is in database with value: " + res);        
        return res;
	}
	
	/**
	 * This method comprises rule engine for checking if a given URL is in scope for rules 
	 * associated with IP analysis. 
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean checkScopeIp(String url, Target target) throws WhoisException {
        boolean res = false;
        Logger.debug("check for scope IP url: " + url + ", nid: " + target.id);
        url = normalizeUrl(url);
        
        /**
         *  Rule 1: check manual scope settings because they have more severity. If one of the fields:
         *
         *  Rule 1.1: "field_uk_domain"
         *  Rule 1.2: "field_uk_postal_address"
         *  Rule 1.3: "field_via_correspondence"
         *  Rule 1.4: "field_professional_judgement"
         *  
         *  is true - checking result is positive.
         *  
         *  Rule 1.5: if the field "field_no_ld_criteria_met" is true - checking result is negative
         * 
         */
        // read Target fields with manual entries and match to the given NID URL (Rules 1.1 - 1.5)
    	if (!res) {
    		res = target.checkManualScope();
    		Logger.debug("checkScopeIp() after manual check (fields: field_uk_postal_address, field_via_correspondence and field_professional_judgement): " + res);
    	}

    	// Rule 2: by permission
        if (!res && target != null) {
        	res = target.checkLicense();
    		Logger.debug("checkScopeIp() after license check (field: field_license): " + res);
        }

        // Rule 3.2: check geo IP
        if (!res && url != null && url.length() > 0) {
        	res = checkGeoIp(url);
    		Logger.debug("checkScopeIp() after geoIp check: " + res);
        }
        
        // Rule 3.3: check whois lookup service
        if (!res && url != null && url.length() > 0) {
        	res = checkWhois(url, target);
    		Logger.debug("checkScopeIp() after whois check: " + res);
        }
        
        /**
         * if database entry exists and is different to the current value - replace it
         */
        if (url != null && url.length() > 0) {
        	List<LookupEntry> lookupEntries = LookupEntry.filterByName(url);
        	if (lookupEntries.size() > 0) {
        		boolean dbValue = LookupEntry.getValueByUrl(url);
        		if (dbValue != res) {
       		        LookupEntry lookupEntry = lookupEntries.get(0);
       		        lookupEntry.scopevalue = res;
       		        Ebean.update(lookupEntry);
            		Logger.debug("updated lookup entry in database for '" + url + "' with value: " + res);
        		}
        	} else {
        		storeInProjectDb(url, res, target);
        	}
        }
        
		Logger.debug("resulting lookup entry for '" + url + "' is: " + res);        
        return res;
	}
	
	/**
	 * This method comprises rule engine for checking if a given URL is in scope for rules 
	 * associated with IP analysis. This check is without license field.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean checkScopeIpWithoutLicense(Target target) throws WhoisException {
        boolean res = false;
//        Logger.debug("check for scope IP url: " + url + ", id: " + target.id);
//        url = normalizeUrl(url);
//        Logger.debug("normalizeUrl: " + url);
        
        /**
         *  Rule 1: check manual scope settings because they have more severity. If one of the fields:
         *
         *  Rule 1.1: "field_uk_domain"
         *  Rule 1.2: "field_uk_postal_address"
         *  Rule 1.3: "field_via_correspondence"
         *  Rule 1.4: "field_professional_judgement"
         *  
         *  is true - checking result is positive.
         *  
         *  Rule 1.5: if the field "field_no_ld_criteria_met" is true - checking result is negative
         * 
         */
        // read Target fields with manual entries and match to the given NID URL (Rules 1.1 - 1.5)
        if (target != null) {
        	if (!res) {
        		// checking target fields
        		res = target.checkManualScope();
        		Logger.debug("checkScopeIp() after manual check ("
        				+ "fields: field_uk_postal_address, "
        				+ "field_via_correspondence and "
        				+ "field_professional_judgement): " + res);
        	}
        }

        // Rule 3.2: check geo IP / uk hosting?
//        if (!res && StringUtils.isNotEmpty(url)) {
        if (!res) {
        	// check target.isUkHosting field with SQL
        	res = target.isUkHosting();
        	// the above calls the same in the end
//        	res = checkGeoIp(url);
    		Logger.debug("checkScopeIp() after geoIp check: " + res);
        }
        
        // Rule 3.3: check whois lookup service / uk registration?
//        if (!res && StringUtils.isNotEmpty(url)) {
        if (!res) {
        	// check target.isUkRegistration field with SQL
        	res = target.isUkRegistration();
        	// the above calls the same in the end
//        	res = checkWhois(url, target);
    		Logger.debug("checkScopeIp() after whois check: " + res);
        }
        
        /**
         * if database entry exists and is different to the current value - replace it
         */
        for (FieldUrl fieldUrl : target.fieldUrls) {
        	List<LookupEntry> lookupEntries = LookupEntry.filterByName(fieldUrl.url);
        	if (lookupEntries.size() > 0) {
        		boolean dbValue = LookupEntry.getValueByUrl(fieldUrl.url);
        		if (dbValue != res) {
       		        LookupEntry lookupEntry = lookupEntries.get(0);
       		        lookupEntry.scopevalue = res;
       		        Ebean.update(lookupEntry);
            		Logger.debug("updated lookup entry in database for '" + fieldUrl.url + "' with value: " + res);
        		}
        	} else {
        		storeInProjectDb(fieldUrl.url, res, target);
        	}
        	
        }
        
//        if (StringUtils.isNotEmpty(url)) {
//        	List<LookupEntry> lookupEntries = LookupEntry.filterByName(url);
//        	if (lookupEntries.size() > 0) {
//        		boolean dbValue = LookupEntry.getValueByUrl(url);
//        		if (dbValue != res) {
//       		        LookupEntry lookupEntry = lookupEntries.get(0);
//       		        lookupEntry.scopevalue = res;
//       		        Ebean.update(lookupEntry);
//            		Logger.debug("updated lookup entry in database for '" + url + "' with value: " + res);
//        		}
//        	} else {
//        		storeInProjectDb(url, res, target);
//        	}
//        }
//        
//		Logger.debug("resulting lookup entry for '" + url + "' is: " + res);
        return res;
	}
	
	/**
	 * This method comprises rule engine for checking if a given URL is in scope for rules 
	 * associated with Domain analysis.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public boolean checkScopeDomain(String url) throws WhoisException {
        boolean res = false;
//        Logger.debug("check for scope Domain url: " + url + ", nid: " + nidUrl);
        // full domain with protocol
        url = normalizeUrl(url);
        
        // Rule 3.1: check domain name
        if (!res && url != null && url.length() > 0) {
	        if (url.contains(UK_DOMAIN) || url.contains(LONDON_DOMAIN) || url.contains(SCOT_DOMAIN) || url.contains(WALES_DOMAIN) || url.contains(CYMRU_DOMAIN)) {
	        	res = true;
	        }
        }
//		Logger.debug("lookup entry for '" + url + "' regarding domain has value: " + res);        
        return res;
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
	 * This method extracts domain name from the given URL and checks country or country code
	 * in response using whois lookup service.
	 * @param url
	 * @return true if in UK domain
	 * @throws WhoisException 
	 */
	public boolean checkWhois(String url, Target target) throws WhoisException {
		boolean res = false;
    	try {
        	JRubyWhois whoIs = new JRubyWhois();
        	Logger.debug("checkWhois: " + whoIs + " " + url);
        	WhoisResult whoIsRes = whoIs.lookup(getDomainFromUrl(url));
        	Logger.debug("whoIsRes: " + whoIsRes);
//        	WhoisResult whoIsRes = whoIs.lookup(getDomainFromUrl("marksandspencer.com"));
        	res = whoIsRes.isUKRegistrant();
        	Logger.debug("isUKRegistrant?: " + res);
    	} catch (Exception e) {
    		Logger.debug("whois lookup message: " + e.getMessage());
	        // store in project DB
	        storeInProjectDb(url, false, target);
//    		throw new WhoisException(e);
    	}
    	Logger.debug("whois res: " + res);        	
		return res;
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
		        	res = whoIsRes.isUKRegistrant();
		        	Logger.debug("isUKRegistrant?: " + res);
		        	// STORE
		        	storeInProjectDb(fieldUrl.url, res, target);
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
		        	res = whoIsRes.isUKRegistrant();
		        	if (res) ukRegistrantCount++;
		        	else nonUKRegistrantCount++;
	//	        	Logger.debug("isUKRegistrant?: " + res);
		        	// STORE
		        	Logger.debug("CHECK TO SAVE " + target.fieldUrl());
		        	storeInProjectDb(fieldUrl.url, res, target);
		        	// ASSIGN TO TARGET
		        	target.isUkRegistration = res;
		        	ukRegistrantCount++;
		    	} catch (Exception e) {
		    		Logger.debug("whois lookup message: " + e.getMessage());
			        // store in project DB
		    		// FAILED - UNCHECKED
			        storeInProjectDb(fieldUrl.url, false, target);
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
	 * This method saves result of scope lookup for given URL if it is 
	 * not yet in a project database.
	 * @param url The search URL
	 * @param res The evaluated result after checking by expert rules
	 */
	public void storeInProjectDb(String url, boolean res, Target target) {
		boolean stored = isLookupExistsInDb(url);
		Logger.debug("STORED: " + stored + " - " + url);
		if (!stored) {
			LookupEntry lookupEntry = new LookupEntry();
			lookupEntry.name = url;
			lookupEntry.scopevalue = res;
			lookupEntry.target = target;
	        lookupEntry.save();
	        Logger.debug("Saved lookup entry " + lookupEntry.toString());
		}
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
	
//	/**
//	 * This method extracts domain name from the given URL.
//	 * @param url
//	 * @return
//	 */
//	public String getDomainFromUrl(String url) {
//		String domain = "";
//		try {
////			Logger.debug("get host: " + new URL(url).getHost());
//			domain = new URL(url).getHost().replace(WWW, "");
////			Logger.debug("extracted domain: " + domain);
//		} catch (Exception e) {
//			Logger.error("domain calculation error for url=" + url + ". " + e.getMessage());
//			domain = url;
//		}
//        return domain;
//	}
	
	public String getDomainFromUrl(String url) throws ActException {
	    URI uri;
		try {
			uri = new URI(url);
			String domain = uri.getHost();
			if (StringUtils.isNotEmpty(domain)) {
				return domain.startsWith("www.") ? domain.substring(4) : domain;
			}
		} catch (URISyntaxException e) {
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
	public boolean isTopLevelDomain(Target target) throws ActException {
		// i.e. terry.com and terry.co.uk - return false;
		try {

	        for (FieldUrl fieldUrl : target.fieldUrls) {
	            URL uri;
					uri = new URI(fieldUrl.url).normalize().toURL();
	
				String url = uri.toExternalForm();
	            Logger.debug("Normalised " + url);
	            
	            String domain = getDomainFromUrl(url);
	
	            Logger.debug("domain " + domain);
	
	            // Rule 3.1: check domain name
		        if (!domain.endsWith(UK_DOMAIN) && !domain.endsWith(LONDON_DOMAIN) && !domain.endsWith(SCOT_DOMAIN) && !domain.endsWith(WALES_DOMAIN) && !domain.endsWith(CYMRU_DOMAIN)) return false;
	        }
//		Logger.debug("lookup entry for '" + url + "' regarding domain has value: " + res);
		} catch (MalformedURLException | URISyntaxException e) {
			throw new ActException(e);
		}
        return true;
	}
	
	
	public boolean isUkRegistration(Target target) throws WhoisException {
        for (FieldUrl fieldUrl : target.fieldUrls) {
        	if (!checkWhois(fieldUrl.url, target)) return false;
        }
		return true;
	}

}

