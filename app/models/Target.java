package models;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.IdGenerator;
import uk.bl.api.Utils;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;

import controllers.Flags;


/**
 * Target entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name = "target")
public class Target extends Model {

    @Required
    @Id
    public Long nid; // Legacy Site ID
    @Column(columnDefinition = "TEXT")
    public String value;
    @Column(columnDefinition = "TEXT")
    public String summary;
    public String format;
    public String field_scope;
    public String field_depth;
    public Boolean field_via_correspondence;
    public Boolean field_uk_postal_address;
    public Boolean field_uk_hosting;
    public String field_nominating_organisation;
    public String field_crawl_frequency;
    public String field_crawl_start_date;
    public Boolean field_uk_domain;
    public String field_crawl_permission;
    public Boolean field_special_dispensation;
    @Column(columnDefinition = "TEXT")
    public String field_special_dispensation_reaso;
    public Boolean field_uk_geoip;
    public Boolean field_professional_judgement;
    public Long vid;
    public Boolean is_new;
    public String type;
    
    @Required
    public String title;
    
    public String language;
    public String url;
    public String edit_url;
    public Long status;
    public Long promote;
    public Long sticky;
    public String created;
    public String changed;
    public String author; 
    public String log;
    public Long comment;
    public Long comment_count;
    public Long comment_count_new;
    public Long feed_nid;
    public String field_crawl_end_date;
    public String field_live_site_status;
    public Long field_wct_id;
    public Long field_spt_id;
    public Long legacy_site_id;
    public Boolean field_no_ld_criteria_met;
    public Boolean field_key_site;
    @Column(columnDefinition = "TEXT")
    public String field_professional_judgement_exp;
    public Boolean field_ignore_robots_txt;
    @Column(columnDefinition = "TEXT")
    public String revision; // revision comment for latest version of the target among targets with the same URL
    public Boolean active; // flag for the latest version of the target among targets with the same URL
    public String white_list; // regex for white list URLs
    public String black_list; // regex for black list URLs
    public String date_of_publication;
    @Column(columnDefinition = "TEXT")
    public String justification; 
    @Column(columnDefinition = "TEXT")
    public String selector_notes; 
    @Column(columnDefinition = "TEXT")
    public String archivist_notes; 
    @Required
    public String selection_type; 
    public String selector;     
    @Column(columnDefinition = "TEXT")
    public String flag_notes;
    /**
     * This field comprises the current tab name for view and edit pages.
     */
    public String tabstatus;
    
    // lists
    @Required
    @Column(columnDefinition = "TEXT")
    public String field_url; 
    @Column(columnDefinition = "TEXT")
    public String domain; 
    @Column(columnDefinition = "TEXT")
    public String field_description; 
    @Column(columnDefinition = "TEXT")
    public String field_uk_postal_address_url; 
    @Column(columnDefinition = "TEXT")
    public String field_suggested_collections; 
    @Column(columnDefinition = "TEXT")
    public String field_collections; 
    @Column(columnDefinition = "TEXT")
    public String field_license; 
    @Column(columnDefinition = "TEXT")
    public String field_collection_categories; 
    @Column(columnDefinition = "TEXT")
    public String field_notes; 
    @Column(columnDefinition = "TEXT")
    public String field_instances; 
    @Required
    @Column(columnDefinition = "TEXT")
    public String field_subject; 
    //@Required
    @Column(columnDefinition = "TEXT")
    public String field_subsubject; 
    @Column(columnDefinition = "TEXT")
    public String keywords; 
    @Column(columnDefinition = "TEXT")
    public String tags; 
    @Column(columnDefinition = "TEXT")
    public String synonyms; 
    @Column(columnDefinition = "TEXT")
    public String originating_organisation; 
    @Column(columnDefinition = "TEXT")
    public String flags; 
    @Column(columnDefinition = "TEXT")
    public String authors; 
    @Column(columnDefinition = "TEXT")
    public String field_qa_status; 
    @Column(columnDefinition = "TEXT")
    public String qa_status; 
    @Column(columnDefinition = "TEXT")
    public String qa_issue_category; 
    @Column(columnDefinition = "TEXT")
    public String qa_notes; 
    @Column(columnDefinition = "TEXT")
    public String quality_notes; 

    
    @Version
    public Timestamp lastUpdate;
    
    /**
     * Constructor
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
    	this.field_crawl_start_date = "";    	
    	this.field_uk_domain = true;    	
    	this.field_crawl_permission = "";    	
    	this.field_special_dispensation = false;
    	this.field_uk_geoip = true;
    	this.field_professional_judgement = false;
    	this.vid = 0L;
    	this.is_new = false;
    	this.language = "en";
    	this.status = 1L;
    	this.promote = 0L;
    	this.sticky = 0L;
    	this.created = "";
    	this.changed = "";
    	this.log = "";
    	this.comment = 0L;
    	this.comment_count = 0L;
    	this.comment_count_new = 0L;
    	this.feed_nid = 0L;
    	this.field_crawl_end_date = "";
    	this.field_live_site_status = "";
    	this.field_spt_id = 0L;
    	this.field_wct_id = 0L;
    	this.field_no_ld_criteria_met = false;
    	this.field_key_site = false;
    	this.field_professional_judgement_exp = "";
    	this.field_ignore_robots_txt = false;
    	this.field_uk_postal_address_url = "";
    	this.field_suggested_collections = "";
    	this.field_collections = "";
    	this.field_license = "";
    	this.field_notes = "";
    	this.field_instances = "";
    	this.field_subject = "";
		this.value = "";
		this.summary = "";
		this.format = "";
		this.field_scope = "root";
		this.field_depth = "capped";
		this.type = Const.URL;
		this.field_collection_categories = "";
//		this.field_nominating_organisation = Const.NONE;
    }

    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,Target> find = new Model.Finder(Long.class, Target.class);
    
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
     * Generate unique nid for target.
     * @return
     */
    public static int generateId() {
    	return IdGenerator.generateUniqueId();
    }
    
    /**
     * This method retrieves all targets for given user.
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
     * @param url
     * @return
     */
    public static List<Target> findAllforOrganisation(String url) {
    	List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true).eq("field_nominating_organisation", url);
        res = ll.findList();
        return res;
	}
    
    /**
     * This method returns all Targets that comprise link to given collection
     * @param collectionUrl - The collection identifier
     * @return Targets list
     */
    public static List<Target> findAllByCollectionUrl(String collectionUrl) {
    	List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().contains(Const.FIELD_SUGGESTED_COLLECTIONS, collectionUrl);
        res = ll.findList();
        return res;
    }

    /**
     * This method retrieves all targets for given collection.
     * @param url
     * @return
     */
    public static List<Target> findAllforCollection(String url) {
    	List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where()//.icontains(Const.FIELD_COLLECTION_CATEGORIES, url);
	        .eq(Const.ACTIVE, true)
			.add(Expr.or(
	                Expr.eq(Const.FIELD_COLLECTION_CATEGORIES, url),
	                Expr.or(
		                    Expr.icontains(Const.FIELD_COLLECTION_CATEGORIES, url + Const.COMMA),
		                    Expr.icontains(Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA + " " + url)
		                 )
	             ));
        
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
	 * @return duplicate count
	 */
	public int getDuplicateNumber() {
		int res = 0;
        ExpressionList<Target> ll = find.where().eq("field_url", this.field_url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per user for given user URL.
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
	 * @param id
	 * @return checking result
	 */
	public static boolean isTargetId(Long id) {
		boolean res = false;
		Target target = find.where().eq(Const.NID, id).findUnique();
		if (target != null) {
			res = true;
		}
		return res;
	}
	
	/**
	 * This method creates ID for new Target and proves that generated ID does not exists.
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
	 * This method computes a number of targets per taxonomy for given taxonomy URL.
	 * @return
	 */
	public static int getTargetNumberByTaxonomyUrl(String url) {
		int res = 0;
        ExpressionList<Target> ll = find.where().eq(Const.FIELD_COLLECTION_CATEGORIES, url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per user for given subject URL.
	 * @return
	 */
	public static int getTargetNumberBySubjectUrl(String url) {
		int res = 0;
        ExpressionList<Target> ll = find.where().eq("field_subject", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
	public static int getTargetNumberByOrganisationUrl(String url) {
		int res = 0;
        ExpressionList<Target> ll = find.where().eq("field_nominating_organisation", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given crawl frequency.
	 * @return
	 */
	public static int getTargetNumberByCrawlFrequency(String url) {
		int res = 0;
        ExpressionList<Target> ll = find.where().eq("field_crawl_frequency", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given depth.
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
	 * @return duplicate count
	 */
	public static List<Target> filterUrl(String url) {
		List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().icontains("field_url", url);
    	res = ll.findList();
		return res;
	}
	
	public static List<Target> filterActiveUrl(String url) {
		List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true).contains("field_url", url);
    	res = ll.findList();
		return res;
	}
	
	/**
	 * This method filters targets by given User URLs.
	 * @return duplicate count
	 */
	public static List<Target> filterUserUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
	        ExpressionList<Target> ll = find.where().contains(Const.AUTHOR, url);
	    	res = ll.findList();
		}
		return res;
	}
	
	/**
	 * This method filters targets by given Organisation URLs.
	 * @return duplicate count
	 */
	public static List<Target> filterOrganisationUrl(String url) {
		List<Target> res = new ArrayList<Target>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
	        ExpressionList<Target> ll = find.where().contains(Const.FIELD_NOMINATING_ORGANISATION, url);
	    	res = ll.findList();
		}
		return res;
	}
	
	/**
	 * This method filters targets by given Curator and Organisation URLs.
	 * @return duplicate count
	 */
	public static List<Target> filterCuratorAndOrganisationUrl(String curatorUrl, String organisationUrl) {
		List<Target> res = new ArrayList<Target>();
		if (curatorUrl != null && organisationUrl != null) {
	        ExpressionList<Target> ll = find.where().contains("field_nominating_organisation", organisationUrl);
	    	res = ll.findList(); 
		}
		return res;
	}
	
	/**
	 * This method filters targets by given URLs.
	 * @return duplicate count
	 */
	public static List<String> getSubjects() {
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_subject != null && target.field_subject.length() > 0 && !subjects.contains(target.field_subject)) {
		        ExpressionList<Target> ll = find.where().contains("field_subject", target.field_subject);
		        if (ll.findRowCount() > 0) {
		        	subjects.add(target.field_subject);
		        }
			}
		}
    	return subjects;
	}
	
	/**
	 * This method retrieves value of the list field.
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
    	String res_str = res.toString().substring(1,res.toString().length()-1);
    	if (res_str.length() > Const.STRING_LIMIT) {
    		res_str = res_str.toString().substring(0,Const.STRING_LIMIT);
    	}
//    	System.out.println(res_str.length());
//		String res_str = "test";
    	return res_str;
    }

	/**
	 * This method retrieves user name for the passed author URL.
	 * @return
	 */
	public String get_user_by_id() {
		String res = "";
		try {
			res = User.findByUrl(author).name;
		} catch (Exception e) {
			Logger.info("no user found for url: " + author + ". " + e);
		}
		return res;
	}
	
    /**
     * Retrieve a Target by URL.
     * @param url
     * @return target 
     */
    public static Target findByUrl(String url) {
    	Target res = new Target();
//        Logger.info("findByUrl() target url: " + url);
        
        if (!url.contains(Const.COMMA)) {
//	        Target res2 = find.where().eq(Const.URL, url).findUnique();
	        Target res2 = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true).findUnique();
	        if (res2 == null) {
	        	res.title = Const.NONE;
	        } else {
	        	res = res2;
	        }
//	        Logger.info("target title: " + res.title);
        }
    	return res;
    }          

    /**
     * Check by URL if target object exists in database.
     * @param url
     * @return true if exists 
     */
    public static boolean existInDb(String url) {
    	boolean res = false;       
        if (url != null) {
	        Target resObj = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true).findUnique();
	        if (resObj != null) {
	        	res = true;
	        }
        }
    	return res;
    }          

    /**
     * Retrieve a Target by target URL.
     * @param target URL
     * @return target object
     */
    public static Target findByTarget(String target) {
    	Target res = new Target();
        Logger.info("findByTarget() target url: " + target);
        
        if (!target.contains(Const.COMMA)) {
	        Target res2 = find.where().eq(Const.FIELD_URL_NODE, target).eq(Const.ACTIVE, true).findUnique();
	        if (res2 == null) {
	        	res.title = Const.NONE;
	        } else {
	        	res = res2;
	        }
//	        Logger.info("target title: " + res.title);
        }
    	return res;
    }          

    /**
     * This method returns previous Target revisions that are not more active for given URL
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
     * @param nid
     * @return target 
     */
    public static Target findById(Long nid) {
        Logger.info("target nid: " + nid);       
        Target res = find.where().eq(Const.NID, nid).findUnique();
    	return res;
    }          

	/**
	 * This method checks whether the passed URL is in scope
	 * and presents result as a string in GUI. 
	 * @param fieldUrl The field URL
	 * @param url The identification URL
	 * @return result as a String
	 */
	public String checkScopeStr(String fieldUrl, String url) {
		String res = "false";
		if (fieldUrl != null && fieldUrl.length() > 0 
				&& url != null && url.length() > 0
				&& Target.isInScopeAll(fieldUrl, url)) {
			res = "true";
		}
    	return res;
    }

	/**
	 * This method checks whether the passed URL is in scope. 
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
     * @param url
     * @return true if one of manual settings is true
     */
    public static boolean checkManualScope(String url) {
        Target target = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true).findUnique();
        boolean res = false;  
        if (target != null
        		&& (target.field_uk_postal_address 
        		|| target.field_via_correspondence
        		|| target.field_professional_judgement)) {
        	Logger.debug("checkManualScope(): " + target.field_uk_postal_address + ", " + 
        		target.field_via_correspondence + ", " + target.field_professional_judgement);
        	res = true;
        }
        if (target != null && target.field_no_ld_criteria_met) {
        	res = false;
        }
        return res;
        }

    /**
     * This method checks license for Target with given URL
     * @param url
     * @return true if license exists
     */
    public static boolean checkLicense(String url) {
        Target target = find.where().eq(Const.URL, url).eq(Const.ACTIVE, true).findUnique();
        boolean res = false;  
        if (target != null 
        		&& target.field_license != null 
        		&& target.field_license.length() > 0 
        		&& !target.field_license.toLowerCase().contains(Const.NONE)) {
        	res = true;
        }
        return res;
        }

	/**
	 * This method checks whether the passed URL is in scope.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
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
	 * @param url The search URL
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
	 * This method checks whether the passed URL is in scope for
	 * rules associated with scope IP.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
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
	 * This method checks whether the passed URL is in scope for
	 * rules associated with scope IP.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
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
	 * This method checks whether the passed URL is in scope for
	 * rules associated with scope domain.
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @return result as a flag
	 */
    public static boolean isInScopeDomain(String url, String nidUrl) {
    	try {
    		return Scope.checkScopeDomain(url, nidUrl);
    	} catch (WhoisException ex) {
    		Logger.info("Exception: " + ex);
    		return false;
    	}
    }
    
	/**
	 * This method checks whether the passed URL is in scope for
	 * rules associated with WhoIs scoping rule.
	 * @param url The search URL
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
	 * @param url The search URL
	 * @param nidUrl The identifier URL in the project domain model
	 * @param mode The mode of checking
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
     * @return
     */
    public static List<Target> findAllActive() {
		List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().eq(Const.ACTIVE, true);
    	res = ll.findList(); 
		return res;
    }          

    /**
     * This method finds all targets that have higher level domain containing in
     * their path on order to extend licence obtained for higher level to the lower levels.
     */
    public static List<Target> findAllTargetsWithLowerLevel(String target) {
		List<Target> res = new ArrayList<Target>();
        ExpressionList<Target> ll = find.where().icontains(Const.FIELD_URL_NODE, target).eq(Const.ACTIVE, true);
    	res = ll.findList(); 
		return res;
    }          

    public String toString() {
        StringWriter sw = new StringWriter();
		sw.append(Const.TARGET_DEF);
		Field[] fields = this.getClass().getFields();
		for (Field f : fields) {
			Object value;
			try {
				value = f.get(this);
				String col = "";
				if (value != null && value.toString() != null) {
					col = value.toString().replace("\n", "");
				}
	    		sw.append(col);
		 	    sw.append(Const.CSV_SEPARATOR);
			} catch (IllegalArgumentException e) {
				Logger.info("reflection illegal argument. " + e);
			} catch (IllegalAccessException e) {
				Logger.info("reflection illegal access. " + e);
			}
		}
 	    sw.append(Const.CSV_LINE_END);
 	    return sw.toString();

//        return "Target(" + nid + ") with" + " title: " + title  + " url: " + url + ", field_crawl_frequency: " + field_crawl_frequency + ", type: " + type +
//        ", field_uk_domain: " + field_uk_domain + ", field_url: " + field_url + 
//        ", field_description: " + field_description + ", field_uk_postal_address_url: " + field_uk_postal_address_url +
//        ", field_suggested_collections: " + field_suggested_collections + ", field_collections: " + field_collections +
//        ", field_license: " + field_license + ", field_collection_categories: " + field_collection_categories +
//        ", field_notes: " + field_notes + ", field_instances: " + field_instances + 
//        ", field_subject: " + field_subject + ", format: " + format + ", summary: " + summary + ", value: " + value;
    }

    // Could really do with many_to_one relationship
    public Organisation getOrganisation() {
    	return Organisation.findByUrl(field_nominating_organisation);
    }

    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasSubject(String subject) {
    	boolean res = false;
    	res = Utils.hasElementInList(subject, field_subject);
    	return res;
    }
        
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasSubSubject(String subject) {
    	boolean res = false;
    	res = Utils.hasElementInList(subject, field_subsubject);
    	return res;
    }
        
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public static boolean hasSubSubject(String subsubject, String subject) {
    	boolean res = false;
    	res = Utils.hasElementInList(subject, subsubject);
    	return res;
    }
        
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param license
     * @return true if in list
     */
    public boolean hasLicense(String license) {
    	boolean res = false;
    	res = Utils.hasElementInList(license, field_license);
    	return res;
    }
        
    /**
     * This method evaluates if a collection is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasCollection(String collection) {
    	boolean res = false;
    	res = Utils.hasElementInList(collection, field_suggested_collections);
    	return res;
    }
    
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasContactPerson(String curContactPerson) {
    	boolean res = false;
    	res = Utils.hasElementInList(curContactPerson, authors);
    	return res;
    }
    
    /**
     * This method returns a list of all language values for target record.
     * @return
     */
    public static List<String> getAllLanguage() {
    	List<String> res = new ArrayList<String>();
	    Const.TargetLanguage[] resArray = Const.TargetLanguage.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method returns a list of all NPLD types for target object.
     * @return
     */
    public static List<String> getAllNpldTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.NpldType[] resArray = Const.NpldType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method returns a list of all selection type values for target record.
     * @return
     */
    public static List<String> getAllSelectionTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.SelectionType[] resArray = Const.SelectionType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method returns a list of all scope type values for target record.
     * @return
     */
    public static List<String> getAllScopeTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.ScopeType[] resArray = Const.ScopeType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method returns a list of all depth type values for target record.
     * @return
     */
    public static List<String> getAllDepthTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.DepthType[] resArray = Const.DepthType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method returns a list of all flag values for target record.
     * @return
     */
    public static List<String> getAllFlags() {
    	List<String> res = new ArrayList<String>();
	    Const.TargetFlags[] resArray = Const.TargetFlags.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Target> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where()
           		.eq(Const.ACTIVE, true)
        		.add(Expr.or(
	                    Expr.icontains(Const.FIELD_URL_NODE, filter),
	                    Expr.icontains(Const.TITLE, filter)
	                 ))
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    

    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Target> pageQa(int page, int pageSize, String sortBy, String order, String filter, 
    		String collection, String qaStatus) {

    	Logger.info("pageQa() collection: " + collection + ", qaStatus: " + qaStatus);

        return find.where(
        		  Expr.and(
        			 Expr.or(
        				Expr.and(Expr.icontains(Const.FIELD_URL_NODE, filter), Expr.eq(Const.ACTIVE, true)),
	                    Expr.and(Expr.icontains(Const.TITLE, filter), Expr.eq(Const.ACTIVE, true))
	                 ),
	                 Expr.and(
		                Expr.icontains(Const.FIELD_QA_STATUS, qaStatus),
		                Expr.icontains(Const.FIELD_COLLECTION_CATEGORIES, collection)
		             )
	               )
        		)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Target objects.
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filterUrl Filter applied on target urls
     * @param curatorUrl Author of the target
     * @param organisationUrl The author's organisation
     * @param subjectUrl Target subject
     * @param crawlFrequency The crawl frequency
     * @param depth The crawl depth
     * @param suggested_collections The associated collection
     * @param license The license name
     * @param pageSize The number of Target entries on the page
     * @param flag The flag assigned by user
     * @return
     */
    public static Page<Target> pageTargets(int page, int pageSize, String sortBy, String order, String filterUrl, 
    		String curatorUrl, String organisationUrl, String subjectUrl, String crawlFrequency, String depth, 
    		String suggested_collections, String license, String flag) {
    	ExpressionList<Target> exp = Target.find.where();
    	Page<Target> res = null;
   		exp = exp.eq(Const.ACTIVE, true);
   		exp = exp.add(Expr.or(
		                    Expr.icontains(Const.FIELD_URL_NODE, filterUrl),
		                    Expr.icontains(Const.TITLE, filterUrl)
		                ));
    	if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
    		exp = exp.icontains(Const.AUTHOR, curatorUrl);
    	}
    	if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
    	} 
    	if (subjectUrl != null && !subjectUrl.equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_SUBJECT, subjectUrl);
    	} 
    	Logger.info("pageTargets() crawlFrequency: " + crawlFrequency + ", depth: " + depth + ", license: " + license);
    	if (crawlFrequency != null && !crawlFrequency.equals("") && !crawlFrequency.toLowerCase().equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_CRAWL_FREQUENCY, crawlFrequency);
    	} 
    	if (depth != null && !depth.equals("") && !depth.toLowerCase().equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_DEPTH, depth);
    	} 
    	if (suggested_collections != null && !suggested_collections.equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, suggested_collections);
    	} 
    	if (license != null && !license.equals("") && !license.toLowerCase().equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_LICENSE_NODE, suggested_collections);
    	} 
    	if (flag != null && !flag.equals("") && !flag.toLowerCase().equals(Const.NONE)) {
    		exp = exp.icontains(Const.FLAGS, flag);
    	} 
    	res = exp.query()
        		.orderBy(sortBy + " " + order)
        		.orderBy(Const.DOMAIN)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    	Logger.info("Expression list size: " + res.getTotalRowCount());
        return res;
    }
        
    /**
     * Return a page of Target objects.
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param status The type of report QA e.g. awaiting QA, with no QA issues...
     * @param curatorUrl
     * @param organisationUrl
     * @param startDate The start date for filtering
     * @param endDate The end date for filtering
     * @param collectionCategoryUrl
     * @return
     */
    public static Page<Target> pageReportsQa(int page, int pageSize, String sortBy, String order, String status, 
    		String curatorUrl, String organisationUrl, String startDate, String endDate, String suggested_collections) {
    	List<Instance> instanceList = Instance.processReportsQa(status, startDate, endDate);
    	ExpressionList<Target> exp = Target.find.where();
    	Page<Target> res = null;
   		exp = exp.eq(Const.ACTIVE, true);
    	if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
//    		Logger.info("curatorUrl: " + curatorUrl);
    		exp = exp.icontains(Const.AUTHOR, curatorUrl);
    	}
    	if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
