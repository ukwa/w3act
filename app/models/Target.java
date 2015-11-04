package models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import models.License.LicenseStatus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import scala.NotImplementedError;
import uk.bl.Const;
import uk.bl.api.OverallLicenseStatus;
import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Target entity managed by Ebean
 */
@Entity
@Table(name = "target")
public class Target extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8283372689443804260L;

	@Id
    public Long id;
	
	@JsonProperty
	@Column(unique=true)
	public String url;

    public Date createdAt;

    @Version
    public Timestamp updatedAt;
   
    @Override
    @Transactional
	public void save() {
    	// need to save to get the ID
    	super.save();
    	if (StringUtils.isEmpty(this.url)) {
    		this.url = Const.ACT_URL + this.id;
    	}
    	if (createdAt == null) {
    		this.createdAt = new Date();
    	}
    	super.save();
    }
    
	public String toCreatedAtString() {
		return Utils.INSTANCE.convertToDateString(createdAt);
	}
	
	public String toUpdatedAtString() {
		return Utils.INSTANCE.convertToDateTime(updatedAt);
	}
	
    @Constraints.Required(message="Title Required")
	public String title;
	
	public String language;
	
	public String secondLanguage;
	
	@Column(name="web_form_info")
	public String webFormInfo;
	
	@Column(name="web_form_date")
	public Date webFormDate;

	@Column(columnDefinition = "text")
	public String revision;

	@JsonProperty  
	public String edit_url;

	@JsonProperty
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "qaissue_id")
	public QaIssue qaIssue;

	@JsonProperty
	@Column(columnDefinition = "text")
	public String notes;
	
	@JsonProperty
	public String format;


	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}
	
	@JsonProperty("originating_organisation")
	@Column(columnDefinition = "text")
	public String originatingOrganisation;
	
	//@JsonProperty("crawl_permissions")
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
//    @OrderBy("createdAt DESC")
	public List<CrawlPermission> crawlPermissions;

	@JsonProperty
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<Instance> instances;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "license_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "license_id", referencedColumnName="id") }) 
	public List<License> licenses;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "subject_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "subject_id", referencedColumnName="id") }) 
	public List<Subject> subjects;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "collection_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") }) 
	public List<Collection> collections;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tag_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") }) 
	public List<Tag> tags;
	
    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "flag_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "flag_id", referencedColumnName="id") }) 
    public List<Flag> flags;
	
	@JsonIgnore
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<LookupEntry> lookupEntries;

	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "target_id")
	@OrderBy("position ASC")
	public List<FieldUrl> fieldUrls;
	
	@Column(name="target_start_date")
	protected Timestamp targetStartDate;
	
	@Column(name="target_end_date")
	protected Timestamp targetEndDate;


	@Column(columnDefinition = "text")
	public String description;
	
	@JsonIgnore
	public Boolean isInScopeIp;
	
	@JsonIgnore
	public Boolean isInScopeIpWithoutLicense;

	public Boolean active; // flag for the latest version of the target among
							// targets with the same URL
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "author_id")
	@Required(message="Author Required")
	public User authorUser;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "document_owner_id")
	public User documentOwner;

	@Column(columnDefinition = "text")
	public String flagNotes;
	/**
	 * This field comprises the current tab name for view and edit pages.
	 */
	
    /* This field is apparently the viaCorrespondenceExp field! */
	@Column(columnDefinition = "text")
	@JsonProperty
	public String value;
	
	@Column(columnDefinition = "text")
	public String summary;

	@JsonProperty("field_special_dispensation")
	public Boolean specialDispensation = Boolean.FALSE;

	@Column(columnDefinition = "text")
	@JsonProperty("field_special_dispensation_reaso")
	public String specialDispensationReason;
	
	@JsonProperty("field_uk_hosting")
	public Boolean isUkHosting;
	
	public Boolean isTopLevelDomain;
	public Boolean isUkRegistration;
	
	@JsonProperty("field_live_site_status")
	public String liveSiteStatus;

	@JsonProperty("field_hidden")
	public Boolean hidden;
	
	@JsonProperty("field_key_site")
	public Boolean keySite;

	@JsonProperty("field_wct_id")
	public Long wctId;

	@JsonProperty("field_spt_id")
	public Long sptId;
	
    @Column(columnDefinition = "text")
	public String keywords;
	
	@Column(columnDefinition = "text")
	public String synonyms;
	
	//@JsonIgnore
	@JsonProperty("nominating_organisation")
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "organisation_id")
	public Organisation organisation;

	@JsonIgnore
	@Column(columnDefinition = "text")
	public String authors;

	public Date dateOfPublication;
	
	@Column(columnDefinition = "text")
	public String justification;

	@Required(message="Selection Type Required")
	public String selectionType;

	@Column(columnDefinition = "text")
	public String selectorNotes;
	
	@Column(columnDefinition = "text")
	public String archivistNotes;

	public Long legacySiteId;
	
	@JsonProperty("field_uk_postal_address")
	public Boolean ukPostalAddress;

	@Column(columnDefinition = "text")
	@JsonProperty("uk_postal_address_url")
	public String ukPostalAddressUrl;
	
	@JsonProperty("field_via_correspondence")
	public Boolean viaCorrespondence;
	
	/*
	@JsonProperty("field_via_correspondence_exp")
	public String viaCorrespondenceExp;
	*/
	
	@JsonProperty("field_professional_judgement")
	public Boolean professionalJudgement;

	@Column(columnDefinition = "text")
	@JsonProperty("field_professional_judgement_exp")
	public String professionalJudgementExp;
	
	@JsonProperty("field_no_ld_criteria_met")
	public Boolean noLdCriteriaMet;

	@JsonProperty("field_scope")
	public String scope;
	
	@JsonProperty("field_depth")
	public String depth;
	
	@JsonProperty("field_ignore_robots_txt")
	public Boolean ignoreRobotsTxt;
	
	@JsonProperty("field_crawl_frequency")
	public String crawlFrequency;
	
	@JsonIgnore
	public Date crawlStartDate;

	@JsonIgnore
	public Date crawlEndDate;
	
	public String whiteList; // regex for white list URLs
	public String blackList; // regex for black list URLs

	public String licenseStatus;
	
	@Transient
	public String tabStatus;

	@Transient
	@JsonProperty
	private String field_uk_domain;
	
	@Transient
	@JsonProperty
	private String field_uk_geoip;
	
	@Transient
	@JsonProperty
	private String field_crawl_permission;

	@SuppressWarnings("rawtypes")
	@JsonProperty
	private List<Map> field_url;

	@Transient
	@JsonProperty
	private FieldModel field_subject;
	
	@Transient
	@JsonProperty
	private Object field_description;

	@Transient
	@JsonProperty
	private Object field_uk_postal_address_url;

	@Transient
	@JsonProperty
	private FieldModel field_nominating_organisation;

	@Transient
	@JsonProperty
	private List<FieldModel> field_suggested_collections;

	@Transient
	@JsonProperty
	private List<FieldModel> field_collections;

	@Transient
	@JsonProperty
	private Long field_crawl_start_date;

	@Transient
	@JsonIgnore
	@JsonProperty
	private Long field_crawl_end_date;

	@Transient
	@JsonProperty
	private List<FieldModel> field_license;

	@Transient
	@JsonProperty
	private Object field_instances;

	@Transient
	@JsonProperty
	private List<FieldModel> field_collection_categories;

	@Transient
	@JsonProperty
	private FieldModel field_qa_status;

	@Transient
	@JsonProperty
	private List<FieldModel> field_snapshots;

	@Transient
	@JsonProperty
	private Object field_notes;

	@Transient
	@Required(message="Url(s) Required")
	public String formUrl;

	@Transient
	public String dateOfPublicationText;
	
	@Transient
	public String crawlStartDateText;

	@Transient
	public String crawlEndDateText;

	@Transient
	public String crawlStartDateISO;

	@Transient
	public String crawlEndDateISO;
	
	@Transient
	public String webFormDateText;

	
	@Transient
	public String subjectSelect;

	@Transient
	public String collectionSelect;

	@Transient
	public String authorIdText;

	@Transient
	@JsonProperty
	public String fieldUrl;

	@Transient
	@JsonProperty
	private List<String> field_urls;

	@Transient
	@JsonProperty
	private List<String> field_subjects;

	@Transient
	@JsonProperty
	private String field_nominating_org;

	@Transient
	@JsonProperty
	private List<String> field_collection_cats;

	@Transient
	@JsonProperty
	private String selector;
	
	@JsonIgnore
	@OneToOne(mappedBy="target", cascade={CascadeType.REFRESH,CascadeType.REMOVE})
	public WatchedTarget watchedTarget;

	@Transient
	private OverallLicenseStatus overallLicenseStatus;
	
	@JsonIgnore
	public boolean isWatched() {
		return watchedTarget != null;
	}
	
	@JsonIgnore
	public boolean hasDocuments() {
		return isWatched() && !watchedTarget.documents.isEmpty();
	}
	
	
