/**
 * 
 */
package uk.bl.scope;

import java.util.List;

import com.avaje.ebean.Ebean;

import models.LookupEntry;
import models.Target;
import play.Logger;
import uk.bl.Const;
import uk.bl.exception.WhoisException;

/**
 * @author andy
 *
 */
public class ScopeLookupEntries {

	/**
	 * This method comprises rule engine for checking if a given URL is in scope for particular mode
	 * e.g. ALL, IP or DOMAIN.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param mode The mode of checking
	 * @return true if in scope
	 * @throws WhoisException
	 */
	public static boolean isLookupExistsInDb(String url) {
        boolean res = false;
        url = Scope.normalizeUrlNoSlash(url);        
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
	 * This method saves result of scope lookup for given URL if it is 
	 * not yet in a project database.
	 * @param url The search URL
	 * @param res The evaluated result after checking by expert rules
	 */
	public static void storeInProjectDb(String url, String type, boolean res, Target target) {
		boolean stored = isLookupExistsInDb(url);
		Logger.debug("STORED: " + stored + " - " + url);
		if (!stored) {
			LookupEntry lookupEntry = new LookupEntry();
			lookupEntry.name = url;
			lookupEntry.scopevalue = res;
			lookupEntry.target = target;
			lookupEntry.ttype = type;
	        lookupEntry.save();
	        Logger.debug("Saved lookup entry " + lookupEntry.toString());
		}
    }
	
	/**
	 * This method updates the lookup entry for given URL with
	 * the new QA status value.
	 * @param target The target object
	 * @param newStatus The QA status value
	 */
	private static void updateLookupEntry(Target target, boolean newStatus) {
        boolean res = false;
        Logger.debug("updateLookupEntry() field URL: " + target.fieldUrl() + ", new QA status: " + newStatus);
        String url = Scope.normalizeUrl(target.fieldUrl());
        
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
	        storeInProjectDb(url, "COMBINED", newStatus, target);
	    }
	}
	
}