//    		Logger.info("organisationUrl: " + organisationUrl);
    		exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
    	} 
    	if (suggested_collections != null && !suggested_collections.equals(Const.NONE)) {
//    		Logger.info("suggested_collections: " + suggested_collections);
    		exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, suggested_collections);
    	} 
    	List<String> targetUrlCollection = new ArrayList<String>();
    	Iterator<Instance> itr = instanceList.iterator();
    	while (itr.hasNext()) {
    		Instance instance = itr.next();
    		if (instance.field_target != null && instance.field_target.length() > 0) {
//    			Logger.info("Target.pageReportsQa() instance.field_target: " + instance.field_target);
    			targetUrlCollection.add(instance.field_target);
    		}
    	}
    	if (targetUrlCollection.size() > 0) {
    		exp = exp.in(Const.URL, targetUrlCollection);
    	}
    	res = exp.query()
        		.orderBy(sortBy + " " + order)
        		.orderBy(Const.DOMAIN)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    	Logger.info("Expression list for targets size: " + res.getTotalRowCount());
        return res;
    }
        
    /**
     * Return a page of Target objects.
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param curatorUrl
     * @param organisationUrl
     * @param startDate The start date for filtering
     * @param endDate The end date for filtering
     * @param npld The selection of NPLD scope rule for filtering
     * @param crawlFrequency The crawl frequency value for filtering
     * @param tld The top level domain setting for filtering
     * @return
     */
    public static Page<Target> pageReportsCreation(int page, int pageSize, String sortBy, String order,  
    		String curatorUrl, String organisationUrl, String startDate, String endDate, 
    		String npld, String crawlFrequency, String tld) {
    	Logger.info("pageReportsCreation() npld: " + npld + ", crawlFrequency: " + crawlFrequency + ", tld: " + tld);
    	ExpressionList<Target> exp = Target.find.where();
    	Page<Target> res = null;
   		exp = exp.eq(Const.ACTIVE, true);
    	if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
//    		Logger.info("curatorUrl: " + curatorUrl);
    		exp = exp.icontains(Const.AUTHOR, curatorUrl);
    	}
    	if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
