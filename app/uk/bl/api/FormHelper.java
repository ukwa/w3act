package uk.bl.api;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.Logger;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import models.CrawlPermission;
import models.FieldUrl;
import models.Target;

public enum FormHelper {

	INSTANCE;

	public boolean isInScopeAllWithoutLicense(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.isInScopeAllOrInheritedWithoutLicense();
	}
	
	public boolean hasGrantedLicense(Long targetId) {
		Logger.debug("hasGrantedLicense");
//		if QAStatus is granted 
//		this.crawlPermissions;
//		this.qaIssue;
		Target target = Target.findById(targetId);
		if( target == null ) return false;

		return target.indicateLicenses();
	}

    public boolean indicateNpldStatus(Long targetId) throws ActException {
    	Target target = Target.findById(targetId);
		if( target == null ) return false;
		
    	return target.indicateNpldStatus();
    }
    
	// to helper
	public Set<Target> getUkwaLicenceStatusList(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return null;

		return target.getUkwaLicenceStatusList();
	}
	
	/** 
	 * determine wether this target has a license request underway, either directly or inherited from another target.
	 * 
	 * @param targetId
	 * @return
	 */
	public boolean licensingUnderway(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		OverallLicenseStatus ols = target.getOverallLicenseStatus();
		if( ols.pendingIncludingInherited ) return true;
		return false;
	}

	/**
	 * 
	 * @param targetId
	 * @return
	 */
	public boolean inheritedLicenceUnderwayOrGranted(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		OverallLicenseStatus ols = target.getOverallLicenseStatus();
		if( ols.inheritedLicensePending || ols.inheritedLicense ) return true;
		return false;
	}

	/**
	 * Indicate whether this target has a license or permission request, either direct or inherited.
	 * @param targetId
	 * @return
	 */
	public boolean indicateUkwaLicenceStatus(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.indicateLicenses();
	}

	/*
	// FIXME This is insanely wrong.
	public static Set<Target> getLowerTargets(FieldUrl fieldUrl) {
		String query = "find target fetch fieldUrls where active = :active and fieldUrls.domain = :domain and LENGTH(fieldUrls.url) > :length";
		
		Set<Target> higherTargets = Ebean.createQuery(Target.class, query)
        		.setParameter("active", true)
        		.setParameter("domain", fieldUrl.domain)
        		.setParameter("length", fieldUrl.url.length())
        		.findSet();
		
		return higherTargets;
	}

	// FIXME This is insanely wrong.
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
	
	// FIXME This is insanely wrong.
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
	
	// FIXME This is insanely wrong.
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
	*/

	// to helper
	public boolean indicateLicenses(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.indicateLicenses();
	}
	
	public CrawlPermission getLatestCrawlPermission(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return null;
		return target.getLatestCrawlPermission();
	}
	
	public boolean hasLicenseAndCrawlPermission(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.indicateLicenses() || (target.crawlPermissions != null && target.crawlPermissions.size() > 0);
	}
	
	public boolean enableLicenseCreation(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.enableLicenseCreation();
	}
	
	public boolean hasInvalidLicenses(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.hasInvalidLicenses();
	}
	
	@JsonIgnore
	public boolean isUkHosting(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.isUkHosting;
	}
	
	@JsonIgnore
	public boolean isTopLevelDomain(Long targetId) throws WhoisException, MalformedURLException, URISyntaxException {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.isTopLevelDomain;
	}
	
	public boolean isUkRegistration(Long targetId) throws WhoisException {
		Target target = Target.findById(targetId);
		if( target == null ) return false;
		return target.isUkRegistration;
	}

	public Set<Target> getNpldStatusList(Long targetId) throws ActException {
		Target target = Target.findById(targetId);
		if( target == null ) return null;
		return target.getNpldStatusList();
	}
	
	// to helper
	public List<FieldUrl> fieldUrls(Long targetId) {
		Target target = Target.findById(targetId);
		if( target == null ) return null;
		return target.fieldUrls;
	}


	public String getCreatedAtDate(Long targetId) throws WhoisException {
		Target target = Target.findById(targetId);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if( target == null ) return null;
		return dateFormat.format(target.createdAt);
	}

	public String getUpdatedAtDate(Long targetId) throws WhoisException {
		Target target = Target.findById(targetId);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		if( target == null ) return null;
		return dateFormat.format(target.updatedAt);
	}

}
