package uk.bl.api;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.Logger;
import uk.bl.exception.ActException;
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
			Logger.debug("isInScopeAllWithoutLicense()");
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
		Logger.debug("hasGrantedLicense");
//		if QAStatus is granted 
//		this.crawlPermissions;
//		this.qaIssue;
		Target target = Target.findById(targetId);

		for (License license : target.licenses) {
			if (license.equals(License.LicenseStatus.GRANTED)) {
				Logger.debug(License.LicenseStatus.GRANTED.getValue());
				return true;
			}
		}
//		if (this.qaIssue != null && this.qaIssue.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
//			return true;
//		}
		return false;
	}

    public boolean indicateNpldStatus(Long targetId) throws ActException {
    	Target target = Target.findById(targetId);
    	return (target.getNpldStatusList().size() > 0);
    }
    
	public boolean indicateUkwaLicenceStatus(Long targetId) {
		// include what RGRAF implemented
		Target target = Target.findById(targetId);
		return target.getUkwaLicenceStatusList().size() > 0;
	}
	
	// to helper
	public Set<Target> getUkwaLicenceStatusList(Long targetId) {
		
		Target target = Target.findById(targetId);

		// Open UKWA Licence at higher level - disabled
		// Open UKWA licence for target being edited - disabled
		
		// url is similar but less in characters
		// has a license
		// has a qaIssue
		Set<Target> results = new LinkedHashSet<Target>();
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

			Set<Target> higherTargets = FormHelper.getHigherTargetsWithLicenseAndQaIssue(thisFieldUrl);
			
			Logger.debug("higherTargets: " + higherTargets.size());
			
			results.addAll(higherTargets);
		}

		return results;
	}

	public static Set<Target> getHigherTargetsWithLicenseAndQaIssue(FieldUrl fieldUrl) {
		String query = "find target fetch fieldUrls fetch licenses where active = :active and fieldUrls.domain = :domain and LENGTH(fieldUrls.url) < :length";
		
		Set<Target> higherTargets = Ebean.createQuery(Target.class, query)
        		.setParameter("active", true)
        		.setParameter("domain", fieldUrl.domain)
        		.setParameter("length", fieldUrl.url.length())
        		.where().or(Expr.isNotNull("licenses"), Expr.isNotNull("qaIssue"))
        		.findSet();
		
		return higherTargets;
	}
	
	public static Target getHigherLevelTargetLicense(FieldUrl fieldUrl) {
		String query = "find target fetch fieldUrls fetch licenses where active = :active and fieldUrls.domain = :domain and LENGTH(fieldUrls.url) < :length";
		
		Target higherTarget = Ebean.createQuery(Target.class, query)
        		.setParameter("active", true)
        		.setParameter("domain", fieldUrl.domain)
        		.setParameter("length", fieldUrl.url.length())
        		.where().isNotNull("licenses")     
        		.findUnique();
		
		return higherTarget;
	}
	
	public static Set<Target> getHigherTargetsForNpld(FieldUrl fieldUrl) {
		StringBuilder query = new StringBuilder("find target fetch fieldUrls where active = :active and fieldUrls.domain = :domain and LENGTH(fieldUrls.url) < :length and isUkHosting = :isUkHosting ");
			query.append("and (ukPostalAddress = :ukPostalAddress or viaCorrespondence = :viaCorrespondence or professionalJudgement = :professionalJudgement or noLdCriteriaMet = :noLdCriteriaMet)");

			Set<Target> higherTargets = Ebean.createQuery(Target.class, query.toString())
        		.setParameter("active", true)
        		.setParameter("domain", fieldUrl.domain)
        		.setParameter("length", fieldUrl.url.length())
        		.setParameter("isUkHosting", false)
        		.setParameter("ukPostalAddress", true)
        		.setParameter("viaCorrespondence", true)
        		.setParameter("professionalJudgement", true)
        		.setParameter("noLdCriteriaMet", true)
        		.findSet();
		
		return higherTargets;
	}
	
	// to helper
	public boolean indicateLicenses(Long targetId) {
		Target target = Target.findById(targetId);
		return (target.hasLicenses() || target.hasHigherLicense() || target.indicateUkwaLicenceStatus());
	}
	@JsonIgnore
	public boolean isUkHosting(Long targetId) {
		Target target = Target.findById(targetId);
		return target.isUkHosting();
	}
	
	@JsonIgnore
	public boolean isTopLevelDomain(Long targetId) throws WhoisException, MalformedURLException, URISyntaxException {
		Target target = Target.findById(targetId);
		return target.isTopLevelDomain();
	}
	
	public boolean isUkRegistration(Long targetId) throws WhoisException {
		Target target = Target.findById(targetId);
		return target.isUkRegistration();
	}

	public List<Target> getNpldStatusList(Long targetId) throws ActException {
		Target target = Target.findById(targetId);
		return target.getNpldStatusList();
	}
	
	// to helper
	public List<FieldUrl> fieldUrls(Long targetId) {
		Target target = Target.findById(targetId);
		return target.fieldUrls;
	}
	
}
