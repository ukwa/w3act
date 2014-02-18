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
import javax.persistence.Version;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.IdGenerator;
import uk.bl.api.Utils;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;


/**
 * Target entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Target extends Model {

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
    public Boolean field_uk_geoip;
    public Boolean field_professional_judgement;
    public Long vid;
    public Boolean is_new;
    public String type;
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
    public String selection_type; 
    public String selector;     
    
    // lists
    @Column(columnDefinition = "TEXT")
    public String field_url; 
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
    @Column(columnDefinition = "TEXT")
    public String field_subject; 
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
        ExpressionList<Target> ll = find.where().eq("field_nominating_organisation", url);
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
        ExpressionList<Target> ll = find.where().eq(Const.FIELD_COLLECTION_CATEGORIES, url);
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
	    	res = ll.findList(); // TODO
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
        Logger.info("findByUrl() target url: " + url);
        
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
	 * This method checks whether the passed URL is in scope. TODO boolean
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
        		&& (target.field_uk_domain.booleanValue() == true 
        		|| target.field_uk_postal_address.equals(Const.TRUE) 
        		|| target.field_via_correspondence.equals(Const.TRUE)
        		|| target.field_professional_judgement.equals(Const.TRUE))) {
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
	 * @param url
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

        return find.where().icontains("field_url", filter)
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

//    	Logger.info("pageQa() collection: " + collection);

        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection)
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
     * @param curator Author of the target
     * @param organisation The author's organisation
     * @param subject Target subject
     * @param crawlFrequency The crawl frequency
     * @param depth The crawl depth
     * @param collection The associated collection
     * @param license The license name
     * @return
     */
    public static Page<Target> pageTargets(int page, int pageSize, String sortBy, String order, String filter, 
    		String curator, String organisation, String subject, String crawlFrequency, String depth, String collection, 
    		String license) {

    	Logger.info("crawlFrequency: " + crawlFrequency + ", depth: " + depth + ", license: " + license);
    	if (crawlFrequency != null && crawlFrequency.length() > 0 && crawlFrequency.toLowerCase().equals(Const.NONE)) {
    		crawlFrequency = ""; 
    	}
    	if (depth != null && depth.length() > 0 && depth.toLowerCase().equals(Const.NONE)) {
    		depth = ""; 
    	}
        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.icontains(Const.AUTHOR, curator)
        		.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisation)
        		.icontains(Const.FIELD_SUBJECT, subject)
        		.icontains(Const.FIELD_CRAWL_FREQUENCY, crawlFrequency)
        		.icontains(Const.FIELD_DEPTH, depth)
        		.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection)
        		.icontains(Const.FIELD_LICENSE_NODE, license)
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
     * @param collection_url Collection where targets search occurs
     * @return
     */
    public static Page<Target> pageCollectionTargets(int page, int pageSize, String sortBy, String order, 
    		String filter, String collection_url) {

        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.eq(Const.FIELD_COLLECTION_CATEGORIES, collection_url)
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

        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.eq(Const.AUTHOR, user_url)
        		.icontains(Const.FIELD_SUBJECT, subject)
        		.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection)        		
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
}