//	  "title": "Your Thurrock" - fine
//	  "field_subject": ["24"],
//	  "field_crawl_frequency": "monthly" - fine
//	  "field_nominating_organisation": "101",
//	  "field_url": ["http://yourthurrock.com"],
//	  "field_collection_categories": ["297"],
//	  "field_crawl_start_date": "1417255200"
	
//		"body":[],
//		"field_scope":"root",
//		"field_url":[
//		             {"url":"http:\/\/www.13baseraf.co.uk\/","attributes":[]},
//		             {"url":"http:\/\/www.northlincsweb.net\/13Base\/","attributes":[]}],
	
	
//		"field_depth":"capped",
//		"field_via_correspondence":false,
//		"field_uk_postal_address":false,
//		"field_uk_hosting":false,

	
//		"field_description":[],

//		"field_uk_postal_address_url":[],

//		"field_nominating_organisation":{
//			"uri":"http:\/\/webarchive.org.uk\/act\/node\/101","id":"101","resource":"node"
//			},

//		"field_crawl_frequency":"annual",

//		"field_suggested_collections":[],
//		"field_collections":[],

//		"field_crawl_start_date":"1395968400",
//		"field_crawl_end_date":"1401580800",
//		"field_uk_domain":"No",

//		"field_license":[
//		                 {"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/168","id":"168","resource":"taxonomy_term"}],


//		"field_crawl_permission":"",

//		"field_collection_categories":[],
//		"field_special_dispensation":false,
//		"field_special_dispensation_reaso":null,
//		"field_live_site_status":null,
//		"field_notes":[],
//		"field_wct_id":"235438128",
//		"field_spt_id":"169073",
//		"field_snapshots":[],

//		"field_no_ld_criteria_met":false,
//		"field_key_site":false,
//		"field_uk_geoip":"Yes",
//		"field_professional_judgement":false,
//		"field_professional_judgement_exp":null,


