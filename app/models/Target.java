package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import scala.NotImplementedError;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;
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
	@ManyToOne
	@JoinColumn(name = "qaissue_id")
	public Taxonomy qaIssue;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "author_id")
	public User authorUser;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "organisation_id")
	public Organisation organisation;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "crawlPermission_id")
	public CrawlPermission crawlPermission;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "license_id")
	public List<Taxonomy> licenses;

	@JsonIgnore
	@OneToMany(mappedBy = "target", cascade = CascadeType.PERSIST)
	public List<Instance> instances;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.SUBJECT_TARGET, joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "subject_id", referencedColumnName="id") }) 
	public List<Taxonomy> subjects;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.COLLECTION_TARGET, joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") }) 
	public List<Taxonomy> collections;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.TAG_TARGET, joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="ID") }) 
	public List<Tag> tags;
	
	@JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.FLAG_TARGET, joinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "flag_id", referencedColumnName="id") }) 
    public List<Target> flags;

	@JsonIgnore
	@Required
	@OneToMany(mappedBy = "target", cascade = CascadeType.PERSIST)
	public List<FieldUrl> fieldUrls;

	public Date fieldCrawlStartDate;
	public Date fieldCrawlEndDate;

	public Long legacySiteId;
	
	public Boolean active; // flag for the latest version of the target among
							// targets with the same URL
	public String whiteList; // regex for white list URLs
	public String blackList; // regex for black list URLs
	
	public Date dateOfPublication;
	
	@Column(columnDefinition = "text")
	public String justification;
	
	@Column(columnDefinition = "text")
	public String selectorNotes;
	
	@Column(columnDefinition = "text")
	public String archivistNotes;
	
	@Required
	public String selectionType;
	
	@Column(columnDefinition = "text")
	public String flagNotes;
	/**
	 * This field comprises the current tab name for view and edit pages.
	 */
	public String tabStatus;

	public Boolean isInScopeUkRegistrationValue;
	public Boolean isInScopeDomainValue;
	public Boolean isUkHostingValue;
	public Boolean isInScopeIpValue;
	public Boolean isInScopeIpWithoutLicenseValue;

	@Column(columnDefinition = "text")
	public String domain;
	
	@Column(columnDefinition = "text")
	public String fieldDescription;
	
	@Column(columnDefinition = "text")
	public String fieldUkPostalAddressUrl;

	@Column(columnDefinition = "text")
	public String fieldNotes;

	@Column(columnDefinition = "text")
	public String keywords;
	
	@Column(columnDefinition = "text")
	public String synonyms;
	
	@Column(columnDefinition = "text")
	@JsonProperty
	public String value;
	
	@Column(columnDefinition = "text")
	public String summary;

	@JsonIgnore
	@JsonProperty
	public String field_scope;

	@JsonIgnore
	@JsonProperty
	public String field_depth;
	
	@JsonIgnore
	@JsonProperty
	public Boolean field_via_correspondence;

	@JsonIgnore
	@JsonProperty
	public Boolean field_uk_postal_address;
	
	@JsonProperty
	public Boolean field_uk_hosting;
	
	@JsonProperty
	public String field_crawl_frequency;

	public Boolean fieldUkDomain;
	
	public Boolean fieldUkGeoip;
	
	@JsonProperty
	public Boolean field_special_dispensation;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String field_special_dispensation_reaso;

	@JsonProperty
	public String field_live_site_status;

	@JsonProperty
	public Long field_wct_id;

	@JsonProperty
	public Long field_spt_id;

	@JsonProperty
	public Boolean field_no_ld_criteria_met;

	@JsonProperty
	public Boolean field_key_site;

	@JsonProperty
	public Boolean field_professional_judgement;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String field_professional_judgement_exp;

	@JsonProperty
	public Boolean field_ignore_robots_txt;
	
	@JsonIgnore
	public String format;

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

	//
	/**
	 * Constructor
	 * 
	 * @param title
	 * @param url
	 */
	public Target(String title, String url) {
		this.title = title;
		this.url = url;
	}

	public Target() {
		this.field_via_correspondence = false;
		this.field_uk_postal_address = false;
		this.field_uk_hosting = false;
		this.field_crawl_frequency = "domaincrawl";
		this.fieldUkDomain = true;
//		this.field_uk_domain = "yes";
//		this.field_crawl_permission = "";
		this.field_special_dispensation = false;
		this.fieldUkGeoip = true;
//		this.field_uk_geoip = "yes";
		this.field_professional_judgement = false;
		// this.isNew = false;
//		this.language = "en";
		// this.status = 1L;
//		this.promote = 0L;
//		this.sticky = 0L;
//		this.log = "";
//		this.comment = 0L;
//		this.feed_nid = 0L;
//		this.field_live_site_status = "";
//		this.field_spt_id = 0L;
//		this.field_wct_id = 0L;
		this.field_no_ld_criteria_met = false;
		this.field_key_site = false;
//		this.field_professional_judgement_exp = "";
		this.field_ignore_robots_txt = false;
//		this.fieldUkPostalAddressUrl = "";
//		this.fieldSuggestedCollections = "";
//		this.fieldCollections = "";
//		this.fieldCollectionCategories = "";
//		this.fieldLicense = "";
//		this.fieldNotes = "";
//		this.fieldInstances = "";
//		this.fieldSubject = "";
//		this.value = "";
//		this.summary = "";
//		this.format = "";
		this.field_scope = "root";
		this.field_depth = "capped";
		this.type = Const.URL;
		// this.field_nominating_organisation = Const.NONE;
	}

	// -- Queries

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long, Target> find = new Model.Finder(
			Long.class, Target.class);

	/**
	 * Retrieve targets
	 */
	public static List<Target> findInvolving() {
		Logger.info("Target.findInvolving()");
		return find.all();
	}

	/**
	 * Retrieve targets
	 */
	public static List<Target> findAll() {
		return find.all();
	}

	/**
	 * This method retrieves all targets for given user.
	 * 
	 * @param url
	 * @return
	 */
	public static List<Target> findAllforUser(String url) {
		Logger.info("findAllforUser() url: " + url);
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find.where().eq(Const.AUTHOR, url);
		res = ll.findList();
		Logger.info("findAllforUser() number: " + res.size());
		return res;
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
			ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true)
					.eq(Const.IS_IN_SCOPE_UK_REGISTRATION_VALUE, true);
			count = ll.findRowCount();
		} else {
			ExpressionList<Target> ll = find
					.where()
					.eq(Const.ACTIVE, true)
					.add(Expr.or(
							Expr.eq(Const.IS_IN_SCOPE_UK_REGISTRATION_VALUE,
									false),
							Expr.isNull(Const.IS_IN_SCOPE_UK_REGISTRATION_VALUE)));
			count = ll.findRowCount();
		}
		return count;
	}

	/**
	 * This method retrieves all targets for given collection.
	 * 
	 * @param url
	 * @return
	 */
	public static List<Target> findAllforCollection(String url) {
		List<Target> res = new ArrayList<Target>();
		ExpressionList<Target> ll = find
				.where()
				// .icontains(Const.FIELD_COLLECTION_CATEGORIES, url);
				.eq(Const.ACTIVE, true)
				.add(Expr.or(Expr.eq(Const.FIELD_COLLECTION_CATEGORIES, url),
						Expr.or(Expr.icontains(
								Const.FIELD_COLLECTION_CATEGORIES, url
										+ Const.COMMA), Expr.icontains(
								Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA
										+ " " + url))));

		res = ll.findList();
		return res;
	}

	/**
	 * Create a new target.
	 */
	public static Target create(String title, String url) {
		Target target = new Target(title, url);
		target.save();
		return target;
	}

	/**
	 * Rename a target
	 */
	public static String rename(Long targetId, String newName) {
		Target target = (Target) find.ref(targetId);
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
			Logger.info(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.info(e.getMessage());
		} catch (SecurityException e) {
			Logger.info(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.info(e.getMessage());
		} catch (Exception e) {
			Logger.info(e.getMessage());
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
		Long res = Utils.createId();
		if (Target.isTargetId(res)) {
			Logger.info("Target with nid " + res + " already exists.");
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
	 * This method retrieves value of the list field.
	 * 
	 * @param fieldName
	 * @return list of strings as a String
	 */
	public String get_field_list_as_str(String fieldName) {
		List<String> res = new ArrayList<String>();
		try {
			res.add(Const.EMPTY);
			Field field = this.getClass().getField(fieldName);
			String content = (String) field.get(this);
			res = Arrays.asList(content.split("\\s*,\\s*"));
		} catch (IllegalArgumentException e) {
			Logger.info(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.info(e.getMessage());
		} catch (SecurityException e) {
			Logger.info(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.info(e.getMessage());
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
		String res_str = res.toString().substring(1,
				res.toString().length() - 1);
		if (res_str.length() > Const.STRING_LIMIT) {
			res_str = res_str.toString().substring(0, Const.STRING_LIMIT);
		}
		// System.out.println(res_str.length());
		// String res_str = "test";
		Logger.info("" + res_str);
		return res_str;
	}

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
			Logger.info("no user found for url: " + this.getAuthor() + ". " + e);
		}
		return res;
	}

	/**
	 * Retrieve a Target by URL.
	 * 
	 * @param url
	 * @return target
	 */
	public static Target findByUrl(String url) {
		Target res = new Target();
		// Logger.info("findByUrl() target url: " + url);

		if (!url.contains(Const.COMMA)) {
			// Target res2 = find.where().eq(Const.URL, url).findUnique();
			Target res2 = find.where().eq(Const.URL, url)
					.eq(Const.ACTIVE, true).findUnique();
			if (res2 == null) {
				res.title = Const.NONE;
			} else {
				res = res2;
			}
			// Logger.info("target title: " + res.title);
		}
		return res;
	}

	public static Target findByWct(String url) {
		return find.where().eq("edit_url", url).findUnique();
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
		Target res = new Target();
		Logger.info("findByTarget() target url: " + target);
		try {
			if (!target.contains(Const.COMMA)) {
				Target res2 = find.where().eq(Const.FIELD_URL_NODE, target)
						.eq(Const.ACTIVE, true).findUnique();
				if (res2 == null) {
					res.title = Const.NONE;
					res.url = Const.NONE;
				} else {
					res = res2;
				}
				// Logger.info("target title: " + res.title);
			}
		} catch (Exception e) {
			Logger.info("Target was not found in database.");
			res.title = Const.NONE;
			res.url = Const.NONE;
		}
		return res;
	}

	/**
	 * This method returns previous Target revisions that are not more active
	 * for given URL
	 * 
	 * @param url
	 * @return list of associated Targets
	 */
	public static List<Target> findRevisions(String url) {
		Logger.info("findRevisions() target url: " + url);
		List<Target> res = new ArrayList<Target>();
		if (url != null && url.length() > 0) {
			ExpressionList<Target> ll = find.where().eq(Const.URL, url);
			res = ll.findList();
		}
		return res;
	}

	/**
	 * Retrieve a Target by Id (nid).
	 * 
	 * @param nid
	 * @return target
	 */
	public static Target findById(Long id) {
		Logger.info("target nid: " + id);
		Target res = find.where().eq(Const.ID, id).findUnique();
		return res;
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
				&& url.length() > 0 && Target.isInScopeAll(fieldUrl, url)) {
			res = "true";
		}
		return res;
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
	public static boolean checkManualScope(String url) {
		Target target = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true)
				.findUnique();
		boolean res = false;
		if (target != null
				&& (target.field_uk_postal_address
						|| target.field_via_correspondence || target.field_professional_judgement)) {
			Logger.debug("checkManualScope(): " + target.field_uk_postal_address
					+ ", " + target.field_via_correspondence + ", "
					+ target.field_professional_judgement);
			res = true;
		}
		if (target != null && target.field_no_ld_criteria_met) {
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
	public static boolean checkLicense(String url) {
		Target target = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true)
				.findUnique();
		boolean res = false;
//		if (target != null && target.li != null && target.fieldLicense.length() > 0 && !target.fieldLicense.toLowerCase().contains(Const.NONE)) {
//			res = true;
//		}
//		throw new NotImplementedError();
		return res;
	}

	/**
	 * This method checks whether license for Target with given URL is granted
	 * 
	 * @param url
	 * @return true if license exists
	 */
	public boolean hasGrantedLicense() {
		Logger.info("hasGrantedLicense");
//		if QAStatus is granted 
//		this.crawlPermissions;
//		this.qaIssue;
//	    @if(permission.status.equals("GRANTED")) {
		// TODO: KL check higher level license too
		if (crawlPermission.name.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
			return true;
		}
//		if (this.qaIssue != null && this.qaIssue.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
//			return true;
//		}
		return false;
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
	public static boolean isInScope(String url, String nidUrl) {
		try {
			return Scope.check(url, nidUrl);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
			return false;
		}
	}

	/**
	 * This method checks the passed URL for GeoIp lookup.
	 * 
	 * @param url
	 *            The search URL
	 * @return result as a flag
	 */
	public static boolean checkUkHosting(String url) {
		try {
			return Scope.checkGeoIp(url);
		} catch (Exception ex) {
			Logger.info("Exception: " + ex);
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
	public static boolean isInScopeAll(String url, String nidUrl) {
		try {
			boolean isInScope = isInScopeIp(url, nidUrl);
			if (!isInScope) {
				isInScope = isInScopeDomain(url, nidUrl);
			}
			return isInScope;
		} catch (Exception ex) {
			Logger.info("isInScopeAll() Exception: " + ex);
			return false;
		}
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
	public static boolean isInScopeAllWithoutLicense(String url, String nidUrl) {
		try {
			boolean isInScope = isInScopeIpWithoutLicense(url, nidUrl);
			if (!isInScope) {
				isInScope = isInScopeDomain(url, nidUrl);
			}
			return isInScope;
		} catch (Exception ex) {
			Logger.info("isInScopeAll() Exception: " + ex);
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
	public static boolean isInScopeIp(String url, String nidUrl) {
		try {
			return Scope.checkScopeIp(url, nidUrl);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
			return false;
		}
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
	public static boolean isInScopeIpWithoutLicense(String url, String nidUrl) {
		try {
			return Scope.checkScopeIpWithoutLicense(url, nidUrl);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
			return false;
		}
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with scope domain.
	 * 
	 * @param url
	 *            The search URL
	 * @param nidUrl
	 *            The identifier URL in the project domain model
	 * @return result as a flag
	 */
	public static boolean isInScopeDomain(String url, String nidUrl) {
		
//		@if(Target.isInScopeDomain(instance.target.fieldUrl(), instance.url)) {

		
		try {
			return Scope.checkScopeDomain(url, nidUrl);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
			return false;
		}
	}

	/**
	 * This method checks whether the passed URL is in scope for rules
	 * associated with WhoIs scoping rule.
	 * 
	 * @param url
	 *            The search URL
	 * @return result as a flag
	 */
	public static boolean isInScopeUkRegistration(String url) {
		try {
			return Scope.checkWhois(url);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
			return false;
		}
	}

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
	public static boolean isInScopeExt(String url, String nidUrl, String mode) {
		try {
			return Scope.checkExt(url, nidUrl, mode);
		} catch (WhoisException ex) {
			Logger.info("Exception: " + ex);
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
//				Logger.info("reflection illegal argument. " + e);
//			} catch (IllegalAccessException e) {
//				Logger.info("reflection illegal access. " + e);
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
		res = Utils.hasElementInList(subject, subsubject);
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
		res = Utils.hasElementInList(curContactPerson, this.authorUser.url);
		return res;
	}

	/**
	 * This method returns a list of all language values for target record.
	 * 
	 * @return
	 */
	public static List<String> getAllLanguage() {
		List<String> res = new ArrayList<String>();
		Const.TargetLanguage[] resArray = Const.TargetLanguage.values();
		for (int i = 0; i < resArray.length; i++) {
			res.add(resArray[i].name());
		}
		return res;
	}

	/**
	 * This method returns a list of all NPLD types for target object.
	 * 
	 * @return
	 */
	public static Const.NpldType[] getAllNpldTypes() {
		return Const.NpldType.values();
	}

	/**
	 * This method returns a list of all selection type values for target
	 * record.
	 * 
	 * @return
	 */
	public static List<String> getAllSelectionTypes() {
		List<String> res = new ArrayList<String>();
		Const.SelectionType[] resArray = Const.SelectionType.values();
		for (int i = 0; i < resArray.length; i++) {
			res.add(resArray[i].name());
		}
		return res;
	}

	/**
	 * This method returns a list of all scope type values for target record.
	 * 
	 * @return
	 */
	public static List<String> getAllScopeTypes() {
		List<String> res = new ArrayList<String>();
		Const.ScopeType[] resArray = Const.ScopeType.values();
		for (int i = 0; i < resArray.length; i++) {
			res.add(resArray[i].name());
		}
		return res;
	}

	/**
	 * This method returns a list of all depth type values for target record.
	 * 
	 * @return
	 */
	public static List<String> getAllDepthTypes() {
		List<String> res = new ArrayList<String>();
		Const.DepthType[] resArray = Const.DepthType.values();
		for (int i = 0; i < resArray.length; i++) {
			res.add(resArray[i].name());
		}
		return res;
	}

	/**
	 * This method returns a list of all flag values for target record.
	 * 
	 * @return
	 */
	public static List<String> getAllFlags() {
		List<String> res = new ArrayList<String>();
		Const.TargetFlags[] resArray = Const.TargetFlags.values();
		for (int i = 0; i < resArray.length; i++) {
			res.add(resArray[i].name());
		}
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
			String order, String filter, String collection, String qaStatus) {

		Logger.info("pageQa() collection: " + collection + ", qaStatus: "
				+ qaStatus + ", filter: " + filter);

		return find
				.where(Expr.and(Expr.or(
						Expr.and(Expr.icontains(Const.FIELD_URL, filter),
								Expr.eq(Const.ACTIVE, true)),
						Expr.and(Expr.icontains(Const.TITLE, filter),
								Expr.eq(Const.ACTIVE, true))),
						Expr.and(
								Expr.or(Expr
										.eq(Const.FIELD_QA_STATUS, qaStatus), // equals
																				// 'act-1'
										// like 'act-1,' like ', act-1'
										Expr.or(Expr.startsWith(
												Const.FIELD_QA_STATUS, qaStatus
														+ ","), Expr.endsWith(
												Const.FIELD_QA_STATUS, ",%"
														+ qaStatus))),
								Expr.icontains(
										Const.FIELD_COLLECTION_CATEGORIES,
										collection))))
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
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
	 * @param curatorUrl
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
			String sortBy, String order, String filterUrl, String curatorUrl,
			String organisationUrl, String subjectUrl, String crawlFrequency,
			String depth, String suggested_collections, String license,
			String flag) {
		ExpressionList<Target> exp = Target.find.where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
//		exp = exp.add(Expr.or(Expr.icontains(Const.FIELD_URL, filterUrl), Expr.icontains(Const.TITLE, filterUrl)));
//		if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
////			exp = exp.icontains(Const.AUTHOR, curatorUrl);
//		}
//		if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
////			exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
//		}
//		Logger.debug("pageTargets() subject: " + subjectUrl);
//		if (subjectUrl != null && !subjectUrl.equals(Const.EMPTY)) {
//			if (subjectUrl.toLowerCase().equals(Const.NONE)) {
//				exp = exp.add(Expr.or(
//						Expr.eq(Const.FIELD_SUBJECT, ""),
//						Expr.icontains(Const.FIELD_SUBJECT,
//								subjectUrl.toLowerCase())));
//			} else {
//				exp = exp.icontains(Const.FIELD_SUBJECT, subjectUrl);
//			}
//		}
//		Logger.debug("pageTargets() crawlFrequency: " + crawlFrequency
//				+ ", depth: " + depth + ", license: " + license);
//		if (crawlFrequency != null && !crawlFrequency.equals("")
//				&& !crawlFrequency.toLowerCase().equals(Const.NONE)) {
//			exp = exp.icontains(Const.FIELD_CRAWL_FREQUENCY, crawlFrequency);
//		}
//		if (depth != null && !depth.equals("")
//				&& !depth.toLowerCase().equals(Const.NONE)) {
//			exp = exp.icontains(Const.FIELD_DEPTH, depth);
//		}
//		Logger.debug("pageTargets() suggested_collections: " + suggested_collections);
//		if (suggested_collections != null && !suggested_collections.equals(Const.NONE)) {
//			exp = exp.icontains(Const.FIELD_COLLECTION_CATEGORIES, suggested_collections);
//			// exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS,
//			// suggested_collections);
//		}
//		if (license != null && !license.equals("") && !license.toLowerCase().equals(Const.NONE)) {
//			exp = exp.icontains(Const.FIELD_LICENSE_NODE, license);
//		}
//		if (flag != null && !flag.equals("") && !flag.toLowerCase().equals(Const.NONE)) {
//			exp = exp.icontains(Const.FLAGS, flag);
//		}
		res = exp.query().orderBy(sortBy + " " + order).orderBy(Const.DOMAIN).findPagingList(pageSize).setFetchAhead(false).getPage(page);
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
			String sortBy, String order, String status, String curatorUrl,
			String organisationUrl, String startDate, String endDate,
			String suggested_collections) {
		List<Instance> instanceList = Instance.processReportsQa(status,
				startDate, endDate);
		ExpressionList<Target> exp = Target.find.where();
		Page<Target> res = null;
		exp = exp.eq(Const.ACTIVE, true);
		if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
			// Logger.info("curatorUrl: " + curatorUrl);
			exp = exp.icontains(Const.AUTHOR, curatorUrl);
		}
		if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
			// Logger.info("organisationUrl: " + organisationUrl);
			exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION,
					organisationUrl);
		}
		if (suggested_collections != null
				&& !suggested_collections.equals(Const.NONE)) {
			// Logger.info("suggested_collections: " + suggested_collections);
			exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS,
					suggested_collections);
		}
		List<String> targetUrlCollection = new ArrayList<String>();
		Iterator<Instance> itr = instanceList.iterator();
		while (itr.hasNext()) {
			Instance instance = itr.next();
			if (instance.fieldTarget != null
					&& instance.fieldTarget.length() > 0) {
				// Logger.info("Target.pageReportsQa() instance.field_target: "
				// + instance.field_target);
				targetUrlCollection.add(instance.fieldTarget);
			}
		}
		if (targetUrlCollection.size() > 0) {
			exp = exp.in(Const.URL, targetUrlCollection);
		}
		res = exp.query().orderBy(sortBy + " " + order).orderBy(Const.DOMAIN)
				.findPagingList(pageSize).setFetchAhead(false).getPage(page);
		Logger.info("Expression list for targets size: "
				+ res.getTotalRowCount());
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
	 */
	public static Page<Target> pageReportsCreation(int page, int pageSize,
			String sortBy, String order, String curatorUrl,
			String organisationUrl, String startDate, String endDate,
			String npld, String crawlFrequency, String tld) {

		Logger.info("pageReportsCreation() npld: " + npld
				+ ", crawlFrequency: " + crawlFrequency + ", tld: " + tld);
		ExpressionList<Target> expressionList = Target.find.where();

		Page<Target> res = null;

		expressionList = expressionList.eq(Const.ACTIVE, true);

		if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
			// Logger.info("curatorUrl: " + curatorUrl);
			expressionList = expressionList.icontains(Const.AUTHOR, curatorUrl);
		}
		if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
			// Logger.info("organisationUrl: " + organisationUrl);
			expressionList = expressionList.icontains(
					Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
		}
		if (startDate != null && startDate.length() > 0) {
			Logger.info("startDate: " + startDate);
			String startDateUnix = Utils
					.getUnixDateStringFromDateExt(startDate);
			if (startDateUnix.length() == 9) {
				startDateUnix = "0" + startDateUnix;
			}
			Logger.info("startDateUnix: " + startDateUnix);
			// TODO: UNIX DATE
			expressionList = expressionList.ge(Const.CREATED_AT, startDateUnix);
		}
		if (endDate != null && endDate.length() > 0) {
			Logger.info("endDate: " + endDate);
			String endDateUnix = Utils.getUnixDateStringFromDate(endDate);
			Logger.info("endDateUnix: " + endDateUnix);
			// TODO: UNIX DATE
			expressionList = expressionList.le(Const.CREATED_AT, endDateUnix);
		}
		if (crawlFrequency != null && !crawlFrequency.equals(Const.NONE)) {
			expressionList = expressionList.icontains(
					Const.FIELD_CRAWL_FREQUENCY, crawlFrequency);
		}

		// new stuff
		if (npld.equals(Const.NpldType.UK_POSTAL_ADDRESS.name())) {
			expressionList.eq(Const.FIELD_UK_POSTAL_ADDRESS, true);
		} else if (npld.equals(Const.NpldType.VIA_CORRESPONDENCE.name())) {
			expressionList.eq(Const.FIELD_VIA_CORRESPONDENCE, true);
		} else if (npld.equals(Const.NpldType.NO_LD_CRITERIA_MET.name())) {
			expressionList.eq(Const.FIELD_NO_LD_CRITERIA_MET, true);
		} else if (npld.equals(Const.NpldType.PROFESSIONAL_JUDGEMENT.name())) {
			expressionList.eq(Const.FIELD_PROFESSIONAL_JUDGEMENT, true);
		} else if (npld.equals(Const.NONE)) {
			expressionList.eq(Const.FIELD_UK_POSTAL_ADDRESS, false);
			expressionList.eq(Const.FIELD_VIA_CORRESPONDENCE, false);
			expressionList.eq(Const.FIELD_NO_LD_CRITERIA_MET, false);
			expressionList.eq("isUkHostingValue", false);
			expressionList.eq("isInScopeUkRegistrationValue", false);
			expressionList.eq(Const.FIELD_PROFESSIONAL_JUDGEMENT, false);
			expressionList.add(Expr.raw("fieldUrl NOT like '%"
					+ Scope.UK_DOMAIN + "%' or fieldUrl NOT like '%"
					+ Scope.LONDON_DOMAIN + "%' or fieldUrl NOT like '%"
					+ Scope.SCOT_DOMAIN + "%'"));
		} else if (npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
			// Expression ex = Expr.or(Expr.icontains("field_url",
			// Scope.UK_DOMAIN), Expr.icontains("field_url",
			// Scope.LONDON_DOMAIN));
			// exp.add(Expr.or(ex, Expr.icontains("field_url",
			// Scope.SCOT_DOMAIN)));
			expressionList.add(Expr.raw("fieldUrl like '%" + Scope.UK_DOMAIN
					+ "%' or fieldUrl like '%" + Scope.LONDON_DOMAIN
					+ "%' or fieldUrl like '%" + Scope.SCOT_DOMAIN + "%'"));
		} else if (npld.equals(Const.NpldType.UK_HOSTING.name())) {
			// uk hosting
			expressionList.eq("isUkHostingValue", true);
		} else if (npld.equals(Const.NpldType.UK_REGISTRATION.name())) {
			// uk registration address
			expressionList.eq("isInScopeUkRegistrationValue", true);
		}

		if (tld.equals(Const.NO)) {
			// not a UK top level domain
			expressionList.eq("isInScopeDomainValue", false);
		}
		if (tld.equals(Const.YES)
				|| npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
			// UK top level domain
			expressionList.eq("isInScopeDomainValue", true);
		}
		if (tld.equals(Const.EITHER)) {
			// not a UK top level domain
			// expressionList.eq("isInScopeDomainValue", false);
			// expressionList.eq("isInScopeDomainValue", true);
		}

		// TODO: NONE SELECTED???

		Logger.info("pageReportsCreation() NPLD: " + npld);

		/**
		 * Apply NPLD filters
		 */
		// if (!tld.equals(Const.EITHER)) {
		// Logger.info("pageReportsCreation() Apply NPLD filters");
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
		// Logger.info("pageReportsCreation() tmp list size: " + tmp.size());
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
		// Logger.info("pageReportsCreation() targetUrlCollection size: " +
		// targetUrlCollection.size());
		// expressionList = expressionList.in(Const.URL, targetUrlCollection);
		// }

		Query<Target> query = expressionList.query();

		res = query.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);

		Logger.info("Expression list for targets created size: "
				+ res.getTotalRowCount());
		Logger.info("RAW SQL: " + query.getGeneratedSql());

		// Target target = query.findUnique();
		// Logger.debug(query.getGeneratedSql());

		// Logger.info("pageReportsCreation() tmp list size: " + res.);

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
			String sortBy, String order, String filter, String collection_url) {
		Logger.debug("pageCollectionTargets() collection_url: "
				+ collection_url);

		return find
				.where()
				.add(Expr.or(Expr.icontains(Const.FIELD_URL, filter),
						Expr.icontains(Const.TITLE, filter)))
				.eq(Const.ACTIVE, true)
				.add(Expr.or(Expr.eq(Const.FIELD_COLLECTION_CATEGORIES,
						collection_url), Expr.or(Expr.or(Expr.contains(
						Const.FIELD_COLLECTION_CATEGORIES, collection_url
								+ Const.COMMA), Expr.startsWith(
						Const.FIELD_COLLECTION_CATEGORIES, collection_url
								+ Const.COMMA)), Expr.or(Expr.endsWith(
						Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA + " "
								+ collection_url), Expr.endsWith(
						Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA + "  "
								+ collection_url)))))
				// .icontains(Const.FIELD_COLLECTION_CATEGORIES, collection_url)
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
			String sortBy, String order, String filter, String subject_url) {
		Logger.debug("pageSubjectTargets() subject_url: " + subject_url);

		return find
				.where()
				.add(Expr.or(Expr.icontains(Const.FIELD_URL, filter),
						Expr.icontains(Const.TITLE, filter)))
				.eq(Const.ACTIVE, true)
				.add(Expr.or(
						Expr.eq(Const.FIELD_SUBJECT, subject_url),
						Expr.or(Expr.contains(Const.FIELD_SUBJECT, subject_url
								+ Const.COMMA), Expr.or(
								Expr.contains(Const.FIELD_SUBJECT, Const.COMMA
										+ " " + subject_url),
								Expr.contains(Const.FIELD_SUBJECT, Const.COMMA
										+ "  " + subject_url)))))
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
			String sortBy, String order, String filter, String organisation_url) {
		Logger.debug("pageOrganisationTargets() organisation_url: "
				+ organisation_url);

		return find
				.where()
				.add(Expr.or(Expr.icontains(Const.FIELD_URL, filter),
						Expr.icontains(Const.TITLE, filter)))
				.eq(Const.ACTIVE, true)
				.add(Expr.eq(Const.FIELD_NOMINATING_ORGANISATION,
						organisation_url))
				// .icontains(Const.FIELD_NOMINATING_ORGANISATION,
				// organisation_url)
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
			String sortBy, String order, String filter, String user_url,
			String subject, String collection) {

		return find
				.where()
				.add(Expr.or(Expr.icontains(Const.FIELD_URL, filter),
						Expr.icontains(Const.TITLE, filter)))
				.eq(Const.ACTIVE, true).eq(Const.AUTHOR, user_url)
				.icontains(Const.FIELD_SUBJECT, subject)
				.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection)
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
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
//		Logger.info("exportByFrequency() resulting list size: " + res.size());
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
		Iterator<Target> itr = targets.findList().iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			boolean isInScope = isInScopeIp(target.fieldUrl(), target.url);
			if (!isInScope) {
				isInScope = isInScopeDomain(target.fieldUrl(), target.url);
			}
			if (isInScope) {
				Logger.debug("add to export ld: " + target);
				res.add(target);
			}
		}
		Logger.info("exportLdFrequency() resulting list size: " + res.size());
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
		Logger.info("findByFieldUrl() target url: " + url);
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
	 * This method evaluates the Target list where NPLD status of (i) one or
	 * more of the 'UK Postal Address', 'Via Correspondence', and/or
	 * 'Professional Judgment' fields is not null in any other target record at
	 * a higher level within the same domain AND (ii) where both 'UK hosting'
	 * and 'UK top-level domain' = No.
	 * 
	 * @return target list
	 */
	public static List<Target> getNpldStatusList(String fieldUrl) {
		List<Target> res = new ArrayList<Target>();
		List<Target> unsorted = new ArrayList<Target>();
		List<Target> targets = new ArrayList<Target>();
		Logger.debug("getNpldStatusList() field URL: " + fieldUrl);
		if (fieldUrl != null && fieldUrl.length() > 0) {
			Logger.debug("getNpldStatusList() fieldUrl: " + fieldUrl);
			fieldUrl = Scope.normalizeUrl(fieldUrl);
			String domain = Scope.getDomainFromUrl(fieldUrl);
			Logger.debug("getNpldStatusList() domain: " + domain);
			ExpressionList<Target> ll = find.where()
					.icontains(Const.FIELD_URL, domain)
					.eq(Const.FIELD_UK_HOSTING, false).eq(Const.ACTIVE, true);
			targets = ll.findList();
		}
		Logger.debug("getNpldStatusList() targets list size: " + targets.size());

		/**
		 * Check that UK top level domain is false, one of mentioned flags is
		 * true and the domain is of higher level.
		 */
		Iterator<Target> itr = targets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			
			// TODO: KL WHAT ABOUT THE COMMA SEPARATED URLS?
			if ((target.field_uk_postal_address || target.field_via_correspondence
					|| target.field_professional_judgement || target.field_no_ld_criteria_met)
					&& isHigherLevel(target.fieldUrl(), fieldUrl)
					&& (!checkUkHosting(target.fieldUrl()) && !isInScopeDomain(
							target.fieldUrl(), target.url))) {
				unsorted.add(target);
				// if (unsorted.size() == Const.MAX_NPLD_LIST_SIZE) {
				// break;
				// }
			}
		}
		Logger.debug("getNpldStatusList() targets unsorted result list size: "
				+ unsorted.size());

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
		Logger.debug("getNpldStatusList() targets result list size: "
				+ res.size());
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
		Logger.info("getLatestCreatedTarget() res: " + res);
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
			res = Utils.showTimestampInTable((String) date);
		}
		return res;
	}

	/**
	 * This method evaluates if given current URL has lower level then URL from
	 * the list.
	 * 
	 * @param iterUrl
	 *            The URL from the list
	 * @param currentUrl
	 *            The current URL
	 * @return
	 */
	@JsonIgnore
	public static boolean isHigherLevel(String iterUrl, String currentUrl) {
		boolean res = false;
		if (currentUrl.contains(iterUrl) && currentUrl.indexOf(iterUrl) == 0
				&& currentUrl.length() > iterUrl.length()) {
			res = true;
		}
		return res;
	}

	@JsonIgnore
	public boolean isHigherLevel(String iterUrl) {
		boolean highLevel = (this.fieldUrl().contains(iterUrl)
				&& this.fieldUrl().indexOf(iterUrl) == 0 && this.fieldUrl()
				.length() > iterUrl.length());
		// Logger.info("iterUrl: " + iterUrl + " " + highLevel);
		return highLevel;
	}

	@JsonIgnore
	public boolean validQAStatus(Target target) {
		// Logger.info("validQAStatus field_url: " + target.field_url);
		return (this.qaIssue != null && target.qaIssue.url.length() > 0 && !target.qaIssue.url
				.toLowerCase().equals(Const.NONE));
	}

	@JsonIgnore
	public boolean hasLicenses() {
		// Open UKWA licence for target being edited - disabled
		// Other license for target being edited - disabled
//		return indicateLicenses(this.fieldLicense);
		throw new NotImplementedError();
	}

	@JsonIgnore
	public boolean hasHigherLicense() {
		// Open UKWA Licence at higher level - disabled
		// Other license at higher level - disabled
//		Target higherTarget = this.getHigherLevelTarget();
//		if (higherTarget != null) {
//			return (indicateLicenses(higherTarget.fieldLicense));
//		}
//		return false;
		throw new NotImplementedError();
	}

	public boolean indicateUkwaLicenceStatus() {
		// include what RGRAF implemented
		return this.getUkwaLicenceStatusList().size() > 0;
	}

	private boolean indicateLicenses(String field_license) {
		List<Taxonomy> licenses = Taxonomy.findByType(Const.LICENCE);
		Logger.info("field_license: " + field_license);
		for (Taxonomy taxonomy : licenses) {
			Logger.info("taxonomy ............... " + taxonomy.url);
			if (field_license != null && field_license.contains(taxonomy.url)) {
				return true;
			}
		}
		return false;
	}

	public boolean indicateLicenses() {
		Logger.info("indicateLicenses >>>>>>> " + indicateUkwaLicenceStatus()
				+ " " + hasLicenses() + " " + hasHigherLicense());
		return (indicateUkwaLicenceStatus() || hasLicenses() || hasHigherLicense());
	}

	@JsonIgnore
	public Target getHigherLevelTarget() {
		// field_url - the domain name
		// field_license - act-168
		if (StringUtils.isNotEmpty(this.fieldUrl())) {
			String normalisedUrl = Scope.normalizeUrl(this.fieldUrl());
			String domain = Scope.getDomainFromUrl(normalisedUrl);
			ExpressionList<Target> ll = find.where()
					.icontains(Const.FIELD_URL, domain).eq(Const.ACTIVE, true);
			List<Target> targets = ll.findList();
			for (Target target : targets) {
				if (isHigherLevel(target.fieldUrl())) {
					return target;
				}
			}

		}
		return null;
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
	public List<Target> getUkwaLicenceStatusList() {
		// Open UKWA Licence at higher level - disabled
		// Open UKWA licence for target being edited - disabled
		List<Target> results = new ArrayList<Target>();
		if (StringUtils.isNotEmpty(this.fieldUrl())) {
			// first aggregate a list of active targets for associated URL
			Logger.debug("getUkwaLicenceStatusList() fieldUrl: "
					+ this.fieldUrl());
			// TODO: KL REDO THIS
//			this.fieldUrl = Scope.normalizeUrl(this.fieldUrl());
			String domain = Scope.getDomainFromUrl(this.fieldUrl());
			Logger.debug("getUkwaLicenceStatusList() domain: " + domain);
			// get me Targets that contain the same domain so I can check the
			// licenses. i.e higher level
			ExpressionList<Target> ll = find.where()
					.icontains(Const.FIELD_URL, domain).eq(Const.ACTIVE, true);
			List<Target> targets = ll.findList();

			Logger.info("Targets containing domain " + domain + " - "
					+ targets.size());

			/**
			 * Check that the domain is of higher level.
			 */
			Iterator<Target> itr = targets.iterator();
			while (itr.hasNext()) {
				Target target = itr.next();
				// Then for each target from selected list look if ‘qa_status’
				// field is not empty. If it is not empty then we know a crawl
				// permission request has already been sent.
				// also check if this target has a valid license too
				// Then look if it is a target of a higher level domain
				// analyzing given URL.
				// license field checked as required in issue 176.
				// Logger.info("validQAStatus: " + validQAStatus(target));
				// higher level domain and has a license or higher level domain
				// and has pending qa status
//				if ((isHigherLevel(target.fieldUrl) && StringUtils
//						.isNotBlank(target.fieldLicense))
//						|| (isHigherLevel(target.fieldUrl) && validQAStatus(target))) {
//					results.add(target);
//				}
				throw new NotImplementedError();
			}
			// what about current target license?
		}

		Logger.debug("getUkwaLicenceStatusList() targets result list size: "
				+ results.size());
		return results;
	}

	public String getField_scope() {
		return field_scope;
	}

	public void setField_scope(String field_scope) {
		this.field_scope = field_scope;
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

	public String getField_depth() {
		return field_depth;
	}

	public void setField_depth(String field_depth) {
		this.field_depth = field_depth;
	}

	public Boolean getField_via_correspondence() {
		return field_via_correspondence;
	}

	public void setField_via_correspondence(Boolean field_via_correspondence) {
		this.field_via_correspondence = field_via_correspondence;
	}

	public Boolean getField_uk_postal_address() {
		return field_uk_postal_address;
	}

	public void setField_uk_postal_address(Boolean field_uk_postal_address) {
		this.field_uk_postal_address = field_uk_postal_address;
	}

	public Boolean getField_uk_hosting() {
		return field_uk_hosting;
	}

	public void setField_uk_hosting(Boolean field_uk_hosting) {
		this.field_uk_hosting = field_uk_hosting;
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

	public String getField_crawl_frequency() {
		return field_crawl_frequency;
	}

	public void setField_crawl_frequency(String field_crawl_frequency) {
		this.field_crawl_frequency = field_crawl_frequency;
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

	public Boolean getField_special_dispensation() {
		return field_special_dispensation;
	}

	public void setField_special_dispensation(Boolean field_special_dispensation) {
		this.field_special_dispensation = field_special_dispensation;
	}

	public String getField_special_dispensation_reaso() {
		return field_special_dispensation_reaso;
	}

	public void setField_special_dispensation_reaso(
			String field_special_dispensation_reaso) {
		this.field_special_dispensation_reaso = field_special_dispensation_reaso;
	}

	public FieldModel getField_qa_status() {
		return field_qa_status;
	}

	public void setField_qa_status(FieldModel field_qa_status) {
		this.field_qa_status = field_qa_status;
	}

	public String getField_live_site_status() {
		return field_live_site_status;
	}

	public void setField_live_site_status(String field_live_site_status) {
		this.field_live_site_status = field_live_site_status;
	}

	public Object getField_notes() {
		return field_notes;
	}

	public void setField_notes(Object field_notes) {
		this.field_notes = field_notes;
	}

	public Long getField_wct_id() {
		return field_wct_id;
	}

	public void setField_wct_id(Long field_wct_id) {
		this.field_wct_id = field_wct_id;
	}

	public Long getField_spt_id() {
		return field_spt_id;
	}

	public void setField_spt_id(Long field_spt_id) {
		this.field_spt_id = field_spt_id;
	}

	public List<FieldModel> getField_snapshots() {
		return field_snapshots;
	}

	public void setField_snapshots(List<FieldModel> field_snapshots) {
		this.field_snapshots = field_snapshots;
	}

	public Boolean getField_no_ld_criteria_met() {
		return field_no_ld_criteria_met;
	}

	public void setField_no_ld_criteria_met(Boolean field_no_ld_criteria_met) {
		this.field_no_ld_criteria_met = field_no_ld_criteria_met;
	}

	public Boolean getField_key_site() {
		return field_key_site;
	}

	public void setField_key_site(Boolean field_key_site) {
		this.field_key_site = field_key_site;
	}

	public String getField_uk_geoip() {
		return field_uk_geoip;
	}

	public void setField_uk_geoip(String field_uk_geoip) {
		this.field_uk_geoip = field_uk_geoip;
	}

	public Boolean getField_professional_judgement() {
		return field_professional_judgement;
	}

	public void setField_professional_judgement(
			Boolean field_professional_judgement) {
		this.field_professional_judgement = field_professional_judgement;
	}

	public String getField_professional_judgement_exp() {
		return field_professional_judgement_exp;
	}

	public void setField_professional_judgement_exp(
			String field_professional_judgement_exp) {
		this.field_professional_judgement_exp = field_professional_judgement_exp;
	}

	public Boolean getField_ignore_robots_txt() {
		return field_ignore_robots_txt;
	}

	public void setField_ignore_robots_txt(Boolean field_ignore_robots_txt) {
		this.field_ignore_robots_txt = field_ignore_robots_txt;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String fieldUrl() {
		List<String> urls = new ArrayList<String>();
		for (FieldUrl fieldUrl : this.fieldUrls) {
			urls.add(fieldUrl.url);
		}
		return StringUtils.join(urls, ", ");
	}
	
	@Override
	public String toString() {
		return "Target [organisation=" + organisation + ", authorUser="
				+ authorUser + ", collections=" + collections + ", subjects="
				+ subjects + ", licenses=" + licenses + ", fieldCrawlStartDate="
				+ fieldCrawlStartDate + ", fieldCrawlEndDate="
				+ fieldCrawlEndDate + ", legacySiteId=" + legacySiteId
				+ ", active=" + active + ", whiteList=" + whiteList
				+ ", blackList=" + blackList + ", dateOfPublication="
				+ dateOfPublication + ", justification=" + justification
				+ ", selectorNotes=" + selectorNotes + ", archivistNotes="
				+ archivistNotes + ", selectionType=" + selectionType
				+ ", flagNotes=" + flagNotes + ", tabStatus=" + tabStatus
				+ ", isInScopeUkRegistrationValue="
				+ isInScopeUkRegistrationValue + ", isInScopeDomainValue="
				+ isInScopeDomainValue + ", isUkHostingValue="
				+ isUkHostingValue + ", isInScopeIpValue=" + isInScopeIpValue
				+ ", isInScopeIpWithoutLicenseValue="
				+ isInScopeIpWithoutLicenseValue + ", domain=" + domain
				+ ", fieldDescription=" + fieldDescription
				+ ", fieldUkPostalAddressUrl=" + fieldUkPostalAddressUrl
				+ ", fieldNotes=" + fieldNotes + ", keywords="
				+ keywords + ", tags=" + tags + ", synonyms=" + synonyms
				+ ", flags=" + flags + ", fieldUrl="
				+ fieldUrl() + ", value=" + value + ", summary=" + summary
				+ ", field_scope=" + field_scope + ", field_depth="
				+ field_depth + ", field_via_correspondence="
				+ field_via_correspondence + ", field_uk_postal_address="
				+ field_uk_postal_address + ", field_uk_hosting="
				+ field_uk_hosting + ", field_crawl_frequency="
				+ field_crawl_frequency + ", fieldUkDomain=" + fieldUkDomain
				+ ", fieldUkGeoip=" + fieldUkGeoip
				+ ", field_special_dispensation=" + field_special_dispensation
				+ ", field_special_dispensation_reaso="
				+ field_special_dispensation_reaso
				+ ", field_live_site_status=" + field_live_site_status
				+ ", field_wct_id=" + field_wct_id + ", field_spt_id="
				+ field_spt_id + ", field_no_ld_criteria_met="
				+ field_no_ld_criteria_met + ", field_key_site="
				+ field_key_site + ", field_professional_judgement="
				+ field_professional_judgement
				+ ", field_professional_judgement_exp="
				+ field_professional_judgement_exp
				+ ", field_ignore_robots_txt=" + field_ignore_robots_txt
				+ ", format=" + format + ", field_uk_domain=" + field_uk_domain
				+ ", field_uk_geoip=" + field_uk_geoip
				+ ", field_crawl_permission=" + field_crawl_permission
				+ ", field_url=" + field_url + ", field_subject="
				+ field_subject + ", field_description=" + field_description
				+ ", field_uk_postal_address_url="
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
				+ ", status=" + status + ", id=" + id + ", url=" + url
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
