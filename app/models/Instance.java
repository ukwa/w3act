package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.ExpressionList;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.IdGenerator;


/**
 * Target instance entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Instance extends Model {

    @Id
    public Long nid;
    @Column(columnDefinition = "TEXT")
    public String value;
    @Column(columnDefinition = "TEXT")
    public String summary;
    public String act_url;
    public String wct_url;
    public String format;
    public String field_scope;
    public String field_depth;
    public String field_via_correspondence;
    public String field_uk_postal_address;
    public String field_uk_hosting;
    public String field_nominating_organisation;
    public String field_crawl_frequency;
    public String field_crawl_start_date;
    public Boolean field_uk_domain;
    public String field_crawl_permission;
    public String field_special_dispensation;
    public Boolean field_uk_geoip;
    public String field_professional_judgement;
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
    public Boolean field_no_ld_criteria_met;
    public String field_key_site;
    @Column(columnDefinition = "TEXT")
    public String field_professional_judgement_exp;
    public String field_ignore_robots_txt;
    public String revision;
    @Column(columnDefinition = "TEXT")
    public String field_qa_issues;
    @Column(columnDefinition = "TEXT")
    public String field_target;
    @Column(columnDefinition = "TEXT")
    public String field_description_of_qa_issues;
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
    public String field_qa_status; 
    @Column(columnDefinition = "TEXT")
    public String qa_status; 
    @Column(columnDefinition = "TEXT")
    public String qa_issue_category; 
    @Column(columnDefinition = "TEXT")
    public String qa_notes; 
    @Column(columnDefinition = "TEXT")
    public String quality_notes; 
    
    /**
     * Constructor
     * @param title
     * @param url
     */
    public Instance(String title, String url) {
    	this.title = title;
    	this.url = url;
    }

    public Instance() {
    }

    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,Instance> find = new Model.Finder(Long.class, Instance.class);
    
    /**
     * Retrieve targets
     */
    public static List<Instance> findInvolving() {
	    return find.all();
	}

    /**
     * Retrieve targets
     */
    public static List<Instance> findAll() {
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
    public static List<Instance> findAllforUser(String url) {
    	List<Instance> res = new ArrayList<Instance>();
        ExpressionList<Instance> ll = find.where().eq("author", url);
        res = ll.findList();
        return res;
	}

    /**
     * This method retrieves all targets for given organisation.
     * @param url
     * @return
     */
    public static List<Instance> findAllforOrganisation(String url) {
    	List<Instance> res = new ArrayList<Instance>();
        ExpressionList<Instance> ll = find.where().eq("field_nominating_organisation", url);
        res = ll.findList();
        return res;
	}

    /**
     * Create a new target.
     */
    public static Instance create(String title, String url) {
        Instance target = new Instance(title, url);
        target.save();
        return target;
    }

   /**
     * Rename a target
     */
    public static String rename(Long targetId, String newName) {
        Instance target = (Instance) find.ref(targetId);
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
        ExpressionList<Instance> ll = find.where().eq("field_url", this.field_url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per user for given user URL.
	 * @return
	 */
	public static int getTargetNumberByCuratorUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("author", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per taxonomy for given taxonomy URL.
	 * @return
	 */
	public static int getTargetNumberByTaxonomyUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_COLLECTION_CATEGORIES, url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per user for given subject URL.
	 * @return
	 */
	public static int getTargetNumberBySubjectUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_subject", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
	public static int getTargetNumberByOrganisationUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_nominating_organisation", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given crawl frequency.
	 * @return
	 */
	public static int getTargetNumberByCrawlFrequency(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_crawl_frequency", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given depth.
	 * @return
	 */
	public static int getTargetNumberByDepth(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_depth", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given license.
	 * @return
	 */
	public static int getTargetNumberByLicense(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_license", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given scope.
	 * @return
	 */
	public static int getTargetNumberByScope(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_scope", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method filters targets by given URLs.
	 * @return duplicate count
	 */
	public static List<Instance> filterUrl(String url) {
		List<Instance> res = new ArrayList<Instance>();
        ExpressionList<Instance> ll = find.where().contains("field_url", url);
    	res = ll.findList();
		return res;
	}
	
	/**
	 * This method filters targets by given User URLs.
	 * @return duplicate count
	 */
	public static List<Instance> filterUserUrl(String url) {
		List<Instance> res = new ArrayList<Instance>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
	        ExpressionList<Instance> ll = find.where().contains(Const.AUTHOR, url);
	    	res = ll.findList();
		}
		return res;
	}
	
	/**
	 * This method filters targets by given Organisation URLs.
	 * @return duplicate count
	 */
	public static List<Instance> filterOrganisationUrl(String url) {
		List<Instance> res = new ArrayList<Instance>();
		if (url == null || url.equals(Const.NONE)) {
			res = find.all();
		} else {
	        ExpressionList<Instance> ll = find.where().contains(Const.FIELD_NOMINATING_ORGANISATION, url);
	    	res = ll.findList();
		}
		return res;
	}
	
	/**
	 * This method filters targets by given Curator and Organisation URLs.
	 * @return duplicate count
	 */
	public static List<Instance> filterCuratorAndOrganisationUrl(String curatorUrl, String organisationUrl) {
		List<Instance> res = new ArrayList<Instance>();
		if (curatorUrl != null && organisationUrl != null) {
	        ExpressionList<Instance> ll = find.where().contains("field_nominating_organisation", organisationUrl);
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
		List<Instance> allTargets = find.all();
		Iterator<Instance> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Instance target = itr.next();
			if (target.field_subject != null && target.field_subject.length() > 0 && !subjects.contains(target.field_subject)) {
		        ExpressionList<Instance> ll = find.where().contains("field_subject", target.field_subject);
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
    public static Instance findByUrl(String url) {
    	Instance res = new Instance();
        Logger.info("target url: " + url);
        
        if (!url.contains(Const.COMMA)) {
	        Instance res2 = find.where().eq(Const.URL, url).findUnique();
	        if (res2 == null) {
	        	res.title = Const.NONE;
	        } else {
	        	res = res2;
	        }
	        Logger.info("target title: " + res.title);
        }
    	return res;
    }          

    /**
     * Retrieve a Target by Id (nid).
     * @param nid
     * @return target 
     */
    public static Instance findById(Long nid) {
        Logger.info("target nid: " + nid);       
        Instance res = find.where().eq(Const.NID, nid).findUnique();
    	return res;
    }          

    public String toString() {
        return "Instance(" + nid + ") with" + " title: " + title  + " url: " + url + ", field_crawl_frequency: " + field_crawl_frequency + ", type: " + type +
        ", field_uk_domain: " + field_uk_domain + ", field_url: " + field_url + 
        ", field_description: " + field_description + ", field_uk_postal_address_url: " + field_uk_postal_address_url +
        ", field_suggested_collections: " + field_suggested_collections + ", field_collections: " + field_collections +
        ", field_license: " + field_license + ", field_collection_categories: " + field_collection_categories +
        ", field_notes: " + field_notes + ", field_instances: " + field_instances + 
        ", field_subject: " + field_subject + ", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