//		"field_ignore_robots_txt":false,
//		"field_instances":[],
//		"nid":"14171",
//		"vid":"28185",
//		"is_new":false,
//		"type":"url",
//		"title":"13 Base - RAF",
//		"language":"en",
//		"url":"http:\/\/webarchive.org.uk\/act\/node\/14171",
//		"edit_url":"http:\/\/webarchive.org.uk\/act\/node\/14171\/edit",
//		"status":"1","promote":"0","sticky":"0","created":"1395767857","changed":"1404815005",
//		"author":{
//				"uri":"http:\/\/webarchive.org.uk\/act\/user\/191","id":"191","resource":"user"
//				},"log":"",
//		"revision":null,"comment":"2","comments":[],"comment_count":"0","comment_count_new":"0","feed_nid":null
	
	public Target() {
	}
	
	
	public void setDefaultValues() {
		// Defaults:
		this.scope = Const.ScopeType.root.name();
		this.depth = Const.DepthType.CAPPED.name();
		this.isUkHosting = Boolean.FALSE;
		this.isTopLevelDomain = Boolean.FALSE;
		this.isUkRegistration = Boolean.FALSE;
		this.liveSiteStatus = Const.SiteStatus.LIVE.name();
		this.hidden = Boolean.FALSE;
		this.keySite = Boolean.FALSE;
		this.ukPostalAddress = Boolean.FALSE;
		this.viaCorrespondence = Boolean.FALSE;
		this.professionalJudgement = Boolean.FALSE;
		this.ignoreRobotsTxt = Boolean.FALSE;
		this.crawlFrequency = Const.CrawlFrequency.DOMAINCRAWL.name();
	}

	public static Model.Finder<Long, Target> find = new Model.Finder<>(Long.class, Target.class);

	public static List<Target> findAll() {
		return find.all();
	}

	/**
	 * Count total number of records that rely on whois?
	 * 
	 * @param value
	 * @return
	 */
	public static int findWhoIsCount(boolean value) {
		int count = 0;
		if (value) {
			ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true).eq("isUkRegistration", true);
			count = ll.findRowCount();
		} else {
			ExpressionList<Target> ll = find
					.where()
					.eq(Const.ACTIVE, true)
					.add(Expr.or(
							Expr.eq("isUkRegistration",
									false),
							Expr.isNull("isUkRegistration")));
			count = ll.findRowCount();
		}
		return count;
	}

	/**
	 * This method filters targets by given URLs.
	 * 
	 * @return duplicate count
	 */
	public static List<Target> filterUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.fetch("fieldUrls").where().icontains("fieldUrls.url", url);
		res = ll.findList();
		return res;
	}

	public static List<Target> filterActiveUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.fetch("fieldUrls").where()
										.eq(Const.ACTIVE, true)
										.add(Expr.or(Expr.icontains("fieldUrls.url", url), Expr.icontains("t0.title", url)));
									
		res = ll.findList();
		return res;
	}

	/**
	 * Retrieve a Target by target URL.
	 * 
	 * @param target
	 *            URL
	 * @return target object
	 */
	public static Target findByTarget(String target) {
		Logger.debug("findByTarget() target url: " + target);
		if (!target.contains(Const.COMMA)) {
			return find.fetch("fieldUrls").where().eq("active", true).eq("fieldUrls.url", target).findUnique();
		} 
		return null;
	}

	/**
	 * This method returns previous Target revisions that are not more active
	 * for given URL
	 * 
	 * @param url
	 * @return list of associated Targets
	 */
	public static List<Target> findRevisions(String url) {
		Logger.debug("findRevisions() target url: " + url);
		List<Target> res = new ArrayList<Target>();
		if (url != null && url.length() > 0) {
			ExpressionList<Target> ll = find.fetch("fieldUrls").where().eq("fieldUrls.url", url);
			res = ll.findList();
		}
		return res;
	}

	public static Target findById(Long id) {
		Target target = find.fetch("fieldUrls").fetch("licenses").where().eq("id", id).findUnique();
		if( target != null ) {
			target.formUrl = target.fieldUrl();
		}
		return target;
	}

	public static Target findByUrl(String url) {
		return find.where().eq(Const.URL, url).eq(Const.ACTIVE, true).findUnique();
	}

	public static Target findByWct(String url) {
		return find.where().eq("edit_url", url).eq(Const.ACTIVE, true).findUnique();
	}

	/**
	 * This method checks whether the passed URL is in scope and presents result
	 * as a string in GUI.
	 * 
	 * @param fieldUrl
	 *            The field URL
	 * @param url
	 *            The identification URL
	 * @return result as a String
	 */
	public String checkScopeStr(String fieldUrl, String url) {
		String res = "false";
		if (fieldUrl != null && fieldUrl.length() > 0 && url != null
				&& url.length() > 0 && this.isInScopeAllOrInheritedWithoutLicense() ) {
			res = "true";
		}
		return res;
	}
	
	/**
	 * This method checks whether all the URLs are in scope for rules
	 * associated with scope IP.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	@JsonIgnore
	public boolean isInScope( boolean includedByPermission ) {
		for (FieldUrl fieldUrl : this.fieldUrls) {
			if( Scope.INSTANCE.check(fieldUrl.url, this, includedByPermission) == false ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method analyzes manual scope settings for Target with given URL
	 * 
	 * Professional Judgement.
	 * Postal Address (set manually to Yes by the user)
	 * By Correspondence (also set manually)
	 *  
	 *  In the UI, if any of the three manual tests (these two or professional judgement) is set to Yes, then the following field (which provide the evidence) are then required.
	 *  ie. if Postal Address is Yes, then Postal Address URL is required (doesn't need validating)
	 *  
	 * @param url
	 * @return true if one of manual settings is true
	 */
	public boolean checkManualScope() {
		boolean res = false;
		if (BooleanUtils.isTrue(this.professionalJudgement) || BooleanUtils.isTrue(this.ukPostalAddress) || BooleanUtils.isTrue(this.viaCorrespondence)) {
			Logger.debug("checkManualScope(): " + this.ukPostalAddress + ", " + this.viaCorrespondence + ", "+ this.professionalJudgement);
			res = true;
		}
		return res;
	}

	/**
	 * This method checks license for Target with given URL
	 * 
	 * @param url
	 * @return true if license exists
	 */
	public boolean checkLicense() {
		boolean res = false;
		if (this.licenses != null && !this.licenses.isEmpty()) {
			res = true;
		}
		return res;
	}

	/**
	 * This method checks whether the passed URL is in scope.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	public boolean isInScope(String url) {
		return Scope.INSTANCE.check(url, this, false);
	}


	/**
	 * This method returns the latest version of Target objects.
	 * 
	 * @return
	 */
	public static List<Target> findAllActive() {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true);
		res = ll.findList();
		return res;
	}

	/**
	 * This method returns the latest version of Target objects.
	 * 
	 * @param number
	 *            The number of targets for which the elapsed time since the
	 *            last check is greatest
	 * @return
	 */
	public static List<Target> findLastActive(int number) {
		List<Target> res = new ArrayList<Target>();
		res = find.where().eq(Const.ACTIVE, true)
				.orderBy(Const.UPDATED_AT + " " + Const.DESC)
				.setMaxRows(number).findList();
		return res;
	}

	/**
	 * This method finds all targets that share a domain.
	 */
	public static List<Target> findAllTargetsForDomain(String domain) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where()
				.eq(Const.ACTIVE, true)
				.ieq("fieldUrls.domain", domain);
		res = ll.findList();
		return res;
	}

	/**
	 * This method finds all targets similar to a domain
	 */
	public static List<Target> findAllTargetsForDomainLike(String domainLike) {
		Logger.debug("Looking for domains like:"+domainLike);
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where()
				.eq(Const.ACTIVE, true)
				.like("fieldUrls.domain", domainLike);
		res = ll.findList();
		return res;
	}

	/**
	 * This method evaluates if element is in a list separated by list delimiter
	 * e.g. ', '.
	 * 
	 * @param subject
	 * @return true if in list
	 */
	public boolean hasSubject(String subject) {
//		boolean res = false;
//		res = Utils.hasElementInList(subject, fieldSubject);
//		return res;
		throw new NotImplementedError();
	}

	/**
	 * This method evaluates if element is in a list separated by list delimiter
	 * e.g. ', '.
	 * 
	 * @param subject
	 * @return true if in list
	 */
	public boolean hasSubSubject(String subject) {
//		boolean res = false;
//		res = Utils.hasElementInList(subject, fieldSubSubject);
//		return res;
		throw new NotImplementedError();
	}

	/**
	 * This method evaluates if element is in a list separated by list delimiter
	 * e.g. ', '.
	 * 
	 * @param subject
	 * @return true if in list
	 */
	public static boolean hasSubSubject(String subsubject, String subject) {
		boolean res = false;
		res = Utils.INSTANCE.hasElementInList(subject, subsubject);
		return res;
	}

	/**
	 * This method evaluates if element is in a list separated by list delimiter
	 * e.g. ', '.
	 * 
	 * @param license
	 * @return true if in list
	 */
	public boolean hasLicense(String license) {
//		boolean res = false;
//		res = Utils.hasElementInList(license, fieldLicense);
//		return res;
		throw new NotImplementedError();
	}

	/**
	 * This method evaluates if element is in a list separated by list delimiter
	 * e.g. ', '.
	 * 
	 * @param fastSubjects
	 * @return true if in list
	 */
	public boolean hasContactPerson(String curContactPerson) {
		boolean res = false;
		res = Utils.INSTANCE.hasElementInList(curContactPerson, this.authorUser.url);
		return res;
	}

	/**
	 * Return a page of Target
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 */
	public static Page<Target> pageQa(int page, int pageSize, String sortBy,
			String order, String filter, String collections, Long qaIssueId) {

		Logger.debug("pageQa() collection: " + collections + ", qaStatus: " + qaIssueId + ", filter: " + filter);

		ExpressionList<Target> results = Target.find.fetch("fieldUrls").fetch("collections").fetch("instances").where();
		
		if (StringUtils.isNotEmpty(filter)) {
			results = results.add(Expr.or(
				Expr.icontains("fieldUrls.url", filter), 
				Expr.icontains("title", filter))
			);
		}
		
		Logger.debug("qaIssueId: " + qaIssueId);

		if (qaIssueId == null || qaIssueId == 0) {
			Logger.debug("nothing selected: " + qaIssueId);
			// get last instance
//			results = Target.find.fetch("fieldUrls").fetch("collections").fetch("instances").setMaxRows(1).orderBy("").where();
		} else {
			if (qaIssueId == 285) {
				// get last instance
				Logger.debug("unknown: " + qaIssueId);

				results = results.isNotNull("instances").add(Expr.or(
						Expr.isNull("instances.qaIssue.id"), 
						Expr.eq("instances.qaIssue.id", qaIssueId)));
			} else {
				results = results.eq("instances.qaIssue.id", qaIssueId);
			}
		}

        String collectionSelect = collections.replace("\"", "");
        Logger.debug("collectionSelect: " + collectionSelect);
        List<Collection> collectionIds = new ArrayList<Collection>();
        if (StringUtils.isNotEmpty(collectionSelect)) {
            String[] collectionArray = collectionSelect.split(", ");
            for (String c : collectionArray) {
            	Long collectionId = Long.valueOf(c);
            	Collection collection = Collection.findById(collectionId);
            	collectionIds.add(collection);
            }
    		results = results.in("collections", collectionIds);
        }		     

        results = results.eq("active", true);
//        order by t0.created_at desc
		Page<Target> res = results.query().orderBy(sortBy + " " + order).findPagingList(pageSize).setFetchAhead(false).getPage(page);        
        Logger.debug("results: " + res.getList().size());
		return res;
	}

	/**
	 * Return a page of Target objects.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filterUrl
	 *            Filter applied on target urls
	 * @param curatorId
	 *            Author of the target
	 * @param organisationUrl
	 *            The author's organisation
	 * @param subjectUrl
	 *            Target subject
	 * @param crawlFrequency
	 *            The crawl frequency
	 * @param depth
	 *            The crawl depth
	 * @param suggested_collections
	 *            The associated collection
	 * @param license
	 *            The license name
	 * @param pageSize
	 *            The number of Target entries on the page
	 * @param flag
	 *            The flag assigned by user
	 * @return
	 */
	public static Page<Target> pageTargets(int page, int pageSize,
			String sortBy, String order, String filterUrl, Long curatorId,
			Long organisationId, String subjectSelect, String crawlFrequencyName,
			String depthName, String collectionSelect, Long licenseId,
			Long flagId) {
		
		ExpressionList<Target> exp = Target.find.fetch("fieldUrls").where();
		
		exp = exp.eq(Const.ACTIVE, true);
	
		exp = exp.add(Expr.or(
				Expr.icontains("fieldUrls.url", filterUrl), 
				Expr.icontains("title", filterUrl))
			);
		
		if (curatorId != 0) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != 0) {
			exp = exp.eq("organisation.id", organisationId);
		}
		if (StringUtils.isNotEmpty(crawlFrequencyName)) {
			exp = exp.eq("crawlFrequency", crawlFrequencyName);
		}
		if (StringUtils.isNotEmpty(depthName)) {
			exp = exp.eq("depth", depthName);
		}
		if (licenseId != 0) {
			exp = exp.eq("licenses.id", licenseId);
		}
		if (flagId != 0) {
			exp = exp.eq("flags.id", flagId);
		}
		
        if (StringUtils.isNotEmpty(subjectSelect)) {
        	List<Long> subjectIds = new ArrayList<Long>();
            String[] subjects = subjectSelect.split(", ");
            for (String sId : subjects) {
            	Long subjectId = Long.valueOf(sId);
            	subjectIds.add(subjectId);
            }
    		exp = exp.in("subjects.id", subjectIds);
        }
        
        if (StringUtils.isNotEmpty(collectionSelect)) {
        	List<Collection> collectionIds = new ArrayList<Collection>();
            String[] collections = collectionSelect.split(", ");
            for (String cId : collections) {
            	Long collectionId = Long.valueOf(cId);
            	Collection collection = Collection.findById(collectionId);
            	collectionIds.add(collection);
            }
    		exp = exp.in("collections", collectionIds);
        }

        Page<Target> res = exp.query().orderBy(sortBy + " " + order).orderBy("fieldUrls.domain").findPagingList(pageSize).setFetchAhead(false).getPage(page);
		Logger.debug("Expression list size: " + res.getTotalRowCount());
		return res;
	}

	/**
	 * Return a page of Target objects.
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param status
	 *            The type of report QA e.g. awaiting QA, with no QA issues...
	 * @param curatorUrl
	 * @param organisationUrl
	 * @param startDate
	 *            The start date for filtering
	 * @param endDate
	 *            The end date for filtering
	 * @param collectionCategoryUrl
	 * @return
	 */
	public static Page<Target> pageReportsQa(int page, int pageSize,
			String sortBy, String order, String status, Long curatorId,
			Long organisationId, String startDate, String endDate,
			Long collectionId) {
		
//		List<Instance> instanceList = Instance.processReportsQa(status, startDate, endDate);
		
		ExpressionList<Target> exp = Target.find.fetch("collections").fetch("fieldUrls").where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
	
		if (curatorId != -1) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != -1) {
			exp = exp.eq("organisation.id", organisationId);
		}
		if (collectionId != -1) {
			exp = exp.eq("collections.id", collectionId);
		}
