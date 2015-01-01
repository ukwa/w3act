package models;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import scala.NotImplementedError;
import uk.bl.Const;
import uk.bl.api.FormHelper;
import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Target entity managed by Ebean
 */
@Entity
@Table(name = "target")
public class Target extends UrlModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8283372689443804260L;

	@JsonIgnore
	@Column(columnDefinition = "text")
	public String originatingOrganisation;
	
	@JsonIgnore
	@OneToMany(mappedBy = "target", cascade = CascadeType.PERSIST)
	public List<CrawlPermission> crawlPermissions;

	@JsonIgnore
	@OneToMany(mappedBy = "target", cascade = CascadeType.PERSIST)
	public List<Instance> instances;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "license_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "license_id", referencedColumnName="id") }) 
	public List<License> licenses;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = "subject_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "subject_id", referencedColumnName="id") }) 
	public List<Subject> subjects;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = "collection_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") }) 
	public List<Collection> collections;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "tag_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") }) 
	public List<Tag> tags;
	
	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "flag_target", joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "flag_id", referencedColumnName="id") }) 
    public List<Flag> flags;

	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<FieldUrl> fieldUrls;

	@Column(columnDefinition = "text")
	public String description;
	
	@JsonIgnore
	public Boolean isInScopeIp;
	
	@JsonIgnore
	public Boolean isInScopeIpWithoutLicense;

	public Boolean active; // flag for the latest version of the target among
							// targets with the same URL
	
	@Column(columnDefinition = "text")
	public String flagNotes;
	/**
	 * This field comprises the current tab name for view and edit pages.
	 */
	public String tabStatus;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String value;
	
	@Column(columnDefinition = "text")
	public String summary;

	@JsonProperty("field_special_dispensation")
	public Boolean specialDispensation;

	@Column(columnDefinition = "text")
	@JsonProperty("field_special_dispensation_reaso")
	public String specialDispensationReason;
	
	@JsonProperty("field_uk_hosting")
	public Boolean isUkHosting = Boolean.FALSE;
	
	public Boolean isTopLevelDomain = Boolean.FALSE;
	public Boolean isUkRegistration = Boolean.FALSE;
	
	@JsonProperty("field_live_site_status")
	public String liveSiteStatus;

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
	
	@JsonIgnore
	@ManyToOne
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
	public Boolean ukPostalAddress = Boolean.FALSE;

	@Column(columnDefinition = "text")
	public String ukPostalAddressUrl;
	
	@JsonProperty("field_via_correspondence")
	public Boolean viaCorrespondence = Boolean.FALSE;
	
	@JsonProperty("field_professional_judgement")
	public Boolean professionalJudgement = Boolean.FALSE;

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

	public Date crawlStartDate;
	public Date crawlEndDate;
	
	public String whiteList; // regex for white list URLs
	public String blackList; // regex for black list URLs

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
	
	@Required(message="Subjects Required")
	@Transient
	public String subjectSelect;

	@Transient
	public String collectionSelect;

	@Transient
	public String authorIdText;
	
//	public String title;
//	
//
//	@Column(columnDefinition = "text")
//	public String revision;
	
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
	
	
	//
//	/**
//	 * Constructor
//	 * 
//	 * @param title
//	 * @param url
//	 */
//	public Target(String title, String url) {
//		this.title = title;
//		this.url = url;
//	}

	public Target() {
		//
	}
