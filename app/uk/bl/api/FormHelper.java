package uk.bl.api;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Ebean;

import play.Logger;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import models.FieldUrl;
import models.License;
import models.Target;

public enum FormHelper {

	INSTANCE;

	public boolean isInScopeAllWithoutLicense(Long targetId) throws MalformedURLException, URISyntaxException {
		Target target = Target.findById(targetId);
		boolean isInScope = false;
		try {
			Logger.info("isInScopeAllWithoutLicense()");
			isInScope = Target.checkScopeIpWithoutLicense(target);
			if (!isInScope) {
				isInScope = Scope.INSTANCE.isTopLevelDomain(target);
			}
		} catch (WhoisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isInScope;
	}
	
	public boolean hasGrantedLicense(Long targetId) {
		Logger.info("hasGrantedLicense");
//		if QAStatus is granted 
//		this.crawlPermissions;
//		this.qaIssue;
		Target target = Target.findById(targetId);

		for (License license : target.licenses) {
			if (license.equals(License.LicenseStatus.GRANTED)) {
				Logger.info(License.LicenseStatus.GRANTED.getValue());
				return true;
			}
		}
//		if (this.qaIssue != null && this.qaIssue.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
//			return true;
//		}
		return false;
	}

    public boolean indicateNpldStatus(Long targetId) {
    	Target target = Target.findById(targetId);
    	return (target.getNpldStatusList().size() > 0);
    }
    
	public boolean indicateUkwaLicenceStatus(Long targetId) {
		// include what RGRAF implemented
		Target target = Target.findById(targetId);
		return target.getUkwaLicenceStatusList().size() > 0;
	}
	
	// to helper
	public List<Target> getUkwaLicenceStatusList(Long targetId) {
		
		Target target = Target.findById(targetId);

		// Open UKWA Licence at higher level - disabled
		// Open UKWA licence for target being edited - disabled
		List<Target> results = new ArrayList<Target>();
		// first aggregate a list of active targets for associated URL
			// TODO: KL REDO THIS
//			this.fieldUrl = Scope.normalizeUrl(this.fieldUrl());
		for (FieldUrl thisFieldUrl : target.fieldUrls) {
			Logger.debug("getUkwaLicenceStatusList() domain: " + thisFieldUrl.domain);

			// all the target/urls that are shorter than me with a license and qa

			// Then for each target from selected list look if ‘qa_status’
			// field is not empty. If it is not empty then we know a crawl
			// permission request has already been sent.
			// also check if this target has a valid license too
			// Then look if it is a target of a higher level domain
			// analyzing given URL.
			
			// license field checked as required in issue 176.
			// higher level domain and has a license or higher level domain
			// and has pending qa status

			String query = "find target fetch fieldUrls fetch licenses where active = true and fieldUrls.domain = :domain and LENGTH(fieldUrls.url) < :length";
			
			List<Target> higherTargets = Ebean.createQuery(Target.class, query)
	        		.setParameter("domain", thisFieldUrl.domain)
	        		.setParameter("length", thisFieldUrl.url.length())
//	        		.where().or(Expr.isNotNull("licenses"), Expr.isNotNull("qaIssue"))
	        		.findList();
			
			Logger.debug("higherTargets: " + higherTargets.size());
			
			// for debug
//			for (Target higherTarget : higherTargets) {
//				Logger.debug("higherTarget: " + higherTarget.fieldUrl());
//			}
			results.addAll(higherTargets);
		}

		return results;
	}
	
	// to helper
	public boolean indicateLicenses(Long targetId) {
		Target target = Target.findById(targetId);
		return (target.hasLicenses() || target.hasHigherLicense() || target.indicateUkwaLicenceStatus());
	}

	public boolean isUkRegistration(Long targetId) throws WhoisException {
		Target target = Target.findById(targetId);
		return Scope.INSTANCE.isUkRegistration(target);
	}

	public List<Target> getNpldStatusList(Long targetId) {
		Target target = Target.findById(targetId);
		return null;
	}
	
	// to helper
	public List<FieldUrl> fieldUrls(Long targetId) {
		Target target = Target.findById(targetId);
		return target.fieldUrls;
	}
	
}