//    		Logger.info("organisationUrl: " + organisationUrl);
    		exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
    	} 
    	if (startDate != null && startDate.length() > 0) {
    		Logger.info("startDate: " + startDate);
        	String startDateUnix = Utils.getUnixDateStringFromDateExt(startDate);
        	Logger.info("startDateUnix: " + startDateUnix);
    		exp = exp.ge(Const.CREATED, startDateUnix);
    	} 
    	if (endDate != null && endDate.length() > 0) {
    		Logger.info("endDate: " + endDate);
        	String endDateUnix = Utils.getUnixDateStringFromDate(endDate);
        	Logger.info("endDateUnix: " + endDateUnix);
    		exp = exp.le(Const.CREATED, endDateUnix);
    	} 
    	if (crawlFrequency != null && !crawlFrequency.equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_CRAWL_FREQUENCY, crawlFrequency);
    	} 
    	
    	/**
    	 * Apply NPLD filters
    	 */
    	if (!tld.equals(Const.EITHER) || !npld.equals(Const.NONE)) {
    		Logger.info("pageReportsCreation() Apply NPLD filters");
        	List<String> targetUrlCollection = new ArrayList<String>();
        	Page<Target> tmp = exp.query()
            		.orderBy(sortBy + " " + order)
            		.findPagingList(pageSize)
            		.setFetchAhead(false)
            		.getPage(page);
    		Logger.info("pageReportsCreation() tmp.getList() size: " + tmp.getList().size());
			Iterator<Target> itr = tmp.getList().iterator();
			while (itr.hasNext()) {
				Target target = itr.next();
		        if (target != null 
		        		&& target.field_url != null 
		        		&& target.field_url.length() > 0 
		        		&& !target.field_url.toLowerCase().contains(Const.NONE)) {
		        	if (tld.equals(Const.NO)) {
		        		boolean isInScope = Target.isInScopeDomain(target.field_url, target.url);
		        		Logger.info("pageReportsCreation() Not UK top level domain isInScope: " + isInScope);
		        		if (!isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (npld.equals(Const.NpldType.UK_HOSTING.name())) {
		        		boolean isInScope = Target.checkUkHosting(target.field_url);
		        		Logger.info("pageReportsCreation() UK Hosting isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (tld.equals(Const.YES) || npld.equals(Const.NpldType.UK_TOP_LEVEL_DOMAIN.name())) {
		        		boolean isInScope = Target.isInScopeDomain(target.field_url, target.url);
		        		Logger.info("pageReportsCreation() UK top level domain isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (npld.equals(Const.NpldType.UK_REGISTRATION.name())) {
		        		boolean isInScope = Target.isInScopeUkRegistration(target.field_url);
		        		Logger.info("pageReportsCreation() UK Registration isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (npld.equals(Const.NpldType.UK_POSTAL_ADDRESS.name())) {
		        		boolean isInScope = target.field_uk_postal_address;
		        		Logger.info("pageReportsCreation() UK Postal Address isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (npld.equals(Const.NpldType.VIA_CORRESPONDENCE.name())) {
		        		boolean isInScope = target.field_via_correspondence;
		        		Logger.info("pageReportsCreation() via correspondence isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        	if (npld.equals(Const.NpldType.NO_LD_CRITERIA_MET.name())) {
		        		boolean isInScope = target.field_no_ld_criteria_met;
		        		Logger.info("pageReportsCreation() no ld criteria met isInScope: " + isInScope);
		        		if (isInScope) {
		    	        	targetUrlCollection.add(target.url);
		        		}
		        	}
		        }
			}
    		Logger.info("pageReportsCreation() targetUrlCollection size: " + targetUrlCollection.size());
    		exp = exp.in(Const.URL, targetUrlCollection);
    	}
    	
    	res = exp.query()
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    	Logger.info("Expression list for targets created size: " + res.getTotalRowCount());
        return res;
    }
        
    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     * @param collection_url Collection where targets search occurs
     * @return
     */
    public static Page<Target> pageCollectionTargets(int page, int pageSize, String sortBy, String order, 
    		String filter, String collection_url) {
    	Logger.debug("pageCollectionTargets() collection_url: " + collection_url);

        return find.where()
        		.add(Expr.or(
	                    Expr.icontains(Const.FIELD_URL_NODE, filter),
	                    Expr.icontains(Const.TITLE, filter)
	                 ))
	            .eq(Const.ACTIVE, true)
        		.add(Expr.or(
	                    Expr.eq(Const.FIELD_COLLECTION_CATEGORIES, collection_url),
	                    Expr.or(
	    	                    Expr.contains(Const.FIELD_COLLECTION_CATEGORIES, collection_url + Const.COMMA),
	    	                    Expr.or(
	    	    	                    Expr.contains(Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA + " " + collection_url),
	    	    	                    Expr.contains(Const.FIELD_COLLECTION_CATEGORIES, Const.COMMA + "  " + collection_url)
	    	    	                 )
	    	                 )
	                 ))
        		//.icontains(Const.FIELD_COLLECTION_CATEGORIES, collection_url)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     * @param organisation_url Organisation where targets search occurs
     * @return
     */
    public static Page<Target> pageOrganisationTargets(int page, int pageSize, String sortBy, String order, 
    		String filter, String organisation_url) {
    	Logger.debug("pageOrganisationTargets() organisation_url: " + organisation_url);

        return find.where()
        		.add(Expr.or(
	                    Expr.icontains(Const.FIELD_URL_NODE, filter),
	                    Expr.icontains(Const.TITLE, filter)
	                 ))
	            .eq(Const.ACTIVE, true)
        		.add(Expr.eq(Const.FIELD_NOMINATING_ORGANISATION, organisation_url))
        		//.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisation_url)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     * @param user_url User for whom targets search occurs
     * @param subject Taxonomy of type subject
     * @param collection Taxonomy of type collection
     * @return
     */
    public static Page<Target> pageUserTargets(int page, int pageSize, String sortBy, String order, 
    		String filter, String user_url, String subject, String collection) {

        return find.where()
        		.add(Expr.or(
	                    Expr.icontains(Const.FIELD_URL_NODE, filter),
	                    Expr.icontains(Const.TITLE, filter)
	                 ))
	            .eq(Const.ACTIVE, true)
        		.eq(Const.AUTHOR, user_url)
        		.icontains(Const.FIELD_SUBJECT, subject)
        		.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection)        		
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
	/**
     * This method provides data exports for given crawl-frequency. 
     * Method returns a list of Targets and associated crawl metadata.
     * @param frequency The crawl frequency e.g. 'daily'
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
		while (itr.hasNext()) {
			Target target = itr.next();
	        if (target != null 
	        		&& target.field_license != null 
	        		&& target.field_license.length() > 0 
	        		&& !target.field_license.toLowerCase().contains(Const.NONE)) {
	        	res.add(target);
	        }
		}
    	Logger.info("exportByFrequency() resulting list size: " + res.size());   	
		return res;
	}
	
	/**
     * This method provides data exports for given crawl-frequency. 
     * Method returns a list of Targets and associated crawl metadata.
     * @param frequency The crawl frequency e.g. 'daily'
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
         * The resulting list should only include those records that
         * are in scope according to InScopeIp and InScopeDomain rules.
         */
		Iterator<Target> itr = targets.findList().iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			boolean isInScope = isInScopeIp(target.field_url, target.url);
			if (!isInScope) {
				isInScope = isInScopeDomain(target.field_url, target.url);
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
     * @param url The crawl URL
     * @return target The target object
     */
    public static Target findByFieldUrl(String url) {
    	Target res = new Target();
        Logger.info("findByFieldUrl() target url: " + url);        
        if (url != null) {
	        res = find.where().eq(Const.FIELD_URL_NODE, url).eq(Const.ACTIVE, true).findUnique();
        }
    	return res;
    }          

    /**
     * This method calculates selected flags for presentation in view page.
     * @return flag list as a string
     */
    public String getSelectedFlags() {
    	String res = "";
    	boolean firstTime = true;
    	if (this.flags != null) {
    		if (this.flags.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = this.flags.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
		    			if (firstTime) {
		    				res = Flags.getGuiName(Flag.findByUrl(part).name);
		    				firstTime = false;
		    			} else {
		    				res = res + Const.LIST_DELIMITER + Flags.getGuiName(Flag.findByUrl(part).name);
		    			}
		    		} catch (Exception e) {
		    			Logger.error("getSelectedFlags error: " + e);
		    		}
		        }
	    	}
    	}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }
    
    /**
     * This method calculates selected tags for presentation in view page.
     * @return tag list as a string
     */
    public String getSelectedTags() {
    	String res = "";
    	boolean firstTime = true;
    	if (this.tags != null) {
    		if (this.tags.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = this.tags.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
		    			if (firstTime) {
		    				res = Tag.findByUrl(part).name;
		    				firstTime = false;
		    			} else {
		    				res = res + Const.LIST_DELIMITER + Tag.findByUrl(part).name;
		    			}
		    		} catch (Exception e) {
		    			Logger.error("getSelectedTags error: " + e);
		    		}
		        }
	    	}
    	}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }
    
    /**
     * This method returns status value as a String
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
	 * This method evaluates the Target list where NPLD status of 
	 * 		(i) one or more of the 'UK Postal Address', 'Via Correspondence', 
	 * 			and/or 'Professional Judgment' fields is not null in any other 
	 *          target record at a higher level within the same domain AND 
	 *      (ii) where both 'UK hosting' and 'UK top-level domain' = No.
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
	        		.icontains(Const.FIELD_URL_NODE, domain)
	        		.eq(Const.FIELD_UK_HOSTING, false)
		    		.eq(Const.ACTIVE, true);
			targets = ll.findList();
		}
		Logger.debug("getNpldStatusList() targets list size: " + targets.size());

		/**
		 * Check that UK top level domain is false, one of mentioned flags is true
		 * and the domain is of higher level.
		 */
		Iterator<Target> itr = targets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if ((target.field_uk_postal_address 
					|| target.field_via_correspondence
					|| target.field_professional_judgement
					|| target.field_no_ld_criteria_met) 
					&& isHigherLevel(target.field_url, fieldUrl)
					&& (!checkUkHosting(target.field_url)
							&& !isInScopeDomain(target.field_url, target.url))) {
				unsorted.add(target);
//				if (unsorted.size() == Const.MAX_NPLD_LIST_SIZE) {
//					break;
//				}
			}
		}
		Logger.debug("getNpldStatusList() targets unsorted result list size: " + unsorted.size());
		
		/**
		 * Check that UK top level domain is false, one of mentioned flags is true
		 * and the domain is of higher level.
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
	
	/**
	 * This method evaluates the latest created target from the passed
	 * unsorted list.
	 * @param unsorted The unsorted list.
	 * @return
	 */
	public static Target getLatestCreatedTarget(List<Target> unsorted) {
		Target res = null;
		long latest = 0L;
		Iterator<Target> itr = unsorted.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.created != null && target.created.length() > 0 && Long.valueOf(target.created) > latest) {
				latest = Long.valueOf(target.created);
				res = target;
			}
		}
		Logger.info("getLatestCreatedTarget() res: " + res);
		return res;
	}
	
	/**
	 * This method returns GUI representation of the date.
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
	 * This method evaluates if given current URL has lower level then
	 * URL from the list.
	 * @param iterUrl The URL from the list
	 * @param currentUrl The current URL
	 * @return
	 */
	public static boolean isHigherLevel(String iterUrl, String currentUrl) {
		boolean res = false;
		if (currentUrl.contains(iterUrl) 
				&& currentUrl.indexOf(iterUrl) == 0
				&& currentUrl.length() > iterUrl.length()) {
			res = true;
		}
		return res;
	}
	    
	/**
	 * This method should give a list of the Target records, which have an Open UKWA 
	 * Licence request in progress for a target at a higher level in the domain. 
	 * [ie. when Open UKWA License Request field = Queued, Pending, Refused, Granted - 
	 * any value except None.
	 * @return target list
	 */
	public static List<Target> getUkwaLicenceStatusList(String fieldUrl) {
		List<Target> res = new ArrayList<Target>();
		List<Target> targets = new ArrayList<Target>();
		Logger.debug("getUkwaLicenceStatusList() field URL: " + fieldUrl);
		if (fieldUrl != null && fieldUrl.length() > 0) {
			Logger.debug("getUkwaLicenceStatusList() fieldUrl: " + fieldUrl);
	        fieldUrl = Scope.normalizeUrl(fieldUrl);
	        String domain = Scope.getDomainFromUrl(fieldUrl);
			Logger.debug("getUkwaLicenceStatusList() domain: " + domain);
	        ExpressionList<Target> ll = find.where()
	        		.icontains(Const.FIELD_URL_NODE, domain)
		    		.eq(Const.ACTIVE, true);
			targets = ll.findList();
		}
		Logger.debug("getUkwaLicenceStatusList() targets list size: " + targets.size());

		/**
		 * Check that no request is in progress and the domain is of higher level.
		 */
		Iterator<Target> itr = targets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
//			Logger.info(
//					"getUkwaLicenceStatusList() check that no request is in progress and the domain is of higher level. qa_status: " + 
//							target.qa_status + ", target.field_url: " + target.field_url + 
//							", isHigherLevel: " + isHigherLevel(target.field_url, fieldUrl));
			if (target.qa_status != null 
					&& target.qa_status.length() > 0 
					&& !target.qa_status.toLowerCase().equals(Const.NONE)
					&& isHigherLevel(target.field_url, fieldUrl)) {
				res.add(target);
			}
		}
		Logger.debug("getUkwaLicenceStatusList() targets result list size: " + res.size());
		return res;
	}
	
}