//		List<String> targetUrlCollection = new ArrayList<String>();
//		Iterator<Instance> itr = instanceList.iterator();
//		while (itr.hasNext()) {
//			Instance instance = itr.next();
//			if (instance.fieldTarget != null
//					&& instance.fieldTarget.length() > 0) {
//				// Logger.debug("Target.pageReportsQa() instance.field_target: "
//				// + instance.field_target);
//				targetUrlCollection.add(instance.fieldTarget);
//			}
//		}
//		if (targetUrlCollection.size() > 0) {
//			exp = exp.in(Const.URL, targetUrlCollection);
//		}
		res = exp.query().orderBy(sortBy + " " + order).orderBy("fieldUrls.domain").findPagingList(pageSize).setFetchAhead(false).getPage(page);
//		Logger.debug("Expression list for targets size: " + res.getTotalRowCount());
		return res;
	}

	/**
	 * Return a page of Target objects.
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param curatorUrl
	 * @param organisationUrl
	 * @param startDate
	 *            The start date for filtering
	 * @param endDate
	 *            The end date for filtering
	 * @param npld
	 *            The selection of NPLD scope rule for filtering
	 * @param crawlFrequency
	 *            The crawl frequency value for filtering
	 * @param tld
	 *            The top level domain setting for filtering
	 * @return
	 * @throws ParseException 
	 */
	public static Page<Target> pageReportsCreation(int page, int pageSize,
			String sortBy, String order, Long curatorId,
			Long organisationId, String startDate, String endDate, String npld, String crawlFrequencyName, String tld) throws ActException {

		ExpressionList<Target> exp = Target.find.fetch("fieldUrls").fetch("flags").fetch("licenses").fetch("subjects").fetch("collections").where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
		
		Logger.debug("" + curatorId + ", " + organisationId + ", " + startDate + ", " + npld + ", " + crawlFrequencyName + ", " + tld);
		if (curatorId != -1) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != -1) {
			exp = exp.eq("organisation.id", organisationId);
		}
		if (StringUtils.isNotEmpty(crawlFrequencyName)) {
			exp = exp.eq("crawlFrequency", crawlFrequencyName);
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				Date date = Utils.INSTANCE.convertDate(startDate);
				exp = exp.ge("createdAt", date);
	        }
			
			if (StringUtils.isNotEmpty(endDate)) {
				Date date = Utils.INSTANCE.convertDate(endDate);
				exp = exp.le("createdAt", date);
			}
			
		} catch (ParseException e) {
			throw new ActException(e);
		}
		
		// Create raw expr for matching domains:
		String notdomexp = "";
		String domexp = "";
		Iterator<String> tlds = Scope.DOMAINS.iterator();
		while( tlds.hasNext() ) {
			String tnext = tlds.next();
			notdomexp += "fieldUrls.domain NOT like '%"+ tnext + "'";
			domexp += "fieldUrls.domain like '%"+ tnext + "'";
			if( tlds.hasNext() ){
				notdomexp += " and ";
				domexp += " or ";
			}
		}		

		// new stuff
		if (npld.equals(Const.NpldType.UK_POSTAL_ADDRESS.name())) {
			exp = exp.eq("ukPostalAddress", true);
		} else if (npld.equals(Const.NpldType.VIA_CORRESPONDENCE.name())) {
			exp = exp.eq("viaCorrespondence", true);
		} else if (npld.equals(Const.NpldType.NO_LD_CRITERIA_MET.name())) {
			exp = exp.eq("noLdCriteriaMet", true);
		} else if (npld.equals(Const.NpldType.PROFESSIONAL_JUDGEMENT.name())) {
			exp = exp.eq("professionalJudgement", true);
		} else if (npld.equals(Const.NpldType.NONE.name())) {
			exp = exp.eq("ukPostalAddress", false);
			exp = exp.eq("viaCorrespondence", false);
			exp = exp.eq("noLdCriteriaMet", false);
			exp = exp.eq("professionalJudgement", false);
			exp = exp.eq("isUkHosting", false);
			exp = exp.eq("isUkRegistration", false);			
			exp = exp.add(Expr.raw(notdomexp));
		} else if (npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
			exp = exp.add(Expr.raw(domexp));
		} else if (npld.equals(Const.NpldType.UK_HOSTING.name())) {
			// uk hosting
			exp = exp.eq("isUkHosting", true);
		} else if (npld.equals(Const.NpldType.UK_REGISTRATION.name())) {
			// uk registration address
			exp = exp.eq("isUkRegistration", true);
		}

		if (tld.equals("no")) {
			// not a UK top level domain
			exp = exp.eq("isTopLevelDomain", false);
		}
		if (tld.equals("yes") || npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
			// UK top level domain
			exp = exp.eq("isTopLevelDomain", true);
		}
		if (tld.equals(Const.EITHER)) {
			// not a UK top level domain
			// expressionList.eq("isTopLevelDomain", false);
			// expressionList.eq("isTopLevelDomain", true);
		}

		// TODO: NONE SELECTED???

		Logger.debug("pageReportsCreation() NPLD: " + npld);

		/**
		 * Apply NPLD filters
		 */
		// if (!tld.equals(Const.EITHER)) {
		// Logger.debug("pageReportsCreation() Apply NPLD filters");
		// List<String> targetUrlCollection = new ArrayList<String>();
		// Page<Target> tmp = exp.query()
		// .orderBy(sortBy + " " + order)
		// .findPagingList(pageSize)
		// .setFetchAhead(false)
		// .getPage(page);

		// TODO: do we really need to query first?
		// List<Target> tmp = expressionList.query()
		// .orderBy(sortBy + " " + order)
		// .findList();
		//
		//
		// Logger.debug("pageReportsCreation() tmp list size: " + tmp.size());
		// Iterator<Target> itr = tmp.iterator();
		// while (itr.hasNext()) {
		// Target target = itr.next();
		// if (target != null
		// && target.field_url != null
		// && target.field_url.length() > 0
		// && !target.field_url.toLowerCase().contains(Const.NONE)) {
		//
		// // target.isInScopeDomainValue =
		// Target.isInScopeDomain(target.field_url, target.url);
		// // // do a contains on target.field_url??? (url.contains(UK_DOMAIN)
		// || url.contains(LONDON_DOMAIN) || url.contains(SCOT_DOMAIN))
		// //
		// // target.isUkHostingValue = Target.checkUkHosting(target.field_url);
		// // target.isInScopeUkRegistrationValue =
		// Target.isInScopeUkRegistration(target.field_url);
		// }
		//
		// Logger.debug("pageReportsCreation() targetUrlCollection size: " +
		// targetUrlCollection.size());
		// expressionList = expressionList.in(Const.URL, targetUrlCollection);
		// }

		Query<Target> query = exp.query();

		res = query.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);

		Logger.debug("Expression list for targets created size: "
				+ res.getTotalRowCount());

		return res;

	}

	/**
	 * Return a page of Target
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 * @param collection_url
	 *            Collection where targets search occurs
	 * @return
	 */
	public static Page<Target> pageCollectionTargets(int page, int pageSize,
			String sortBy, String order, String filter, Long collectionId) {
		return find.where().eq("collections.id", collectionId)
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}


	/**
	 * 
	 * @param collectionId
	 * @return
	 */
	public static List<Target> allCollectionTargets(Long collectionId) {
		return find.where().eq("collections.id", collectionId).findList();
	}
	
	/**
	 * Return a page of Target
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 * @param subject_url
	 *            Subject where targets search occurs
	 * @return
	 */
	public static Page<Target> pageSubjectTargets(int page, int pageSize,
			String sortBy, String order, String filter, Long subjectId) {

		return find.where().eq("subjects.id", subjectId)
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	/**
	 * Return a page of Target
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 * @param organisation_url
	 *            Organisation where targets search occurs
	 * @return
	 */
	public static Page<Target> pageOrganisationTargets(int page, int pageSize,
			String sortBy, String order, String filter, Long organisationId) {

		return find.fetch("fieldUrls").where().eq("active", true)
				.add(Expr.or(Expr.icontains("fieldUrls.url", filter),Expr.icontains("title", filter)))
				.add(Expr.eq("organisation.id", organisationId))
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	/**
	 * Return a page of Target
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of targets per page
	 * @param sortBy
	 *            Target property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 * @param user_url
	 *            User for whom targets search occurs
	 * @param fastSubjects
	 *            Taxonomy of type subject
	 * @param collection
	 *            Taxonomy of type collection
	 * @return
	 */
	public static Page<Target> pageUserTargets(int page, int pageSize,
			String sortBy, String order, String filter, Long userId,
			Long subjectId, Long collectionId) {
		
		Logger.debug("pageUserTargets " + userId + ", " + subjectId + ", " + collectionId);
		
		ExpressionList<Target> exp = find.fetch("fieldUrls").fetch("collections").fetch("subjects").fetch("authorUser").where();
		
		exp = exp.eq(Const.ACTIVE, true);
			
		if (userId != null) {
			exp = exp.eq("authorUser.id", userId);
		}
		if (subjectId.longValue() != 0L) {
			exp = exp.eq("subjects.id", subjectId);
		}
		if (collectionId.longValue() != 0L) {
			exp = exp.eq("collections.id", collectionId);
		}
		
		exp = exp.add(Expr.or(Expr.icontains("fieldUrls.url", filter), Expr.icontains("title", filter)));

		Page<Target> pages = exp.query().orderBy(sortBy + " " + order).orderBy("fieldUrls.domain").findPagingList(pageSize).setFetchAhead(false).getPage(page);

		return pages;
	}
	
//	checkScopeIpWithoutLicense
	public static boolean isInScope(Target target) throws WhoisException {
		for (FieldUrl fieldUrl : target.fieldUrls) {
			if(!Scope.INSTANCE.check(fieldUrl.url, target, false)) return false;
		}
		return true;
	}

	public static boolean isInScopeDomain(Target target) throws ActException {
		 return Scope.isTopLevelDomain(target);
	}
	
	/**
	 * This method provides data exports for given crawl-frequency. Method
	 * returns a list of Targets and associated crawl metadata.
	 * 
	 * @param frequency
	 *            The crawl frequency e.g. 'daily'
	 * @return list of Target objects
	 * @throws WhoisException 
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 */
	public static List<Target> exportLdFrequency(String frequency) throws WhoisException, MalformedURLException, URISyntaxException {
		ExpressionList<Target> targets = find.fetch("fieldUrls").where().eq(Const.ACTIVE, true);
		if (!frequency.equalsIgnoreCase("all")) {
			targets = targets.ieq("crawlFrequency", frequency);
		} else {
			targets = targets.ne("crawlFrequency", Const.CrawlFrequency.NEVERCRAWL.name());
		}
		
		List<Target> result = new ArrayList<Target>();
		for (Target target : targets.findList()) {
			if (target.isInScopeAllOrInheritedWithoutLicense()) {
				result.add(target);
			}
		}
		Logger.debug("exportLdFrequency() resulting list size: " + result.size());
		return result;
	}

	/**
	 * This method provides data exports for given crawl-frequency. Method
	 * returns a list of Targets and associated crawl metadata.
	 * 
	 * @param frequency
	 *            The crawl frequency e.g. 'daily'
	 * @return list of Target objects
	 */
	public static List<Target> exportByFrequency(String frequency) {
		ExpressionList<Target> targets = find.fetch("licenses").where().eq(Const.ACTIVE, true);
		if (!frequency.equalsIgnoreCase("all")) {
			targets = targets.ieq("crawlFrequency", frequency);
		} else {
			targets = targets.ne("crawlFrequency", Const.CrawlFrequency.NEVERCRAWL.name());
		}

		/**
		 * The resulting list should only include everything we are able to crawl.
		 */
		List<Target> result = new ArrayList<Target>();
		Iterator<Target> itr = targets.findList().iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			// This includes all, rather than just the licensed stuff:
			if (target.isInScopeAllOrInheritedWithoutLicense() || target.indicateLicenses()) {
			//if ( target.indicateLicenses() ) {
				result.add(target);
			}
		}
		Logger.debug("exportByFrequency() resulting list size: " + result.size());
		return result;
	}
	
	/**
	 * This method evaluates the latest created target from the passed unsorted
	 * list.
	 * 
	 * @param unsorted
	 *            The unsorted list.
	 * @return
	 */
	public static Target getLatestCreatedTarget(List<Target> unsorted) {
		Target res = null;
		long latest = 0L;
		Iterator<Target> itr = unsorted.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.createdAt != null) {
				latest = target.createdAt.getTime();
				res = target;
			}
		}
		Logger.debug("getLatestCreatedTarget() res: " + res);
		return res;
	}

	public Boolean getIsUkHosting() {
		return isUkHosting;
	}

	public void setIsUkHosting(Boolean isUkHosting) {
		this.isUkHosting = isUkHosting;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getField_url() {
		return field_url;
	}

	@SuppressWarnings("rawtypes")
	public void setField_url(List<Map> field_url) {
		this.field_url = field_url;
	}

	public FieldModel getField_subject() {
		return field_subject;
	}

	public void setField_subject(FieldModel field_subject) {
		this.field_subject = field_subject;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public Boolean getViaCorrespondence() {
		return viaCorrespondence;
	}

	public void setViaCorrespondence(Boolean viaCorrespondence) {
		this.viaCorrespondence = viaCorrespondence;
	}

	public Boolean getUkPostalAddress() {
		return ukPostalAddress;
	}

	public void setUkPostalAddress(Boolean ukPostalAddress) {
		this.ukPostalAddress = ukPostalAddress;
	}

	public Object getField_description() {
		return field_description;
	}

	public void setField_description(Object field_description) {
		this.field_description = field_description;
	}

	public Object getField_uk_postal_address_url() {
		return field_uk_postal_address_url;
	}

	public void setField_uk_postal_address_url(
			Object field_uk_postal_address_url) {
		this.field_uk_postal_address_url = field_uk_postal_address_url;
	}

	public FieldModel getField_nominating_organisation() {
		return field_nominating_organisation;
	}

	public void setField_nominating_organisation(
			FieldModel field_nominating_organisation) {
		this.field_nominating_organisation = field_nominating_organisation;
	}

	public List<FieldModel> getField_suggested_collections() {
		return field_suggested_collections;
	}

	public void setField_suggested_collections(
			List<FieldModel> field_suggested_collections) {
		this.field_suggested_collections = field_suggested_collections;
	}

	public List<FieldModel> getField_collections() {
		return field_collections;
	}

	public void setField_collections(List<FieldModel> field_collections) {
		this.field_collections = field_collections;
	}

	public Long getField_crawl_start_date() {
		return field_crawl_start_date;
	}

	public void setField_crawl_start_date(Long field_crawl_start_date) {
		this.field_crawl_start_date = field_crawl_start_date;
	}

	public Long getField_crawl_end_date() {
		return field_crawl_end_date;
	}

	public void setField_crawl_end_date(Long field_crawl_end_date) {
		this.field_crawl_end_date = field_crawl_end_date;
	}

	public String getField_uk_domain() {
		return field_uk_domain;
	}

	public void setField_uk_domain(String field_uk_domain) {
		this.field_uk_domain = field_uk_domain;
	}

	public List<FieldModel> getField_license() {
		return field_license;
	}

	public void setField_license(List<FieldModel> field_license) {
		this.field_license = field_license;
	}

	public String getField_crawl_permission() {
		return field_crawl_permission;
	}

	public void setField_crawl_permission(String field_crawl_permission) {
		this.field_crawl_permission = field_crawl_permission;
	}

	public List<FieldModel> getField_collection_categories() {
		return field_collection_categories;
	}

	public void setField_collection_categories(
			List<FieldModel> field_collection_categories) {
		this.field_collection_categories = field_collection_categories;
	}

	public Boolean getSpecialDispensation() {
		return specialDispensation;
	}

	public void setSpecialDispensation(Boolean specialDispensation) {
		this.specialDispensation = specialDispensation;
	}

	public String getSpecialDispensationReason() {
		return specialDispensationReason;
	}

	public void setSpecialDispensationReason(String specialDispensationReason) {
		this.specialDispensationReason = specialDispensationReason;
	}

	public FieldModel getField_qa_status() {
		return field_qa_status;
	}

	public void setField_qa_status(FieldModel field_qa_status) {
		this.field_qa_status = field_qa_status;
	}

	public String getLiveSiteStatus() {
		return liveSiteStatus;
	}

	public void setLiveSiteStatus(String liveSiteStatus) {
		this.liveSiteStatus = liveSiteStatus;
	}

	public Object getField_notes() {
		return field_notes;
	}

	public void setField_notes(Object field_notes) {
		this.field_notes = field_notes;
	}

	public Long getWctId() {
		return wctId;
	}

	public void setWctId(Long wctId) {
		this.wctId = wctId;
	}

	public Long getSptId() {
		return sptId;
	}

	public void setSptId(Long sptId) {
		this.sptId = sptId;
	}

	public List<FieldModel> getField_snapshots() {
		return field_snapshots;
	}

	public void setField_snapshots(List<FieldModel> field_snapshots) {
		this.field_snapshots = field_snapshots;
	}

	public Boolean getNoLdCriteriaMet() {
		return noLdCriteriaMet;
	}

	public void setNoLdCriteriaMet(Boolean noLdCriteriaMet) {
		this.noLdCriteriaMet = noLdCriteriaMet;
	}

	public Boolean getKeySite() {
		return keySite;
	}

	public void setKeySite(Boolean keySite) {
		this.keySite = keySite;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getField_uk_geoip() {
		return field_uk_geoip;
	}

	public void setField_uk_geoip(String field_uk_geoip) {
		this.field_uk_geoip = field_uk_geoip;
	}


	public Boolean getProfessionalJudgement() {
		return professionalJudgement;
	}

	public void setProfessionalJudgement(Boolean professionalJudgement) {
		this.professionalJudgement = professionalJudgement;
	}

	public String getProfessionalJudgementExp() {
		return professionalJudgementExp;
	}

	public void setProfessionalJudgementExp(String professionalJudgementExp) {
		this.professionalJudgementExp = professionalJudgementExp;
	}

	public Boolean getIgnoreRobotsTxt() {
		return ignoreRobotsTxt;
	}

	public void setIgnoreRobotsTxt(Boolean ignoreRobotsTxt) {
		this.ignoreRobotsTxt = ignoreRobotsTxt;
	}

	public Object getField_instances() {
		return field_instances;
	}

	public void setField_instances(Object field_instances) {
		this.field_instances = field_instances;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	
	@JsonIgnore
	public String fieldUrl() {
		List<String> urls = new ArrayList<String>();
		//
		/*
		Collections.sort(fieldUrls, new Comparator<FieldUrl>(){
		     @Override
			public int compare(FieldUrl o1, FieldUrl o2){
		         if(o1.position == o2.position)
		             return 0;
		         return o1.position < o2.position ? -1 : 1;
		     }
		});
		*/
		// Build up the field string:
		for (FieldUrl fieldUrl : fieldUrls) {
			Logger.info("Adding URL to string: "+fieldUrl.id+":"+fieldUrl.url+" "+fieldUrl.position);
			urls.add(fieldUrl.url);
		}
		return StringUtils.join(urls, ", ");
	}
	
	@JsonIgnore
	public String primaryUrl() {
		if( this.fieldUrls != null && this.fieldUrls.size() > 0 ) {
			return this.fieldUrls.get(0).url;
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	public String subjectIdsAsString() {
		return StringUtils.join(this.subjectIds(), ", ");
	}

	@JsonIgnore
	public List<Long> subjectIds() {
		List<Long> ids = new ArrayList<Long>();
		for (Subject subject : this.subjects) {
			ids.add(subject.id);
		}
		return ids;
	}
	
	@JsonIgnore
	public String subjectsAsString() {
		List<String> names = new ArrayList<String>();
		for (Subject subject : this.subjects) {
			names.add(subject.name);
		}
		return StringUtils.join(names, ", ");
	}
	
	@JsonIgnore
	public List<Long> collectionIds() {
		List<Long> ids = new ArrayList<Long>();
		for (Collection collection : this.collections) {
			ids.add(collection.id);
		}
		return ids;
	}
	
	@JsonIgnore
	public String collectionIdsAsString() {
		return StringUtils.join(collectionIds(), ", ");
	}
	
	@JsonIgnore
	public String collectionsAsString() {
		List<String> names = new ArrayList<String>();
		for (Collection collection : this.collections) {
			names.add(collection.name);
		}
		return StringUtils.join(names, ", ");
	}
	
	@JsonIgnore
	public String licensesAsString() {
		Logger.debug("licensesAsString");
		List<String> names = new ArrayList<String>();
		for (License license : this.licenses) {
			names.add(license.name);
		}
		Logger.debug("" + names);
		return StringUtils.join(names, ", ");
	}
	
	@JsonIgnore
	public boolean isUkHosting() {
		if( this.isUkHosting == null ) return false;
		return this.isUkHosting;
	}
	
	@JsonIgnore
	public boolean isTopLevelDomain() {
		if( this.isTopLevelDomain == null ) return false;
		return this.isTopLevelDomain;
	}
	
	@JsonIgnore
	public boolean isUkRegistration() throws WhoisException {
		if( this.isUkRegistration == null ) return false;
		return this.isUkRegistration;
	}
	
	@JsonIgnore
	public String tagsAsString() {
		List<String> names = new ArrayList<String>();
		for (Tag tag : this.tags) {
			names.add(tag.name);
		}
		return StringUtils.join(names, ", ");
	}

	@JsonIgnore
	public String flagsAsString() {
		List<String> names = new ArrayList<String>();
		for (Flag flag : this.flags) {
			names.add(flag.name);
		}
		return StringUtils.join(names, ", ");
	}
	
	
	public String getDateOfPublicationText() {
		if (dateOfPublication != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateOfPublicationText = dateFormat.format(dateOfPublication);
		}
		return dateOfPublicationText;
	}

	public String getWebFormDateText() {
		if (webFormDate != null) {
			webFormDateText = Utils.INSTANCE.convertToDateString(webFormDate);
		}else{
			
			webFormDateText = "";
		}
		return webFormDateText;
	}

	
	public String getCrawlStartDateText() {
		if (crawlStartDate != null) {
			crawlStartDateText = Utils.INSTANCE.convertToDateTime(crawlStartDate);
		}else{
			
			crawlStartDateText = "";
		}
		return crawlStartDateText;
	}
	
	public String getCrawlEndDateText() {
		if (crawlEndDate != null) {
			crawlEndDateText = Utils.INSTANCE.convertToDateTime(crawlEndDate);
		}else{
			crawlEndDateText = "";
		}
		return crawlEndDateText;
	}
	
	public String getCrawlStartDateISO() {
		if (crawlStartDate != null) {
			crawlStartDateISO = Utils.INSTANCE.convertToDateTimeISO(crawlStartDate);
		}
		return crawlStartDateISO;
	}

	public String getCrawlEndDateISO() {
		if (crawlEndDate != null) {
			crawlEndDateISO = Utils.INSTANCE.convertToDateTimeISO(crawlEndDate);
		}
		return crawlEndDateISO;
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with scope IP. This check is without license field.
	 * NPLD
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	@JsonIgnore
	public boolean isInScopeAllWithoutLicense() {
		Logger.debug("isInScopeAllWithoutLicense()");
		
		// Manual scope:
		if (this.checkManualScope())
		    return true;

		// Cached values for other allowed mechanisms:
		if ( Boolean.TRUE.equals(this.isTopLevelDomain) || 
			 Boolean.TRUE.equals(this.isUkHosting) || 
			 Boolean.TRUE.equals(this.isUkRegistration)) {
			return true;
		}
		
		// Otherwise, nope:
		return false;
	}
	
	@JsonProperty
	@Transient
	public boolean getInScopeForLegalDeposit() {
		return this.isInScopeAllOrInheritedWithoutLicense();
	}
	
	@JsonIgnore
	public void setInScopeForLegalDeposit(boolean dummy) {
	}	
	
	@JsonIgnore
	public boolean isInScopeAllOrInheritedWithoutLicense() {
		Logger.debug("isInScopeAllOrInheritedWithoutLicense()");
		
		// Manual scope:
		if (this.isInScopeAllWithoutLicense())
		    return true;

		// Possibly inherited scope:
		if (this.hasInheritedNpldScope())
			return true;

		// Otherwise, nope:
		return false;
	}

	/**
	 * This helper looks at the direct and inherited licenses and returns an object that 
	 * describes the current state.
	 * 
	 * @param targetId
	 * @return
	 */
	@JsonIgnore
	@Transient
	public OverallLicenseStatus getOverallLicenseStatus() {
		if( this.overallLicenseStatus == null)
			this.overallLicenseStatus = new OverallLicenseStatus(this);
		return this.overallLicenseStatus;
	}
	

	/**
	 * Simple boolean check build on presence of a license.
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean hasInheritedLicense() {
		OverallLicenseStatus ols = getOverallLicenseStatus();
		return ols.inheritedLicense;
	}

	@JsonIgnore
    public boolean hasInheritedNpldScope() {
		OverallLicenseStatus ols = getOverallLicenseStatus();
		return ols.inheritedNPLDScope;
    }

	@JsonIgnore
    public boolean indicateNpldStatus() {
		return hasInheritedNpldScope();
    }

	@JsonIgnore
	public Set<Target> getNpldStatusList() {
		OverallLicenseStatus ols = getOverallLicenseStatus();
		return ols.NPLDParents;
	}

	public boolean indicateUkwaLicenceStatus() {
		OverallLicenseStatus ols = getOverallLicenseStatus();
		return ols.licensedOrPending;
	}
	
	/**
	 * This method should give a list of the Target records, which have an Open
	 * UKWA Licence request in progress for a target at a higher level in the
	 * domain. [ie. when Open UKWA License Request field = Queued, Pending,
	 * Refused, Granted - any value except None.
	 * 
	 * @return target list
	 */
	@JsonIgnore
	public Set<Target> getUkwaLicenceStatusList() {
		OverallLicenseStatus ols = getOverallLicenseStatus();
		return ols.licenseParents;
	}
	
	@JsonIgnore
	public boolean hasLicenses() {
		return (this.licenses != null && !this.licenses.isEmpty());
	}

	@JsonProperty
	@Transient
	public boolean getHasOpenAccessLicense() {
		return this.indicateLicenses();
	}
	
	@JsonIgnore
	public void setHasOpenAccessLicense(boolean dummy) {
	}
	
	@JsonIgnore
	public boolean indicateLicenses() {
		return (hasLicenses() || hasInheritedLicense());
	}    
    
	// Cannot create
	public boolean enableLicenseCreation() {
		return (StringUtils.isBlank(this.licenseStatus) || this.isNotInitiated());
	}
	
	// only sys admin and archivist can create
	@JsonIgnore
	public boolean hasInvalidLicenses() {
		return (this.isRefused() || this.isEmailRejected() || this.isSuperseded());
	}
	
	@JsonIgnore
    public boolean hasStatus(String licenseStatus) {
    	return (StringUtils.isNotEmpty(this.licenseStatus) && this.licenseStatus.equals(licenseStatus));
    }
    
	@JsonIgnore
    public boolean isGranted() {
    	return this.hasStatus(LicenseStatus.GRANTED.name());
    }
    
	@JsonIgnore
    public boolean isNotInitiated() {
    	return this.hasStatus(LicenseStatus.NOT_INITIATED.name());
    }
    
	@JsonIgnore
    public boolean isQueued() {
    	return this.hasStatus(LicenseStatus.QUEUED.name());
    }
    
	@JsonIgnore
    public boolean isPending() {
    	return this.hasStatus(LicenseStatus.PENDING.name());
    }
    
	@JsonIgnore
    public boolean isRefused() {
    	return this.hasStatus(LicenseStatus.REFUSED.name());
    }
    
	@JsonIgnore
    public boolean isEmailRejected() {
    	return this.hasStatus(LicenseStatus.EMAIL_REJECTED.name());
    }
    
	@JsonIgnore
    public boolean isSuperseded() {
    	return this.hasStatus(LicenseStatus.SUPERSEDED.name());
    }
    	
	// to helper
	@JsonIgnore
	public boolean hasGrantedLicense() {
		Logger.debug("hasGrantedLicense");
		return (this.hasLicenses());
	}

	public boolean hasQaIssue() {
		return (this.qaIssue != null);
	}
	
	public static boolean hasQaIssue(Long targetId) {
		Target target = Target.findById(targetId);
		return (target.qaIssue != null);
	}
	
	@JsonIgnore
	public CrawlPermission getLatestCrawlPermission() {
		if (crawlPermissions != null && crawlPermissions.size() > 0) {
			return crawlPermissions.get(crawlPermissions.size() - 1);
		}
		return null;
	}
	
	@JsonIgnore
	public Instance getLatestInstance() {
		if (instances != null && instances.size() > 0) {
			return instances.get(instances.size() - 1);
		}
		return null;
	}
	
	@JsonIgnore
	public Instance findLastInstance() {
		Instance instance = Instance.findLastInstanceByTarget(this.id);
//		Logger.debug("Last instance: " + instance);
		return instance;
	}
	
	@JsonIgnore
	public boolean isDeletable() {
		Logger.debug("collections size...." + this.collections.size());
		Logger.debug("licenses size...." + this.licenses.size());
		return (!this.checkLicense() && CollectionUtils.isEmpty(this.collections));
	}
	
    @PreUpdate
    @PrePersist
	public void preSaveChecks() {
		Logger.debug("before persist");
		runChecks();
		Logger.debug("after persist");
	}

    public void runChecks() {
		this.isUkHosting = Scope.INSTANCE.isUkHosting(this);
		this.isTopLevelDomain = Scope.isTopLevelDomain(this);
		this.isUkRegistration = Scope.INSTANCE.isUkRegistration(this);
		Logger.debug("runChecks done: "+this.isUkHosting+" "+this.isTopLevelDomain+" "+this.isUkRegistration);
    }
    
    @JsonIgnore
    @Transient
    public List<Collection> getCollectionCategories() {
    	List<Collection> categories = new ArrayList<Collection>();
    	for (Collection collection : this.collections) {
    		if (collection.parent == null) {
    			categories.add(collection);
    		}
    	}
    	Logger.debug(this.collections.size()  + " - " + categories.size());
    	return categories;
//    	return Collection.getCollectionCategoriesByTargetId(this.id);
    }
    
	public List<String> getField_urls() {
		return field_urls;
	}

	public void setField_urls(List<String> field_urls) {
		this.field_urls = field_urls;
	}

	public List<String> getField_subjects() {
		return field_subjects;
	}

	public void setField_subjects(List<String> field_subjects) {
		this.field_subjects = field_subjects;
	}

	public String getField_nominating_org() {
		return field_nominating_org;
	}

	public void setField_nominating_org(String field_nominating_org) {
		this.field_nominating_org = field_nominating_org;
	}

	public List<String> getField_collection_cats() {
		return field_collection_cats;
	}

	public void setField_collection_cats(List<String> field_collection_cats) {
		this.field_collection_cats = field_collection_cats;
	}

	@JsonIgnore
	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}
	
	
	public String getUkPostalAddressUrl() {
		return ukPostalAddressUrl;
	}

	public void setUkPostalAddressUrl(String ukPostalAddressUrl) {
		this.ukPostalAddressUrl = ukPostalAddressUrl;
	}

	@Override
	public String toString() {
		return "Target [qaIssue=" + qaIssue + ", authorUser=" + authorUser
				+ ", authors=" + authors + ", organisation=" + organisation
				+ ", crawlPermissions=" + crawlPermissions + ", instances="
				+ instances + ", licenses=" + licenses + ", subjects="
				+ subjects + ", collections=" + collections + ", tags=" + tags
				+ ", flags=" + flags + ", fieldUrls=" + fieldUrls
				+ ", isUkHosting=" + isUkHosting + ", isTopLevelDomain="
				+ isTopLevelDomain + ", isUkRegistration=" + isUkRegistration
				+ ", isInScopeIp=" + isInScopeIp
				+ ", isInScopeIpWithoutLicense=" + isInScopeIpWithoutLicense
				+ ", crawlStartDate=" + crawlStartDate + ", crawlEndDate="
				+ crawlEndDate + ", legacySiteId=" + legacySiteId + ", active="
				+ active + ", whiteList=" + whiteList + ", blackList="
				+ blackList + ", dateOfPublication=" + dateOfPublication
				+ ", justification=" + justification + ", selectorNotes="
				+ selectorNotes + ", archivistNotes=" + archivistNotes
				+ ", selectionType=" + selectionType + ", flagNotes="
				+ flagNotes + ", tabStatus=" + tabStatus + ", description="
				+ description + ", ukPostalAddressUrl=" + ukPostalAddressUrl
				+ ", keywords=" + keywords + ", synonyms="
				+ synonyms + ", value=" + value + ", summary=" + summary
				+ ", scope=" + scope + ", depth=" + depth
				+ ", viaCorrespondence=" + viaCorrespondence
				+ ", ukPostalAddress=" + ukPostalAddress + ", crawlFrequency="
				+ crawlFrequency + ", specialDispensation="
				+ specialDispensation + ", specialDispensationReason="
				+ specialDispensationReason + ", liveSiteStatus="
				+ liveSiteStatus + ", wctId=" + wctId + ", sptId=" + sptId
				+ ", noLdCriteriaMet=" + noLdCriteriaMet + ", hidden="+ hidden + ",keySite="
				+ keySite + ", professionalJudgement=" + professionalJudgement
				+ ", professionalJudgementExp=" + professionalJudgementExp
				+ ", ignoreRobotsTxt=" + ignoreRobotsTxt + ", format=" + format
				+ ", field_uk_domain=" + field_uk_domain + ", field_uk_geoip="
				+ field_uk_geoip + ", field_crawl_permission="
				+ field_crawl_permission + ", field_url=" + field_url
				+ ", field_subject=" + field_subject + ", field_description="
				+ field_description + ", field_uk_postal_address_url="
				+ field_uk_postal_address_url
				+ ", field_nominating_organisation="
				+ field_nominating_organisation
				+ ", field_suggested_collections="
				+ field_suggested_collections + ", field_collections="
				+ field_collections + ", field_crawl_start_date="
				+ field_crawl_start_date + ", field_crawl_end_date="
				+ field_crawl_end_date + ", field_license=" + field_license
				+ ", field_instances=" + field_instances
				+ ", field_collection_categories="
				+ field_collection_categories + ", field_qa_status="
				+ field_qa_status + ", field_snapshots=" + field_snapshots
				+ ", field_notes=" + field_notes + ", title=" + title
				+ ", edit_url=" + edit_url + ", language=" + language
				+ ", revision=" + revision + ", id=" + id
				+ ", url=" + url + "]";
	}
	
	
}