//
    public String validate() {
    	Logger.debug("VALIDATE.....");
        if (title == null) {
            return "Title is blank";
        }
//        if (subjects == null || subjects.isEmpty()) {
//        	return "Subjects is empty";
//        }
        return null;
    }

	public static Model.Finder<Long, Target> find = new Model.Finder<Long, Target>(
			Long.class, Target.class);

	/**
	 * Retrieve targets
	 */
	public static List<Target> findInvolving() {
		Logger.debug("Target.findInvolving()");
		return find.all();
	}

	/**
	 * Retrieve targets
	 */
	public static List<Target> findAll() {
		return find.all();
	}

	/**
	 * This method retrieves all targets for given organisation.
	 * 
	 * @param url
	 * @return
	 */
	public static List<Target> findAllforOrganisation(String url) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true)
				.eq(Const.FIELD_NOMINATING_ORGANISATION, url);
		res = ll.findList();
		return res;
	}

	/**
	 * This method returns all Targets that comprise link to given collection
	 * 
	 * @param collectionUrl
	 *            - The collection identifier
	 * @return Targets list
	 */
	public static List<Target> findAllByCollectionUrl(String collectionUrl) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where().contains(
				Const.FIELD_SUGGESTED_COLLECTIONS, collectionUrl);
		res = ll.findList();
		return res;
	}

	/**
	 * This method returns all Targets that comprise link to given collection
	 * 
	 * @param collectionUrl
	 *            - The collection identifier
	 * @return Targets list
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
	 * Create a new target.
	 */
	public static Target create(String title, String url) {
		Target target = new Target();
		target.title = title;
		target.url = url;
		target.save();
		return target;
	}

	/**
	 * Rename a target
	 */
	public static String rename(Long targetId, String newName) {
		Target target = find.ref(targetId);
		target.title = newName;
		target.update();
		return newName;
	}

	/**
	 * This method translates database view to the HTML view.
	 * 
	 * @return list of Strings
	 */
	public List<String> get_field_list(String fieldName) {
		List<String> res = new ArrayList<String>();
		try {
			res.add(Const.EMPTY);
			Field field = this.getClass().getField(fieldName);
			String content = (String) field.get(this);
			res = Arrays.asList(content.split("\\s*,\\s*"));
		} catch (IllegalArgumentException e) {
			Logger.debug(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.debug(e.getMessage());
		} catch (SecurityException e) {
			Logger.debug(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.debug(e.getMessage());
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
		return res;
	}

	/**
	 * This method computes duplicates for target URLs.
	 * 
	 * @return duplicate count
	 */
//	public int getDuplicateNumber() {
//		int res = 0;
//		ExpressionList<Target> ll = find.fetch("fieldUrls").where().eq("fieldUrls.url", this.fieldUrl());
//		res = ll.findRowCount();
//		return res;
//	}

	/**
	 * This method computes a number of targets per user for given user URL.
	 * 
	 * @return
	 */
	public static int getTargetNumberByCuratorUrl(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("author", url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method is checking whether target id already exists.
	 * 
	 * @param id
	 * @return checking result
	 */
	public static boolean isTargetId(Long id) {
		boolean res = false;
		Target target = find.where().eq(Const.ID, id).findUnique();
		if (target != null) {
			res = true;
		}
		return res;
	}

	/**
	 * This method creates ID for new Target and proves that generated ID does
	 * not exists.
	 * 
	 * @return new Target ID
	 */
	public static Long createId() {
		Long res = Utils.INSTANCE.createId();
		if (Target.isTargetId(res)) {
			Logger.debug("Target with nid " + res + " already exists.");
			res = createId();
		}
		return res;
	}

	/**
	 * This method computes a number of targets per taxonomy for given taxonomy
	 * URL.
	 * 
	 * @return
	 */
	public static int getTargetNumberByTaxonomyUrl(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq(
				Const.FIELD_COLLECTION_CATEGORIES, url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets per user for given subject URL.
	 * 
	 * @return
	 */
	public static int getTargetNumberBySubjectUrl(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("field_subject", url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets per organisation for given
	 * organisation URL.
	 * 
	 * @return
	 */
	public static int getTargetNumberByOrganisationUrl(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq(
				"field_nominating_organisation", url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets for given crawl frequency.
	 * 
	 * @return
	 */
	public static int getTargetNumberByCrawlFrequency(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("field_crawl_frequency",
				url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets for given depth.
	 * 
	 * @return
	 */
	public static int getTargetNumberByDepth(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("field_depth", url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets for given license.
	 * 
	 * @return
	 */
	public static int getTargetNumberByLicense(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("field_license", url);
		res = ll.findRowCount();
		return res;
	}

	/**
	 * This method computes a number of targets for given scope.
	 * 
	 * @return
	 */
	public static int getTargetNumberByScope(String url) {
		int res = 0;
		ExpressionList<Target> ll = find.where().eq("field_scope", url);
		res = ll.findRowCount();
		return res;
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
		ExpressionList<Target> ll = find.fetch("fieldUrls").where().eq(Const.ACTIVE, true).contains("fieldUrls.url", url);
		res = ll.findList();
		return res;
	}

	/**
	 * This method filters targets by given User URLs.
	 * 
	 * @return duplicate count
	 */
	public static List<Target> filterUserUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
			ExpressionList<Target> ll = find.where()
					.contains(Const.AUTHOR, url);
			res = ll.findList();
		}
		return res;
	}

	/**
	 * This method filters targets by given Organisation URLs.
	 * 
	 * @return duplicate count
	 */
	public static List<Target> filterOrganisationUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
			ExpressionList<Target> ll = find.where().contains(
					Const.FIELD_NOMINATING_ORGANISATION, url);
			res = ll.findList();
		}
		return res;
	}

	/**
	 * This method filters targets by given Curator and Organisation URLs.
	 * 
	 * @return duplicate count
	 */
	public static List<Target> filterCuratorAndOrganisationUrl(
			String curatorUrl, String organisationUrl) {
		List<Target> res = new ArrayList<Target>();
		if (curatorUrl != null && organisationUrl != null) {
			ExpressionList<Target> ll = find.where().contains(
					"field_nominating_organisation", organisationUrl);
			res = ll.findList();
		}
		return res;
	}

	/**
	 * This method filters targets by given URLs.
	 * 
	 * @return duplicate count
	 */
//	public static List<String> getSubjects() {
//		List<String> subjects = new ArrayList<String>();
//		List<Target> allTargets = find.all();
//		Iterator<Target> itr = allTargets.iterator();
////		while (itr.hasNext()) {
////			Target target = itr.next();
////			if (target.fieldSubject != null && target.fieldSubject.length() > 0
////					&& !subjects.contains(target.fieldSubject)) {
////				ExpressionList<Target> ll = find.where().contains(
////						"field_subject", target.fieldSubject);
////				if (ll.findRowCount() > 0) {
////					subjects.add(target.fieldSubject);
////				}
////			}
////		}
////		return subjects;
//		throw new NotImplementedError();
//	}

	/**
	 * This method retrieves user name for the passed author URL.
	 * 
	 * @return
	 */
	public String get_user_by_id() {
		String res = "";
		try {
			// TODO: KL
			// res = User.findByUrl(author).name;
		} catch (Exception e) {
			Logger.debug("no user found for url: " + this.getAuthor() + ". " + e);
		}
		return res;
	}

	/**
	 * Check by URL if target object exists in database.
	 * 
	 * @param url
	 * @return true if exists
	 */
	public static boolean existInDb(String url) {
		boolean res = false;
		if (url != null) {
			Target resObj = find.where().eq(Const.URL, url)
					.eq(Const.ACTIVE, true).findUnique();
			if (resObj != null) {
				res = true;
			}
		}
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
			ExpressionList<Target> ll = find.where().eq(Const.URL, url);
			res = ll.findList();
		}
		return res;
	}

	public static Target findById(Long id) {	
		return find.where().eq("id", id).findUnique();
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
	@JsonIgnore
	public String checkScopeStr(String fieldUrl, String url) {
		String res = "false";
		if (fieldUrl != null && fieldUrl.length() > 0 && url != null
				&& url.length() > 0 && this.isInScopeAll()) {
			res = "true";
		}
		return res;
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with scope IP.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	@JsonIgnore
	public boolean isInScopeAll() {
		try {
			boolean isInScope = false;
			for (FieldUrl fieldUrl : this.fieldUrls) {
				isInScope = isInScopeIp(fieldUrl.url);
			}
			if (!isInScope) {
				// TODO: KL TO REFACTOR
//				isInScope = isInScopeDomain(url, nidUrl);
			}
			return isInScope;
		} catch (Exception ex) {
			Logger.debug("isInScopeAll() Exception: " + ex);
			return false;
		}
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with scope IP.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	@JsonIgnore
	public boolean isInScopeIp(String url) {
		try {
			for (FieldUrl fieldUrl : this.fieldUrls) {
				return Scope.INSTANCE.checkScopeIp(fieldUrl.url, this);
			}
		} catch (WhoisException ex) {
			Logger.debug("Exception: " + ex);
		}
		return false;
	}
	
	/**
	 * This method checks whether the passed URL is in scope.
	 * 
	 * @param url
	 * @return result as a String
	 */
	public String checkScope(String url) {
		String res = "false";
		if (url.contains(".uk")) {
			res = "true";
		}
		return res;
	}

	/**
	 * This method analyzes manual scope settings for Target with given URL
	 * 
	 * @param url
	 * @return true if one of manual settings is true
	 */
	public boolean checkManualScope() {
		boolean res = false;
		if (BooleanUtils.isTrue(this.ukPostalAddress) || BooleanUtils.isTrue(this.viaCorrespondence) || BooleanUtils.isTrue(this.professionalJudgement)) {
			Logger.debug("checkManualScope(): " + this.ukPostalAddress + ", " + this.viaCorrespondence + ", "+ this.professionalJudgement);
			res = true;
		}
		if (BooleanUtils.isTrue(this.noLdCriteriaMet)) {
			res = false;
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
		try {
			return Scope.INSTANCE.check(url, this);
		} catch (WhoisException ex) {
			Logger.debug("Exception: " + ex);
			return false;
		}
	}

//	public boolean isUkHosting(Target target) {
//		for (FieldUrl fieldUrl : target.fieldUrls) {
//			if (!this.checkGeoIp(fieldUrl.url)) return false;
//		}
//		return true;
//	}

//	/**
//	 * This method checks whether the passed URL is in scope for rules
//	 * associated with scope IP. This check is without license field.
//	 * 
//	 * @param url
//	 *            The search URL
//	 * @param nidUrl
//	 *            The identifier URL in the project domain model
//	 * @return result as a flag
//	 */
//	public boolean isInScopeIpWithoutLicense(String url) {
//		try {
//			return Scope.INSTANCE.checkScopeIpWithoutLicense(url, this);
//		} catch (WhoisException ex) {
//			Logger.debug("Exception: " + ex);
//			return false;
//		}
//	}

	/**
	 * This method checks whether the passed URL is in scope for particular mode
	 * e.g. IP or DOMAIN.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @param mode
	 *            The mode of checking
	 * @return result as a flag
	 */
	public static boolean isInScopeExt(String url, Target target, String mode) {
		try {
			return Scope.INSTANCE.checkExt(url, target, mode);
		} catch (WhoisException ex) {
			Logger.debug("Exception: " + ex);
			return false;
		}
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
	 * This method finds all targets that have higher level domain containing in
	 * their path on order to extend licence obtained for higher level to the
	 * lower levels.
	 */
	public static List<Target> findAllTargetsWithLowerLevel(String target) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where()
				.icontains(Const.FIELD_URL_NODE, target).eq(Const.ACTIVE, true);
		res = ll.findList();
		return res;
	}

//	public String toString() {
//		StringWriter sw = new StringWriter();
//		sw.append(Const.TARGET_DEF);
//		Field[] fields = this.getClass().getFields();
//		for (Field f : fields) {
//			Object value;
//			try {
//				value = f.get(this);
//				String col = "";
//				if (value != null && value.toString() != null) {
//					col = value.toString().replace("\n", "");
//				}
//				sw.append(col);
//				sw.append(Const.CSV_SEPARATOR);
//			} catch (IllegalArgumentException e) {
//				Logger.debug("reflection illegal argument. " + e);
//			} catch (IllegalAccessException e) {
//				Logger.debug("reflection illegal access. " + e);
//			}
//		}
//		sw.append(Const.CSV_LINE_END);
//		return sw.toString();
//	}

	// Could really do with many_to_one relationship
	// TODO: KL
//	public Organisation getOrganisation() {
//		return Organisation.findByUrl(field_no);
//	}

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
	 * @param subject
	 * @return true if in list
	 */
	public boolean hasContactPerson(String curContactPerson) {
		boolean res = false;
		res = Utils.INSTANCE.hasElementInList(curContactPerson, this.authorUser.url);
		return res;
	}

//	/**
//	 * This method returns a list of all scope type values for target record.
//	 * 
//	 * @return
//	 */
//	public static List<String> getAllScopeTypes() {
//		List<String> res = new ArrayList<String>();
//		Const.ScopeType[] resArray = Const.ScopeType.values();
//		for (int i = 0; i < resArray.length; i++) {
//			res.add(resArray[i].name());
//		}
//		return res;
//	}
//
//	/**
//	 * This method returns a list of all depth type values for target record.
//	 * 
//	 * @return
//	 */
//	public static List<String> getAllDepthTypes() {
//		List<String> res = new ArrayList<String>();
//		Const.DepthType[] resArray = Const.DepthType.values();
//		for (int i = 0; i < resArray.length; i++) {
//			res.add(resArray[i].name());
//		}
//		return res;
//	}
//
//	/**
//	 * This method returns a list of all flag values for target record.
//	 * 
//	 * @return
//	 */
//	public static List<String> getAllFlags() {
//		List<String> res = new ArrayList<String>();
//		Const.TargetFlags[] resArray = Const.TargetFlags.values();
//		for (int i = 0; i < resArray.length; i++) {
//			res.add(resArray[i].name());
//		}
//		return res;
//	}

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

		Logger.debug("pageQa() collection: " + collections + ", qaStatus: "
				+ qaIssueId + ", filter: " + filter);

        String collectionSelect = collections.replace("\"", "");
        Logger.debug("collectionSelect: " + collectionSelect);
        List<Long> collectionIds = new ArrayList<Long>();
        if (StringUtils.isNotEmpty(collectionSelect)) {
            String[] collectionArray = collectionSelect.split(", ");
            for (String c : collectionArray) {
            	Long collectionId = Long.valueOf(c);
            	collectionIds.add(collectionId);
            }
        }		     
		
		ExpressionList<Target> results = Target.find.fetch("fieldUrls").fetch("collections").where();
		Page<Target> res = null;
		results = results.eq(Const.ACTIVE, true);
	
		results = results.add(Expr.or(
				Expr.icontains("fieldUrls.url", filter), 
				Expr.icontains("title", filter))
			);
		
		Logger.debug("qaIssueId: " + qaIssueId);
		if (qaIssueId != 0) {
			results = results.eq("qaIssue.id", qaIssueId);
		}

		results = results.in("collections.id", collectionIds);

		results = results.eq("active", true);


		res = results.query().orderBy(sortBy + " " + order).orderBy(sortBy + " " + order).findPagingList(pageSize).setFetchAhead(false).getPage(page);        
        
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
		
		ExpressionList<Target> exp = Target.find.fetch("fieldUrls").fetch("flags").fetch("licenses").fetch("subjects").fetch("collections").where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
	
		exp = exp.add(Expr.or(
				Expr.icontains("fieldUrls.url", filterUrl), 
				Expr.icontains("title", filterUrl))
			);
		
		if (curatorId != -1) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != -1) {
			exp = exp.eq("organisation.id", organisationId);
		}
		if (StringUtils.isNotEmpty(crawlFrequencyName)) {
			exp = exp.eq("crawlFrequency", crawlFrequencyName);
		}
		if (StringUtils.isNotEmpty(depthName)) {
			exp = exp.eq("depth", depthName);
		}
		if (licenseId != -1) {
			exp = exp.eq("licenses.id", flagId);
		}
		if (flagId != -1) {
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
        	List<Long> collectionIds = new ArrayList<Long>();
            String[] collections = collectionSelect.split(", ");
            for (String cId : collections) {
            	Long collectionId = Long.valueOf(cId);
            	collectionIds.add(collectionId);
            }
    		exp = exp.in("collections.id", collectionIds);
        }

		res = exp.query().orderBy(sortBy + " " + order).orderBy("fieldUrls.domain").findPagingList(pageSize).setFetchAhead(false).getPage(page);
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
			Long organisationId, String startDate, String endDate,
			String npld, String crawlFrequencyName, String tld) throws ActException {

		ExpressionList<Target> exp = Target.find.fetch("fieldUrls").fetch("flags").fetch("licenses").fetch("subjects").fetch("collections").where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
		
		Logger.debug("" + curatorId + ", " + organisationId + ", " + startDate + ", " + endDate + ", " + npld + ", " + crawlFrequencyName + ", " + tld);
		if (curatorId != 0) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != 0) {
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

		// new stuff
		if (npld.equals(Const.NpldType.UK_POSTAL_ADDRESS.name())) {
			exp = exp.eq("ukPostalAddress", true);
		} else if (npld.equals(Const.NpldType.VIA_CORRESPONDENCE.name())) {
			exp = exp.eq("viaCorrespondence", true);
		} else if (npld.equals(Const.NpldType.NO_LD_CRITERIA_MET.name())) {
			exp = exp.eq("noLdCriteriaMet", true);
		} else if (npld.equals(Const.NpldType.PROFESSIONAL_JUDGEMENT.name())) {
			exp = exp.eq("professionalJudgement", true);
		} else if (npld.equals(Const.NONE)) {
			exp = exp.eq("ukPostalAddress", false);
			exp = exp.eq("viaCorrespondence", false);
			exp = exp.eq("noLdCriteriaMet", false);
			exp = exp.eq("isUkHosting", false);
			exp = exp.eq("isUkRegistration", false);
			exp = exp.eq("viaCorrespondence", false);
			exp = exp.add(Expr.raw("fieldUrls.url NOT like '%"
					+ Scope.UK_DOMAIN + "%' or fieldUrl NOT like '%"
					+ Scope.LONDON_DOMAIN + "%' or fieldUrl NOT like '%"
					+ Scope.SCOT_DOMAIN + "%'"));
		} else if (npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
			// Expression ex = Expr.or(Expr.icontains("field_url",
			// Scope.UK_DOMAIN), Expr.icontains("field_url",
			// Scope.LONDON_DOMAIN));
			// exp.add(Expr.or(ex, Expr.icontains("field_url",
			// Scope.SCOT_DOMAIN)));
			exp = exp.add(Expr.raw("fieldUrls.url like '%" + Scope.UK_DOMAIN
					+ "%' or fieldUrls.url like '%" + Scope.LONDON_DOMAIN
					+ "%' or fieldUrls.url like '%" + Scope.SCOT_DOMAIN + "%'"));
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
	 * @param subject
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

	/**
	 * This method provides data exports for given crawl-frequency. Method
	 * returns a list of Targets and associated crawl metadata.
	 * 
	 * @param frequency
	 *            The crawl frequency e.g. 'daily'
	 * @return list of Target objects
	 */
	public static List<Target> exportByFrequency(String frequency) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> targets = find.where().eq(Const.ACTIVE, true)
				.icontains(Const.FIELD_CRAWL_FREQUENCY, frequency);
		if (frequency.equals(Const.ALL)) {
			targets = find.where().eq(Const.ACTIVE, true);
		}

		/**
		 * The resulting list should only include those records where there is
		 * specific 'UKWA Licensing' (i.e. where field_license is not empty).
		 */
		Iterator<Target> itr = targets.findList().iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			if (target != null && target.fieldLicense != null
//					&& target.fieldLicense.length() > 0
//					&& !target.fieldLicense.toLowerCase().contains(Const.NONE)) {
//				res.add(target);
//			}
//		}
//		Logger.debug("exportByFrequency() resulting list size: " + res.size());
//		return res;
		throw new NotImplementedError();
	}

	/**
	 * This method provides data exports for given crawl-frequency. Method
	 * returns a list of Targets and associated crawl metadata.
	 * 
	 * @param frequency
	 *            The crawl frequency e.g. 'daily'
	 * @return list of Target objects
	 */
	public static List<Target> exportLdFrequency(String frequency) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> targets = find.where().eq(Const.ACTIVE, true)
				.icontains(Const.FIELD_CRAWL_FREQUENCY, frequency);
		if (frequency.equals(Const.ALL)) {
			targets = find.where().eq(Const.ACTIVE, true);
		}

		/**
		 * The resulting list should only include those records that are in
		 * scope according to InScopeIp and InScopeDomain rules.
		 */
		// TODO: KL TO REFACTOR
//		Iterator<Target> itr = targets.findList().iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			boolean isInScope = isInScopeIp(target.fieldUrl(), target.url);
//			if (!isInScope) {
//				isInScope = isInScopeDomain(target.fieldUrl(), target.url);
//			}
//			if (isInScope) {
//				Logger.debug("add to export ld: " + target);
//				res.add(target);
//			}
//		}
		Logger.debug("exportLdFrequency() resulting list size: " + res.size());
		return res;
	}

	/**
	 * Retrieve a Target by crawl URL.
	 * 
	 * @param url
	 *            The crawl URL
	 * @return target The target object
	 */
	public static Target findByFieldUrl(String url) {
		Target res = new Target();
		Logger.debug("findByFieldUrl() target url: " + url);
		if (url != null) {
			res = find.where().eq(Const.FIELD_URL, url).eq(Const.ACTIVE, true)
					.findUnique();
		}
		return res;
	}

	/**
	 * This method calculates selected flags for presentation in view page.
	 * 
	 * @return flag list as a string
	 */
//	public String getSelectedFlags() {
////		String res = "";
////		boolean firstTime = true;
////		if (this.flags != null) {
////			if (this.flags.contains(Const.LIST_DELIMITER)) {
////				String[] parts = this.flags.split(Const.LIST_DELIMITER);
////				for (String part : parts) {
////					try {
////						if (firstTime) {
////							res = Flags.getGuiName(Flag.findByUrl(part).name);
////							firstTime = false;
////						} else {
////							res = res
////									+ Const.LIST_DELIMITER
////									+ Flags.getGuiName(Flag.findByUrl(part).name);
////						}
////					} catch (Exception e) {
////						Logger.error("getSelectedFlags error: " + e);
////					}
////				}
////			}
////		}
////		if (res.length() == 0) {
////			res = Const.NONE;
////		}
////		return res;
//		throw new NotImplementedError();
//	}

	/**
	 * This method calculates selected tags for presentation in view page.
	 * 
	 * @return tag list as a string
	 */
//	public String getSelectedTags() {
////		String res = "";
////		boolean firstTime = true;
////		if (this.tags != null) {
////			if (this.tags.contains(Const.LIST_DELIMITER)) {
////				String[] parts = this.tags.split(Const.LIST_DELIMITER);
////				for (String part : parts) {
////					try {
////						if (firstTime) {
////							res = Tag.findByUrl(part).name;
////							firstTime = false;
////						} else {
////							res = res + Const.LIST_DELIMITER
////									+ Tag.findByUrl(part).name;
////						}
////					} catch (Exception e) {
////						Logger.error("getSelectedTags error: " + e);
////					}
////				}
////			}
////		}
////		if (res.length() == 0) {
////			res = Const.NONE;
////		}
////		return res;
//		throw new NotImplementedError();
//	}

	/**
	 * This method returns status value as a String
	 * 
	 * @return
	 */
	public String getStatusStr() {
		String res = Const.NA;
		if (status != null) {
			res = Const.statusStrMap.get(Long.valueOf(status));
		}
		return res;
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

	/**
	 * This method returns GUI representation of the date.
	 * 
	 * @param date
	 * @return GUI string
	 */
	public static String getDateAsString(String date) {
		String res = "";
		if (date != null && date.length() > 0) {
			res = Utils.INSTANCE.showTimestampInTable((String) date);
		}
		return res;
	}
	


//	@JsonIgnore
//	public Target getHigherLevelTarget() {
//		// field_url - the domain name
//		// field_license - act-168
//		if (StringUtils.isNotEmpty(this.fieldUrl())) {
//			String normalisedUrl = Scope.INSTANCE.normalizeUrl(this.fieldUrl());
//			String domain = Scope.INSTANCE.getDomainFromUrl(normalisedUrl);
//			ExpressionList<Target> ll = find.where()
//					.icontains(Const.FIELD_URL, domain).eq(Const.ACTIVE, true);
//			List<Target> targets = ll.findList();
//			for (Target target : targets) {
//				if (isHigherLevel(target.fieldUrl())) {
//					return target;
//				}
//			}
//
//		}
//		return null;
//	}
//
//	public static List<Target> findUkwaTargets(String domain, String url) {
//		Logger.debug("Parameters: " + domain + " - " + url.length());
//		String query = "find target fetch fieldUrls fetch licenses where fieldUrls.url like :domain and LENGTH(fieldUrls.url) < :length";
//        List<Target> targets = Ebean.createQuery(Target.class, query)
//        		.setParameter("domain", "%" + domain + "%")
//        		.setParameter("length", url.length()).where().or(Expr.isNotNull("licenses"), Expr.isNotNull("qaIssue")).findList();
//		return targets;
//	}
//	

	public Boolean getIsUkHosting() {
		return isUkHosting;
	}

	public void setIsUkHosting(Boolean isUkHosting) {
		this.isUkHosting = isUkHosting;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
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

	public String getCrawlFrequency() {
		return crawlFrequency;
	}

	public void setCrawlFrequency(String crawlFrequency) {
		this.crawlFrequency = crawlFrequency;
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
		for (FieldUrl fieldUrl : this.fieldUrls) {
			urls.add(fieldUrl.url);
		}
		return StringUtils.join(urls, ", ");
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
		return Scope.INSTANCE.isUkHosting(this);
	}
	
	@JsonIgnore
	public boolean isTopLevelDomain() throws WhoisException, MalformedURLException, URISyntaxException {
		return Scope.INSTANCE.isTopLevelDomain(this);
	}
	
	@JsonIgnore
	public boolean isUkRegistration() throws WhoisException {
		return Scope.INSTANCE.isUkRegistration(this);
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
			dateOfPublicationText = dateFormat.format(dateOfPublication);
		}
		return dateOfPublicationText;
	}

	public String getCrawlStartDateText() {
		if (crawlStartDate != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			crawlStartDateText =  dateFormat.format(crawlStartDate);
		}
		return crawlStartDateText;
	}
	
	public String getCrawlEndDateText() {
		if (crawlEndDate != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			crawlEndDateText = dateFormat.format(crawlEndDate);
		}
		return crawlEndDateText;
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with scope IP. This check is without license field.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	@JsonIgnore
	public boolean isInScopeAllWithoutLicense() {
		boolean isInScope = false;
		try {
			Logger.debug("isInScopeAllWithoutLicense()");
			isInScope = checkScopeIpWithoutLicense(this);
			if (!isInScope) {
				isInScope = Scope.INSTANCE.isTopLevelDomain(this);
			}
		} catch (WhoisException | MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}
		return isInScope;

	}

	@JsonIgnore
	public static boolean checkScopeIpWithoutLicense(Target target) throws WhoisException {
		for (FieldUrl fieldUrl : target.fieldUrls) {
			if (!Scope.INSTANCE.checkScopeIpWithoutLicense(fieldUrl.url, target)) return false;
		}
		return true;
	}

	@JsonIgnore
    public boolean indicateNpldStatus() throws ActException {
    	return (getNpldStatusList().size() > 0);
    }

	/**
	 * This method evaluates the Target list where NPLD status of (i) one or
	 * more of the 'UK Postal Address', 'Via Correspondence', and/or
	 * 'Professional Judgment' fields is not null in any other target record at
	 * a higher level within the same domain AND (ii) where both 'UK hosting'
	 * and 'UK top-level domain' = No.
	 * 
	 * @return target list
	 * @throws ActException 
	 */
	@JsonIgnore
	public List<Target> getNpldStatusList() throws ActException {
		
		List<Target> res = new ArrayList<Target>();
		List<Target> unsorted = new ArrayList<Target>();
		
		List<Target> targets = new ArrayList<Target>();
		
		for (FieldUrl fieldUrl : this.fieldUrls) {
//			String url = this.normalizeUrl(fieldUrl.url);
			String domain = fieldUrl.domain;
			Logger.debug("getNpldStatusList() domain: " + domain);
		
			
			// higher level again
			Set<Target> higherTargets = FormHelper.getHigherTargetsForNpld(fieldUrl);
			targets.addAll(higherTargets);
		}

		Logger.debug("getNpldStatusList() targets list size: " + targets.size());

		/**
		 * Check that UK top level domain is false, one of mentioned flags is
		 * true and the domain is of higher level.
		 */
		for (Target target : targets) {
			
			try {
				if (!target.isUkHosting() && !target.isTopLevelDomain()) {
					unsorted.add(target);
					 if (unsorted.size() == Const.MAX_NPLD_LIST_SIZE) {
						 break;
					 }
				}
			} catch (MalformedURLException | WhoisException | URISyntaxException e) {
				throw new ActException(e);
			}
		}
		
		Logger.debug("getNpldStatusList() targets unsorted result list size: " + unsorted.size());

		/**
		 * Check that UK top level domain is false, one of mentioned flags is
		 * true and the domain is of higher level.
		 */
		for (int i = 0; i < Const.MAX_NPLD_LIST_SIZE; i++) {
			if (i < unsorted.size()) {
				Target target = getLatestCreatedTarget(unsorted);
				if (target != null) {
					res.add(i, target);
					unsorted.remove(target);
				}
			}
		}
		Logger.debug("getNpldStatusList() targets result list size: " + res.size());
		return res;
	}
		
//	/**
//	 * This method evaluates if given current URL has lower level then URL from
//	 * the list.
//	 * 
//	 * @param iterUrl
//	 *            The URL from the list
//	 * @param currentUrl
//	 *            The current URL
//	 * @return
//	 */
//	@JsonIgnore
//	public static boolean isHigherLevel(String iterUrl, String currentUrl) {
//		boolean res = false;
//		if (currentUrl.contains(iterUrl) && currentUrl.indexOf(iterUrl) == 0
//				&& currentUrl.length() > iterUrl.length()) {
//			res = true;
//		}
//		return res;
//	}
//
//	@JsonIgnore
//	public boolean isHigherLevel(String otherUrl) {
//		for (FieldUrl thisFieldUrl : this.fieldUrls) {
//			boolean highLevel = (thisFieldUrl.url.contains(otherUrl) 
//					&& thisFieldUrl.url.indexOf(otherUrl) == 0 
//					&& thisFieldUrl.url.length() > otherUrl.length());
//			// Logger.debug("iterUrl: " + iterUrl + " " + highLevel);
//			return highLevel;
//		}
//		return false;
//	}

	@JsonIgnore
	public boolean hasLicenses() {
		return (this.licenses != null && !this.licenses.isEmpty());
	}

	@JsonIgnore
	public boolean hasHigherLicense() {
		// Open UKWA Licence at higher level - disabled
		// Other license at higher level - disabled
//		Target higherTarget = this.getHigherLevelTarget();
//		if (higherTarget != null) {
//			return (indicateLicenses(higherTarget.fieldLicense));
//		}
		for (FieldUrl fieldUrl : this.fieldUrls) {
			if (FormHelper.getHigherLevelTargetLicense(fieldUrl) != null) {
				return true;
			}
		} 
		return false;
	}

	public boolean indicateLicenses() {
		return (hasLicenses() || hasHigherLicense() || indicateUkwaLicenceStatus());
	}    
    
	public boolean indicateUkwaLicenceStatus() {
		// include what RGRAF implemented
		return this.getUkwaLicenceStatusList().size() > 0;
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
		// Open UKWA Licence at higher level - disabled
		// Open UKWA licence for target being edited - disabled
		
		// url is similar but less in characters
		// has a license
		// has a qaIssue
		Set<Target> results = new LinkedHashSet<Target>();
		// first aggregate a list of active targets for associated URL
			// TODO: KL REDO THIS
//			this.fieldUrl = Scope.normalizeUrl(this.fieldUrl());
		for (FieldUrl thisFieldUrl : this.fieldUrls) {
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
	
	// to helper
	@JsonIgnore
	public boolean hasGrantedLicense() {
		Logger.debug("hasGrantedLicense");
//		if QAStatus is granted 
//		this.crawlPermissions;
//		this.qaIssue;
		for (License license : this.licenses) {
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

	public boolean hasQaIssue() {
		return (this.qaIssue != null);
	}
	
	public static boolean hasQaIssue(Long targetId) {
		Target target = Target.findById(targetId);
		return (target.qaIssue != null);
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
				+ ", noLdCriteriaMet=" + noLdCriteriaMet + ", keySite="
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
				+ ", revision=" + revision + ", vid=" + vid + ", type=" + type
				+ ", status=" + status + ", author=" + author + ", id=" + id
				+ ", url=" + url + "]";
	}
	

}
