package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;
import uk.bl.api.models.FieldValue;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.QueryIterator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import controllers.Flags;


/**
 * Instance instance entity managed by Ebean
 */
@Entity 
@Table(name = "instance")
public class Instance extends JsonModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4285620218930401425L;

	//bi-directional many-to-many association to Subject
	@ManyToMany(mappedBy="instances")
	public List<Taxonomy> subjects = new ArrayList<Taxonomy>();

	//bi-directional many-to-many association to DCollection
	@ManyToMany(mappedBy="instances")
	public List<Taxonomy> collections = new ArrayList<Taxonomy>();
	
	//bi-directional many-to-many association to Flag
	@ManyToMany(mappedBy="instances")
	public List<Flag> flagToInstance = new ArrayList<Flag>();
    
	//bi-directional many-to-many association to Tag
	@ManyToMany(mappedBy="instances")
	public List<Tag> tagToInstance = new ArrayList<Tag>();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "qaissue_id")
	public QaIssue qaIssue;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "target_id")
	public Target target;
	
    public Date fieldTimestamp;
	
    @Column(columnDefinition = "text")
    public String value;
    @Column(columnDefinition = "text")
    public String summary;
    
    public Date fieldDate;
    
	@JsonProperty
	public String edit_url;
	
	@Transient
	@JsonProperty
	private String field_timestamp;
	
	@Transient
	@JsonProperty
	private FieldModel field_qa_issues;

	@Transient
	@JsonProperty
	private FieldModel field_target;

	@Transient
	@JsonProperty
	private FieldValue field_description_of_qa_issues;
	
	@Transient
	@JsonProperty
	private Boolean field_published;
	
	@Transient
	@JsonProperty
	private Boolean field_to_be_published_;

	
//	same "body":{
//		"value":"too much data here",
//		"summary":"","format":"filtered_html"
//		},
//		"field_timestamp":"20130908110118",
//		"field_qa_issues":{
//			"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/165","id":"165","resource":"taxonomy_term"
//			},
//		"field_target":{
//				"uri":"http:\/\/webarchive.org.uk\/act\/node\/676","id":"676","resource":"node"
//			},
//		"field_description_of_qa_issues":{
//				"value":"\u003Cp\u003ESite not live\u003C\/p\u003E\n","format":"filtered_html"
//			},
//		"field_published":false,
//		"field_to_be_published_":false,
	
