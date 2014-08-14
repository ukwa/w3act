package models;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.QueryIterator;

import controllers.Flags;


/**
 * Instance instance entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name = "instance")
public class Instance extends Model {

    @Required
    @Id
    @Column(name="ID")
    public Long nid;
    
	//bi-directional many-to-one association to Organisation
	@ManyToOne
	@JoinColumn(name="id_organisation")
	public Organisation organisation_to_instance;
    	
	//bi-directional many-to-many association to Subject
	@ManyToMany(mappedBy="instances")
	public List<Taxonomy> subject_to_instance = new ArrayList<Taxonomy>();

	//bi-directional many-to-one association to DCollection
	@ManyToOne
	@JoinColumn(name="id_collection")
	public DCollection collection_to_instance;
	
	//bi-directional many-to-one association to Flag
	@ManyToOne
	@JoinColumn(name="id_flag")
	public Flag flag_to_instance;
    
	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name="id_tag")
	public Tag tag_to_instance;
    
    @Column(columnDefinition = "TEXT")
    public String value;
    @Column(columnDefinition = "TEXT")
    public String summary;
    public String act_url;
    public String wct_url;
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
    public String field_special_dispensation;
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
    public Boolean field_no_ld_criteria_met;
    public Boolean field_key_site;
    @Column(columnDefinition = "TEXT")
    public String field_professional_judgement_exp;
    public Boolean field_ignore_robots_txt;
    public String revision;
    @Column(columnDefinition = "TEXT")
    public String field_qa_issues;
    @Column(columnDefinition = "TEXT")
    public String field_target;
    @Column(columnDefinition = "TEXT")
    public String field_description_of_qa_issues;
    @Column(columnDefinition = "TEXT")
    public String field_timestamp;
    public Boolean field_published;
    public Boolean field_to_be_published;
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
    public Long legacy_site_id;
    public String white_list; // regex for white list URLs
    public String black_list; // regex for black list URLs
    
    @Version
    public Timestamp lastUpdate;
    
    // lists
    @Required
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
    @Required
    @Column(columnDefinition = "TEXT")
    public String field_subject; 
    @Column(columnDefinition = "TEXT")
    public String field_subsubject; 
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

    public Instance() {
    	super();
    }

    /**
     * Constructor
     * @param title
     * @param url
     */
    public Instance(String title, String url) {
    	this.title = title;
    	this.url = url;
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
     * This method returns a list of ids
     * @return
     */
    public static List<Object> findIds() {
	    return find.findIds();
	}

    public static QueryIterator<Instance> getIterator() {
	    return find.findIterate();
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
	public static int getInstanceNumberByCuratorUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("author", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per taxonomy for given taxonomy URL.
	 * @return
	 */
	public static int getInstanceNumberByTaxonomyUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_COLLECTION_CATEGORIES, url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per user for given subject URL.
	 * @return
	 */
	public static int getInstanceNumberBySubjectUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_subject", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
	public static int getInstanceNumberByOrganisationUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_nominating_organisation", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given crawl frequency.
	 * @return
	 */
	public static int getInstanceNumberByCrawlFrequency(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_crawl_frequency", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given depth.
	 * @return
	 */
	public static int getInstanceNumberByDepth(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_depth", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given license.
	 * @return
	 */
	public static int getInstanceNumberByLicense(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("field_license", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given scope.
	 * @return
	 */
	public static int getInstanceNumberByScope(String url) {
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
		List<Instance> allInstances = find.all();
		Iterator<Instance> itr = allInstances.iterator();
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
			if (author.length() > 0) {
				res = User.findByUrl(author).name;
			}
		} catch (Exception e) {
			Logger.info("no user found for url: " + author + ". " + e);
		}
		return res;
	}
	
    /**
     * Retrieve a Instance by URL.
     * @param url
     * @return instance object 
     */
    public static Instance findByUrl(String url) {
    	Instance res = new Instance();
//        Logger.info("instance url: " + url);
        
        if (!url.contains(Const.COMMA)) {
	        Instance res2 = find.where().eq(Const.URL, url).findUnique();
	        if (res2 == null) {
	        	res.title = Const.NONE;
	        } else {
	        	res = res2;
	        }
//	        Logger.info("instance title: " + res.title);
        }
    	return res;
    }          

    /**
     * Retrieve an Instance by timestamp.
     * @param url
     * @return instance object 
     */
    public static Instance findByTimestamp(String timestamp) {
    	Instance res = new Instance();
//        Logger.info("instance timestamp: " + timestamp);
        
		List<Instance> list = new ArrayList<Instance>();
		if (timestamp != null && timestamp.length() > 0) {
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TIMESTAMP, timestamp);
	    	list = ll.findList(); 
		}

		Instance instance = null;
		if (list.size() > 0) {
	        instance = list.get(0);
		}
	//        Instance instance = find.where().eq(Const.FIELD_TIMESTAMP, timestamp).findUnique();
        if (instance == null) {
        	res.url = Const.NONE;
        } else {
        	res = instance;
        }
    	return res;
    }          

    /**
     * Retrieve an Instance by timestamp and target URL.
     * @param url
     * @return instance object 
     */
    public static Instance findByTimestampAndUrl(String timestamp, String url) {
    	Instance res = new Instance();
//        Logger.info("instance timestamp: " + timestamp);
        
		List<Instance> list = new ArrayList<Instance>();
		if (timestamp != null && timestamp.length() > 0 && url != null && url.length() > 0) {
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TIMESTAMP, timestamp).eq(Const.FIELD_TARGET, url);
	    	list = ll.findList(); 
		}

		Instance instance = null;
		if (list.size() > 0) {
	        instance = list.get(0);
		}
        if (instance == null) {
        	res.url = Const.NONE;
        } else {
        	res = instance;
        }
    	return res;
    }          

    /**
     * Retrieve a Instance by Id (nid).
     * @param nid
     * @return target 
     */
    public static Instance findById(Long nid) {
        Logger.info("target nid: " + nid);       
        Instance res = find.where().eq(Const.NID, nid).findUnique();
    	return res;
    }          

	/**
	 * This method computes a number of instances for given target url.
	 * @return
	 */
	public static List<Instance> findAllInstancesByTarget(String url) {
		List<Instance> list = new ArrayList<Instance>();
        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
    	list = ll.findList(); 
		return list;
	}
	
	/**
	 * This method returns a list of instances for given target url.
	 * @return
	 */
	public static int findAllByTarget(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method evaluates the latest timestamp for given target.
	 * @param url
	 * @return timestamp value
	 */
	public static String showLatestTimestamp(String url) {
		String res = "";
		if (url != null && url.length() > 0) {
			List<Instance> instanceList = new ArrayList<Instance>();
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
	        instanceList = ll.findList(); 
	        Iterator<Instance> itr = instanceList.iterator();
//	        Date lastDate = new Date();
	        String lastDate = "";
	        while (itr.hasNext()) {
	        	Instance instance = itr.next();
	        	String curDate = instance.changed;
//	        	String curDate = instance.field_timestamp;
//	        	Date curDate = new Date(instance.field_timestamp);
	        	if (lastDate == null || lastDate.equals("")) {
	        		lastDate = curDate;
	        	}
//	        	Date lastDateTime = new Date(lastDate);
//	        	Date curDateTime = new Date(curDate);
	        	long lastDateTime = Long.parseLong(lastDate);
	        	long curDateTime = Long.parseLong(curDate);
	        	if (curDateTime > lastDateTime) {
	        		lastDate = curDate;
	        	}
	        }
	        res = Utils.getDateFromUnixDate(lastDate);
//	        res = Utils.showTimestamp(lastDate);
		}
		return res;		
	}
	
	/**
	 * This method shows the date in a page.
	 * @param curDate
	 * @return timestamp value
	 */
	public static String showTimestamp(String curDate) {
		String res = "";
		if (curDate != null && curDate.length() > 0) {
			Logger.info("showTimestamp() curDate: " + curDate + ", Utils.getDateFromUnixDate(curDate): " + Utils.getDateFromUnixDate(curDate));
	        res = Utils.getDateFromUnixDate(curDate);
		}
		return res;		
	}
	
	/**
	 * This method evaluates the latest Instance object for given target.
	 * @param url
	 * @return link to the instance
	 */
	public static String getLatestInstance(String url) {
		String res = "";
		String lastDate = "";
		if (url != null && url.length() > 0) {
			List<Instance> instanceList = new ArrayList<Instance>();
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
	        instanceList = ll.findList(); 
	        Iterator<Instance> itr = instanceList.iterator();
	        while (itr.hasNext()) {
	        	Instance instance = itr.next();
	        	String curDate = instance.field_timestamp;
	        	if (lastDate == null || lastDate.equals("")) {
	        		lastDate = curDate;
	        	}
	        	long lastDateTime = Long.parseLong(lastDate);
	        	long curDateTime = Long.parseLong(curDate);
	        	if (curDateTime > lastDateTime) {
	        		lastDate = curDate;
	        	}
	        }
		}
		Instance instance = Instance.findByTimestampAndUrl(lastDate, url);
		res = instance.url;	
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
     * Return a page of Instance
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Instance property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Instance> page(int page, int pageSize, String sortBy, String order, String filter) {

//    	Logger.info("Instnce.page() filter: " + filter);
        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Instance
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Instance property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     * @param targetUrl Filter by target url
     */
    public static Page<Instance> pageByTarget(int page, int pageSize, String sortBy, String order, 
    		String filter, String targetUrl) {

//    	Logger.info("Instnce.pageByTarget() filter: " + filter);
        return find.where().icontains(Const.FIELD_URL_NODE, filter)
        		.eq(Const.FIELD_TARGET, targetUrl)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
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
     * This method returns previous Instance revisions that are not more active for given URL
     * @param url
     * @return list of associated Instances
     */
    public static List<Instance> findRevisions(String url) {
        Logger.info("findRevisions() target url: " + url);
		List<Instance> res = new ArrayList<Instance>();
		if (url != null && url.length() > 0) {
	        ExpressionList<Instance> ll = find.where().eq(Const.URL, url);
	    	res = ll.findList(); 
		}
		return res;
    }          
    
    public Organisation getOrganisation() {
    	return Organisation.findByUrl(field_nominating_organisation);
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
    public static Page<Instance> pageReportsQa(int page, int pageSize, String sortBy, String order, String status, 
    		String curatorUrl, String organisationUrl, String startDate, String endDate, String suggested_collections) {
    	ExpressionList<Instance> exp = Instance.find.where();
    	Page<Instance> res = null;
//    	if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
//    		exp = exp.icontains(Const.AUTHOR, curatorUrl);
//    	}
    	if (startDate != null && startDate.length() > 0) {
    		Logger.info("start_date: " + startDate);
    		String startDateStr = Utils.getUnixDateStringFromDate(startDate);
    		Logger.info("start_date string: " + startDateStr);
    		exp = exp.ge(Const.CHANGED, startDateStr);
    	} 
    	if (endDate != null && endDate.length() > 0) {
    		Logger.info("end_date: " + endDate);
    		String endDateStr = Utils.getUnixDateStringFromDate(endDate);
    		exp = exp.le(Const.CHANGED, endDateStr);
    	} 
    	res = exp.query()
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    	Logger.info("Expression list size: " + res.getTotalRowCount());
        return res;
    }
        	
    /**
     * This method applies filters to the list of reports QA.
     * @param status The status depicts the type of report
     * @param startDate The start date for filtering for field 'changed'
     * @param endDate The end date for filtering for field 'changed'
     * @return
     */
    public static List<Instance> processReportsQa(String status, String startDate, String endDate) {
    	boolean isProcessed = false;
    	ExpressionList<Instance> exp = Instance.find.where();
    	List<Instance> res = new ArrayList<Instance>();
//    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
//    		Logger.info("status: " + status);
//    		exp = exp.eq(Const.STATUS, status);
//    		isProcessed = true;
//    	} 
		if (status != null && status.equals(Const.ReportQaStatusType.WITHQAISSUES.name().toLowerCase())) { 
			exp = exp.eq(Const.QA_STATUS, Const.QAStatusType.ISSUE_NOTED.name());
			isProcessed = true;
    	} 
    	
		if (status != null && status.equals(Const.ReportQaStatusType.WITHNOQAISSUES.name().toLowerCase())) { 
			exp = exp.ne(Const.QA_STATUS, Const.QAStatusType.ISSUE_NOTED.name());
			isProcessed = true;
    	} 
    	
		if (status != null && status.equals(Const.ReportQaStatusType.FAILEDINSTANCES.name().toLowerCase())) { 
			exp = exp.ne(Const.QA_STATUS, Const.QAStatusType.FAILED_DO_NOT_PUBLISH.name());
			isProcessed = true;
    	} 
    	
		if (status != null && status.equals(Const.ReportQaStatusType.PASSED.name().toLowerCase())) { 
			exp = exp.ne(Const.QA_STATUS, Const.QAStatusType.FAILED_PASS_TO_ENGINEER.name());
			isProcessed = true;
    	} 
    	
		if (status != null && status.equals(Const.ReportQaStatusType.WITHQAISSUESRESOLVED.name().toLowerCase())) { 
			exp = exp.ne(Const.QA_STATUS, Const.QAStatusType.ISSUE_NOTED.name());
			isProcessed = true;
    	} 
    	
    	if (startDate != null && startDate.length() > 0) {
    		Logger.info("start_date: " + startDate);
    		String startDateStr = Utils.getUnixDateStringFromDate(startDate);
    		Logger.info("start_date string: " + startDateStr);
    		if (status != null && (status.length() > 0 || status.length() ==  0) 
    				&& (status.equals(Const.ReportQaStatusType.QAED.name().toLowerCase())
        				|| status.equals(Const.ReportQaStatusType.WITHQAISSUES.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.WITHNOQAISSUES.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.FAILEDINSTANCES.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.PASSED.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.WITHQAISSUESRESOLVED.name().toLowerCase())	
    					)) { 
    			exp = exp.ge(Const.CHANGED, startDateStr);
    		}
    		if (status != null && status.length() > 0 
    				&& status.equals(Const.ReportQaStatusType.AWAITINGQA.name().toLowerCase())) { 
    			exp = exp.le(Const.CHANGED, startDateStr);
    		}
    		isProcessed = true;
    	} 
    	if (endDate != null && endDate.length() > 0) {
    		Logger.info("end_date: " + endDate);
    		String endDateStr = Utils.getUnixDateStringFromDate(endDate);
    		if (status != null && (status.length() > 0 || status.length() ==  0) 
    				&& (status.equals(Const.ReportQaStatusType.QAED.name().toLowerCase())
    					|| status.equals(Const.ReportQaStatusType.WITHQAISSUES.name().toLowerCase())	
    					|| status.equals(Const.ReportQaStatusType.WITHNOQAISSUES.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.FAILEDINSTANCES.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.PASSED.name().toLowerCase())	
        				|| status.equals(Const.ReportQaStatusType.WITHQAISSUESRESOLVED.name().toLowerCase())	
    					)) { 
    			exp = exp.le(Const.CHANGED, endDateStr);
    		}
    		if (status != null && status.length() > 0 
    				&& status.equals(Const.ReportQaStatusType.AWAITINGQA.name().toLowerCase())) { 
    			exp = exp.ge(Const.CHANGED, endDateStr);
    		}
    		isProcessed = true;
    	} 
//    	if (collection != null && !collection.equals(Const.NONE)) {
//    		exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection);
//    	} 
    	res = exp.query().findList();

    	Logger.info("Expression list for instances size: " + res.size() + ", isProcessed: " + isProcessed);
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
     * This method updates foreign key mapping between an Instance and an Organisation.
     */
    public void updateOrganisation() {
		if (field_nominating_organisation != null
				&& field_nominating_organisation.length() > 0) {
			Organisation organisation = Organisation.findByUrl(field_nominating_organisation);
//            Logger.info("Add instance to organisation: " + organisation.toString());
            this.organisation_to_instance = organisation;
		}
    	
    }
		
    /**
     * This method updates foreign key mapping between a Instance and a DCollection.
     */
    public void updateCollection() {
    	Logger.info("updateCollection() field_collection_categories: " + field_collection_categories);
    	this.collection_to_instance = null;
		if (field_collection_categories != null
				&& field_collection_categories.length() > 0
				&& !field_collection_categories.contains(Const.COMMA)) {
			DCollection collection = DCollection.findByUrl(field_collection_categories);
            Logger.info("Add instance to collection: " + collection.toString());
            this.collection_to_instance = collection;
		} 
    	
    }
	
    /**
     * This method updates foreign key mapping between an Instance and a Flag.
     */
    public void updateFlag() {
		if (flags != null
				&& flags.length() > 0) {
			Flag flag = Flag.findByUrl(flags);
//            Logger.info("Add instance to flag: " + flag.toString());
            this.flag_to_instance = flag;
		}
    	
    }
	
    /**
     * This method updates foreign key mapping between an Instance and a Tag.
     */
    public void updateTag() {
		if (tags != null
				&& tags.length() > 0) {
			Tag tag = Tag.findByUrl(tags);
//            Logger.info("Add instance to tag: " + tag.toString());
            this.tag_to_instance = tag;
		}
    	
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

