package uk.bl.scope;

import play.Logger;

/**
 * This class implements scope rule engine.
 * A Target is in scope if any of the following statements is true:
 *
 *   Legal Deposit
 *   =============
 *      1. All URLs for this Target meet at least one of the following automated criteria:
 *          1.a The authority of the URI (i.e. the hostname) end with '.uk'.
 *          1.b The IP address associated with the URI is geo-located in the UK (using this GeoIP2 database, in a manner similar to our H3 GeoIP module).
 *      2. The Target is known to be hosted in the UK (manual boolean field).
 *      3. The Target features an page that specified a UK postal address (a manual boolean field plus a text field to hold a specific URL that contains the address).
 *      4. The Target is known to be a UK publication, according to correspondence with a curator (a manual boolean field plus a text field to hold details of the correspondence).
 *      5. The Target is known to be a UK publication, in the professional judgement of a curator (a manual boolean field plus a text field to hold the justification).
 *  By permission
 *  =============
 *      The Target is one for which we have a license that gives us permission to crawl the site (and make it available), even if the Target does not fall under any Legal Deposit criteria.
 *
 */
public class Scope {

	public static final String UK_DOMAIN          = ".uk";
	
	/**
	 * This method checks if a given URL is in scope.
	 * @return true if in scope
	 */
	public static boolean check(String url) {
        boolean res = false;
        Logger.info("check url: " + url);
        if (url.contains(UK_DOMAIN)) {
        	return true;
        }
        // TODO read configuration files with manual entries and match to the given URL
        return res;
	}
	
}