//	same	"nid":"12954",
//	same	"vid":"20109",
//	same	"is_new":false,
//	same	"type":"instance",
//	same 	"title":"20130908110118",
//	same 	"language":"und",
//	same	"url":"http:\/\/webarchive.org.uk\/act\/node\/12954",
//	same	"edit_url":"http:\/\/webarchive.org.uk\/act\/node\/12954\/edit",
//	same 	"status":"1",
//	same 	"promote":"0",
//	same 	"sticky":"0",
//	same	"created":"1394535642",
//	same	"changed":"1394551688",
//	same	"log":"",
//	same	"revision":null,
//	same	"comment":"1",
//	same	"comments":[],
//	same	"comment_count":"0",
//	same	"comment_count_new":"0",
//	same	"feed_nid":null}

    public String format;
    public String fieldScope;
    public String fieldDepth;
    public Boolean fieldViaCorrespondence;
    public Boolean fieldUkPostalAddress;
    public Boolean fieldUkHosting;
    public String fieldNominatingOrganisation;
    public String fieldCrawlFrequency;
    public Date fieldCrawlStartDate;
    public Date fieldCrawlEndDate;
    public Boolean fieldUkDomain;
    public String fieldCrawlPermission;
    public String fieldSpecialDispensation;
    public Boolean fieldUkGeoip;
    public Boolean fieldProfessionalJudgement;

    public String author; 
    public String log;

    public String fieldLiveSiteStatus;
    public Long fieldWct_id;
    public Long fieldSpt_id;
    public Boolean fieldNoLdCriteriaMet;
    public Boolean fieldKeySite;
    @Column(columnDefinition = "text")
    public String fieldProfessionalJudgementExp;
    public Boolean fieldIgnoreRobotsTxt;
    public String revision;
    @Column(columnDefinition = "text")
    public String fieldTarget;
    @Column(columnDefinition = "text")
    public String fieldDescriptionOfQaIssues;
    @Column(columnDefinition = "text")
    
    
    public Boolean fieldPublished;
    public Boolean fieldToBePublished;
    public String dateOfPublication;
    @Column(columnDefinition = "text")
    public String justification; 
    @Column(columnDefinition = "text")
    public String selectorNotes; 
    @Column(columnDefinition = "text")
    public String archivistNotes; 
    @Required
    public String selectionType; 
    public String selector;     
    public Long legacySite_id;
    public String whiteList; // regex for white list URLs
    public String blackList; // regex for black list URLs
    
    // lists
    @Required
    @Column(columnDefinition = "text")
    public String fieldUrl; 
    @Column(columnDefinition = "text")
    public String fieldDescription; 
    @Column(columnDefinition = "text")
    public String fieldUkPostalAddressUrl; 
    @Column(columnDefinition = "text")
    public String fieldSuggestedCollections; 
    @Column(columnDefinition = "text")
    public String fieldCollections; 
    @Column(columnDefinition = "text")
    public String fieldLicense; 
    @Column(columnDefinition = "text")
    public String fieldCollectionCategories; 
    @Column(columnDefinition = "text")
    public String fieldNotes; 
    @Column(columnDefinition = "text")
    public String fieldInstances; 
    @Required
    @Column(columnDefinition = "text")
    public String fieldSubject; 
    @Column(columnDefinition = "text")
    public String fieldSubSubject; 
    
    @Column(columnDefinition = "text")
    public String qaIssueCategory; 
    @Column(columnDefinition = "text")
    public String qaNotes; 
    @Column(columnDefinition = "text")
    public String qualityNotes; 
    @Column(columnDefinition = "text")
    public String keywords; 
    @Column(columnDefinition = "text")
    public String tags; 
    @Column(columnDefinition = "text")
    public String synonyms; 
    @Column(columnDefinition = "text")
    public String originatingOrganisation; 
    @Column(columnDefinition = "text")
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
        ExpressionList<Instance> ll = find.where().eq("fieldNominatingOrganisation", url);
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
	public List<String> getFieldList(String fieldName) {
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
        ExpressionList<Instance> ll = find.where().eq("fieldUrl", this.fieldUrl);
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
        ExpressionList<Instance> ll = find.where().eq("fieldSubject", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
	public static int getInstanceNumberByOrganisationUrl(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("fieldNominatingOrganisation", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given crawl frequency.
	 * @return
	 */
	public static int getInstanceNumberByCrawlFrequency(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("fieldCrawlFrequency", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given depth.
	 * @return
	 */
	public static int getInstanceNumberByDepth(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("fieldDepth", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given license.
	 * @return
	 */
	public static int getInstanceNumberByLicense(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq("fieldLicense", url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method computes a number of targets for given scope.
	 * @return
	 */
	public static int getInstanceNumberByScope(String url) {
		int res = 0;
        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_SCOPE, url);
        res = ll.findRowCount();
		return res;
	}
	
	/**
	 * This method filters targets by given URLs.
	 * @return duplicate count
	 */
	public static List<Instance> filterUrl(String url) {
		List<Instance> res = new ArrayList<Instance>();
        ExpressionList<Instance> ll = find.where().contains("fieldUrl", url);
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
	        ExpressionList<Instance> ll = find.where().contains(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
	    	res = ll.findList(); 
		}
		return res;
	}
	
//	/**
//	 * This method filters targets by given URLs.
//	 * @return duplicate count
//	 */
//	public static List<String> getSubjects() {
//		List<String> subjects = new ArrayList<String>();
//		List<Instance> allInstances = find.all();
//		Iterator<Instance> itr = allInstances.iterator();
//		while (itr.hasNext()) {
//			Instance target = itr.next();
//			if (target.fieldSubject != null && target.fieldSubject.length() > 0 && !subjects.contains(target.fieldSubject)) {
//		        ExpressionList<Instance> ll = find.where().contains("field_subject", target.fieldSubject);
//		        if (ll.findRowCount() > 0) {
//		        	subjects.add(target.fieldSubject);
//		        }
//			}
//		}
//    	return subjects;
//	}
	
	public String getFieldUrlAsStr() {
		return getFieldListAsStr("fieldUrl");
	}
	
	/**
	 * This method retrieves value of the list field.
	 * @param fieldName
	 * @return list of strings as a String
	 */
	public String getFieldListAsStr(String fieldName) {
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
	public String getUserById() {
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
    public static Instance findByTimestampAndUrl(Date timestamp, String url) {
    	Instance res = new Instance();
//        Logger.info("instance timestamp: " + timestamp);
        
		List<Instance> list = new ArrayList<Instance>();
		if (timestamp != null && url != null && url.length() > 0) {
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
    public static Instance findById(Long id) {
        Logger.info("target nid: " + id);       
        Instance res = find.where().eq(Const.ID, id).findUnique();
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
	public static Date showLatestTimestamp(String url) {
//		String res = "";
        Date lastDate = null;
		if (url != null && url.length() > 0) {
			List<Instance> instanceList = new ArrayList<Instance>();
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
	        instanceList = ll.findList(); 
	        Iterator<Instance> itr = instanceList.iterator();
//	        Date lastDate = new Date();
	        while (itr.hasNext()) {
	        	Instance instance = itr.next();
	        	Date curDate = instance.createdAt;
//	        	String curDate = instance.field_timestamp;
//	        	Date curDate = new Date(instance.field_timestamp);
	        	if (lastDate == null) {
	        		lastDate = curDate;
	        	}
//	        	Date lastDateTime = new Date(lastDate);
//	        	Date curDateTime = new Date(curDate);
//	        	long lastDateTime = Long.parseLong(lastDate);
//	        	long curDateTime = Long.parseLong(curDate);
	        	
	            if(curDate.after(lastDate)){
	        		lastDate = curDate;
	            }
//	        	if (curDateTime > lastDateTime) {
//	        		lastDate = curDate;
//	        	}
	        }
//	        res = Utils.getDateFromUnixDate(lastDate);
//	        res = Utils.showTimestamp(lastDate);
		}
		return lastDate;		
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
		Date lastDate = null;
		if (url != null && url.length() > 0) {
			List<Instance> instanceList = new ArrayList<Instance>();
	        ExpressionList<Instance> ll = find.where().eq(Const.FIELD_TARGET, url);
	        instanceList = ll.findList(); 
	        Iterator<Instance> itr = instanceList.iterator();
	        while (itr.hasNext()) {
	        	Instance instance = itr.next();
	        	Date curDate = instance.fieldTimestamp;
	        	if (lastDate == null) {
	        		lastDate = curDate;
	        	}
//	        	long lastDateTime = Long.parseLong(lastDate);
//	        	long curDateTime = Long.parseLong(curDate);
	            if(curDate.after(lastDate)){
//	        	if (curDateTime > lastDateTime) {
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
    	res = Utils.hasElementInList(subject, fieldSubject);
    	return res;
    }
        
    /**
     * This method evaluates if a collection is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasCollection(String collection) {
    	boolean res = false;
    	res = Utils.hasElementInList(collection, fieldSuggestedCollections);
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
    	return Organisation.findByUrl(fieldNominatingOrganisation);
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
    	res = Utils.hasElementInList(subject, this.fieldSubSubject);
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
		if (fieldNominatingOrganisation != null
				&& fieldNominatingOrganisation.length() > 0) {
			Organisation organisation = Organisation.findByUrl(fieldNominatingOrganisation);
//            Logger.info("Add instance to organisation: " + organisation.toString());
            this.organisation = organisation;
		}
    	
    }
    
    public String getField_timestamp() {
		return field_timestamp;
	}

	public void setField_timestamp(String field_timestamp) {
		this.field_timestamp = field_timestamp;
	}

	public FieldModel getField_qa_issues() {
		return field_qa_issues;
	}

	public void setField_qa_issues(FieldModel field_qa_issues) {
		this.field_qa_issues = field_qa_issues;
	}

	public FieldModel getField_target() {
		return field_target;
	}

	public void setField_target(FieldModel field_target) {
		this.field_target = field_target;
	}

	public FieldValue getField_description_of_qa_issues() {
		return field_description_of_qa_issues;
	}

	public void setField_description_of_qa_issues(
			FieldValue field_description_of_qa_issues) {
		this.field_description_of_qa_issues = field_description_of_qa_issues;
	}

	public Boolean getField_published() {
		return field_published;
	}

	public void setField_published(Boolean field_published) {
		this.field_published = field_published;
	}

	public Boolean getField_to_be_published_() {
		return field_to_be_published_;
	}

	public void setField_to_be_published_(Boolean field_to_be_published_) {
		this.field_to_be_published_ = field_to_be_published_;
	}

	public String toString() {
        return "Instance(" + id + ") with" + " title: " + title  + " url: " + url + ", field_crawl_frequency: " + fieldCrawlFrequency + ", type: " + type +
        ", field_uk_domain: " + fieldUkDomain + ", field_url: " + fieldUrl + 
        ", field_description: " + fieldDescription + ", field_uk_postal_address_url: " + fieldUkPostalAddressUrl +
        ", field_suggested_collections: " + fieldSuggestedCollections + ", field_collections: " + fieldCollections +
        ", field_license: " + fieldLicense + ", field_collection_categories: " + fieldCollectionCategories +
        ", field_notes: " + fieldNotes + ", field_instances: " + fieldInstances + 
        ", field_subject: " + fieldSubject + ", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

