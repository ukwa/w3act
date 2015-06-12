/**
 * 
 */
package uk.bl.api;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.net.InternetDomainName;

import play.Logger;
import uk.bl.Const.ScopeType;
import uk.bl.scope.Scope;
import models.FieldUrl;
import models.Target;

/**
 * Look for any kind of license at a higher level.
 * 
 * Loop over Targets that may cover each URL.
 * For each one, it checks if it might apply to the current target, and whether 
 * it's a NPLD or licensed Target.
 * This means checking that all URLs are covered by the 'parent'.
 * Every URL must be covered by at least on Target.
 * 
 * @author andy
 *
 */
public class OverallLicenseStatus {
	
	public final boolean inNPLDScope;
	
	public final boolean licensedOrPending;
	
	public final boolean inheritedLicense;
	
	public final boolean inheritedNPLDScope;
	
	public final Set<Target> NPLDParents = new LinkedHashSet<Target>();
	
	public final Set<Target> licenseParents = new LinkedHashSet<Target>();

	public OverallLicenseStatus(Target target) {
		int npldc = 0, lc = 0;
		for (FieldUrl fieldUrl : target.fieldUrls) {
			Logger.info("Looking for inherited licensed for "+fieldUrl.url);
			List<Target> tp = Target.findAllTargetsForDomainLike("%"+getParentDomain(fieldUrl.domain));
			if( tp == null ) {
				Logger.info("Found no potential matches.");
				continue;
			}
			Logger.info("Found " + tp.size() + " potential matches.");
			for( Target t :  tp ) {
				if( t.id.equals(target.id)) {
					Logger.info("Skipping "+t.title);
					continue;
				}
				Logger.info("Checking "+t.title);
				// Check if the scoping of the target applies here:
				for( FieldUrl pt : t.fieldUrls) {
					Logger.info("Checking "+pt.url);
					// If one of the 'parent's' URLs is a prefix of this one:
					if( inheritsFromTo( pt,t.scope, fieldUrl ) ) {
						// Check if this is in scope, but without forcing a full re-check (e.g. whois):
						boolean isInScope = t.isInScopeAllWithoutLicense();
						if( isInScope ) {
							Logger.info("Found licensed parent for "+fieldUrl.url+", inScope="+isInScope);
							NPLDParents.add(t);
							npldc++;
						}
						// Also check if a license process is underway:
						boolean midLic = ! t.enableLicenseCreation();
						if( midLic ) {
							Logger.info("Found licensed parent for "+fieldUrl.url+", mid-licensing="+midLic);
							licenseParents.add(t);
							lc++;
						}
					} else {
						Logger.info("Parent license does not apply to "+fieldUrl.url);						
					}
				}
			}
		}
		// Record if either was sufficient:
		if(lc == target.fieldUrls.size()) {
			this.inheritedLicense = true;
		} else {
			this.inheritedLicense = false;
		}
		if(npldc == target.fieldUrls.size()) { 
			this.inheritedNPLDScope = true;
		} else {
			this.inheritedNPLDScope = false;
		}
		
		// And finally record the direct status:
		this.inNPLDScope = target.isInScopeAllWithoutLicense();
		this.licensedOrPending = ! target.enableLicenseCreation();
	}
	
	/**
	 * Get the parent domain name, in order to find inheritence candidates:
	 * @param domain
	 * @return
	 */
	private static String getParentDomain(String domain) {
		domain = stripWWW(domain);
		InternetDomainName idn = InternetDomainName.from(domain);
		if( idn.isTopPrivateDomain() ) return idn.name();
		// Otherwise, strip a subdomain off:
		return idn.parent().name();
	}

	/** 
	 * Checks whether a child URL should inherit from a parent one, given the scope.
	 * 
	 * @param parent
	 * @param scope
	 * @param child
	 * @return
	 */
	private boolean inheritsFromTo(FieldUrl parent, String scope, FieldUrl child ) {
		if( ScopeType.root.name().equals(scope) ) {
			String nurl = stripWWW(child.url);
			String pnurl = stripWWW(parent.url);
			Logger.debug("Checking root scope for: "+nurl+" under "+pnurl);
			return nurl.startsWith(pnurl);
		} else if( ScopeType.subdomains.name().equals(scope)) {
			String pdom = parent.domain;
			pdom = pdom.replaceFirst("^www\\.", "");
			Logger.debug("Checking domain scope for: "+child.domain+" under "+pdom);
			return child.domain.endsWith(pdom);
		} else {
			Logger.error("UNSUPPORTED SCOPE "+scope);
		}
		return false;
	}
	
	private static String stripWWW( String url ) {
		String nurl = url.toLowerCase();
		if( nurl.startsWith("http://www.") || nurl.startsWith("https://www.")) {
			nurl = nurl.replaceFirst("www\\.", "");
		}
		return nurl;
	}
}

