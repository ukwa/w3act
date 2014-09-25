package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This calls supports crawl permissions workflow and
 * handles crawl permission requests sent by e-mail to the owner.
 */
@Entity
@Table(name = "crawl_permission")
public class CrawlPermission extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2250099575463302989L;

	@Id @JsonIgnore
    public Long id;

	//bi-directional many-to-one association to Target
	@ManyToOne
	@JoinColumn(name="id_target")
	public Target target_to_crawl_permission;
    
	//bi-directional many-to-one association to MailTemplate
	@ManyToOne
	@JoinColumn(name="id_mailtemplate")
	public MailTemplate mailtemplate_to_crawlpermission;
	
	//bi-directional many-to-one association to ContactPerson
	@ManyToOne
	@JoinColumn(name="id_contactperson")
	public ContactPerson contactperson_to_crawlpermission;
	
    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;

    @Column(columnDefinition = "TEXT")
    @Required
    public String name;
    
    /**
     * This field contains target URL.
     */
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    @Required
    public String target;
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String anyOtherInformation;
    
    /**
     * Records status of permission process e.g. 
     * Not Initiated, Queued, Pending, Refused, Granted
     * Usually populated by system actions, but may also be modified by Archivist 
     */
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String status; 
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String contactPerson; 
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String creatorUser; 
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String assignedArchivist; 
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String template; 
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String license;
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String licenseDate;
    
    /**
     * This is a checkbox defining whether follow up e-mails should be send.
     */
    @JsonIgnore
    public Boolean requestFollowup;
    
    /**
     * The number of requests that were sent.
     */
    @JsonIgnore
    public Long numberRequests;
    
    /**
     * This is a checkbox defining whether any content on this web site subject 
     * to copyright and/or the database right held by another party.
     */
    @JsonIgnore
    public Boolean thirdPartyContent;
    
    /**
     * This is a checkbox defining whether owner allows the archived web site 
     * to be used in any future publicity for the Web Archive.
     */
    @JsonIgnore
    public Boolean publish;
    
    /**
     * This is a checkbox defining whether owner agrees to archive web site.
     */
    @JsonIgnore
    public Boolean agree;
    
    @JsonIgnore
    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, CrawlPermission> find = new Model.Finder<Long, CrawlPermission>(Long.class, CrawlPermission.class);

    public CrawlPermission() {}
    
    public CrawlPermission(Long id, String url) {
		this.id = id;
		this.url = url;
	}

	public CrawlPermission(Long id, String url, String name) {
		this.id = id;
		this.url = url;
		this.name = name;
	}

	public String getName()
    {
        return name;
    }

    public static CrawlPermission findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static CrawlPermission findById(Long id) {
    	CrawlPermission res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    /**
     * Retrieve a crawl permission by URL.
     * @param url
     * @return crawl permission name
     */
    public static CrawlPermission findByUrl(String url) {
//    	Logger.info("permission findByUrl: " + url);
    	CrawlPermission res = new CrawlPermission();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    /**
     * @param target The field URL
     * @return
     */
    public static CrawlPermission findByTarget(String target) {
    	CrawlPermission res = new CrawlPermission();
    	if (target != null && target.length() > 0 && !target.equals(Const.NONE)) {
    		res = find.where().eq(Const.TARGET, target).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    /**
     * This method is used to show crawl permission in a table.
     * It shows none value if no entry was found in database.
     * @param url
     * @return
     */
    public static CrawlPermission showByUrl(String url) {
//    	Logger.info("permission findByUrl: " + url);
    	CrawlPermission res = new CrawlPermission();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		try {
    			res = find.where().eq(Const.URL, url).findUnique();
            	if (res == null) {
                	res = new CrawlPermission();
                	res.name = Const.NONE;            	}
    		} catch (Exception e) {
    			Logger.info("crawl permission could not be find in database: " + e);
    		}
    	} else {
        	res.name = Const.NONE;
    	}
//    	Logger.info("permission res: " + res);
    	return res;
    }
    
	/**
	 * This method filters crawl permissions by name and returns a list 
	 * of filtered CrawlPermission objects.
	 * @param name
	 * @return
	 */
	public static List<CrawlPermission> filterByName(String name) { 
		List<CrawlPermission> res = new ArrayList<CrawlPermission>();
        ExpressionList<CrawlPermission> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
	/**
	 * This method filters crawl permissions by contact person and returns a list 
	 * of filtered CrawlPermission objects.
	 * @param url The identifier for contact person
	 * @return
	 */
	public static List<CrawlPermission> filterByContactPerson(String url) { 
		List<CrawlPermission> res = new ArrayList<CrawlPermission>();
        ExpressionList<CrawlPermission> ll = find.where().icontains(Const.CONTACT_PERSON, url);
    	res = ll.findList();
		return res;
	}
        
	/**
	 * Find out crawl permission by target that was submitted by owner.
	 * @param cur_target
	 * @return permission list
	 */
	public static List<CrawlPermission> filterByTarget(String cur_target) {
		List<CrawlPermission> res = new ArrayList<CrawlPermission>();
        ExpressionList<CrawlPermission> ll = find.where().icontains(Const.TARGET, cur_target);
    	res = ll.findList();
		return res;
	}
        
	/**
	 * This method filters crawl permissions by status and returns a list 
	 * of filtered CrawlPermission objects.
	 * @param status
	 * @return
	 */
	public static List<CrawlPermission> filterByStatus(String status) {
		List<CrawlPermission> res = new ArrayList<CrawlPermission>();
        ExpressionList<CrawlPermission> ll = find.where().icontains(Const.STATUS, status);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all crawl permissions.
     */
    public static List<CrawlPermission> findAll() {
        return find.all();
    }
    
    /**
     * This method returns a list of all status values for crawl permission record.
     * @return
     */
    public static List<String> getAllStatus() {
    	List<String> res = new ArrayList<String>();
	    Const.CrawlPermissionStatus[] resArray = Const.CrawlPermissionStatus.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         

    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasContactPerson(String curContactPerson) {
    	boolean res = false;
    	res = Utils.hasElementInList(curContactPerson, contactPerson);
    	return res;
    }

    /**
     * This method enables replacing of placeholders in mail text by given value.
     * @param text The text of an email.
     * @param placeHolder The placeholder string e.g. ||URL||
     * @param value The value that overwrites placeholder
     * @return updated text
     */
    public static String replaceStringInText(String text, String placeHolder, String value) {
    	String res = text;
    	res = text.replace(placeHolder, value);
    	return res;
    }
    
    /**
     * This method enables replacing of two place holders in mail text by given values.
     * @param text The text of an email.
     * @param placeHolderUrl The placeholder string for crawl URL ||URL||
     * @param placeHolderLink The placeholder string for unique license URL ||LINK||
     * @param valueUrl The value that overwrites associated placeholder
     * @param valueLink The value that overwrites associated placeholder
     * @return updated text
     */
    public static String replaceTwoStringsInText(String text, String placeHolderUrl, String placeHolderLink, 
    		String valueUrl, String valueLink) {
    	String res = text;
//    	Logger.debug("replaceTwoStringsInText valueUrl: " + valueUrl);
    	List<String> placeHolders = new ArrayList<String>();
    	placeHolders.add(placeHolderUrl);
    	placeHolders.add(placeHolderLink);
    	List<String> values = new ArrayList<String>();
    	values.add(valueUrl);
    	values.add(valueLink);
    	res = replacePlaceholdersInText(text, placeHolders, values);
    	return res;
    }
    
    /**
     * This method enables replacing of place holders in mail text by given values.
     * @param text The text of an email.
     * @param placeHolders The placeholder list in string format e.g. ||URL||, ||LINK||
     * @param values The value that overwrites place holders
     * @return updated text
     */
    public static String replacePlaceholdersInText(String text, List<String> placeHolders, List<String> values) {
    	String res = text;
    	if (placeHolders != null && placeHolders.size() > 0 
    			&& values != null && values.size() > 0
    			&& placeHolders.size() == values.size()) {
    		int counter = placeHolders.size();
    		for (int i = 0; i < counter; i++) {
    			Logger.info("replacePlaceholdersInText placeholder: " + placeHolders.get(i) +
    					", value: " + values.get(i));
    	    	res = res.replace(placeHolders.get(i), values.get(i));
    		}
    	}
    	return res;
    }
    
    /**
     * This method returns a list of all request filtering types for crawl permission record.
     * @return
     */
    public static List<String> getAllRequestTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.RequestTypes[] resArray = Const.RequestTypes.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         
    
    /**
     * Return a page of crawl permission 
     *
     * @param page Page to display
     * @param pageSize Number of Users per page
     * @param sortBy Crawl permission property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     * @param status The status of crawl permission request (e.g. QUEUED, PENDING...)
     * @param target The field URL
     */
    public static Page<CrawlPermission> page(int page, int pageSize, String sortBy, String order, String filter, 
    		String status, String target) {

        return find.where()
        		.icontains(Const.NAME, filter)
        		.eq(Const.STATUS, status)
//        		.ne(Const.STATUS, Const.NONE)
//        		.ne(Const.STATUS, Const.NONE_VALUE)
        		.icontains(Const.TARGET, target)
//        		.ne(Const.TARGET, Const.NONE)
//        		.ne(Const.TARGET, Const.NONE_VALUE)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
    public String toString() {
        return "CrawlPermission(" + name + ")" + ", id:" + id;
    }    

    /**
     * This method updates foreign key mapping between a CrawlPermission and a Target.
     */
    public void updateTarget() {
		if (target != null
				&& target.length() > 0) {
			Target targetObj = Target.findByTarget(target);
//            Logger.info("Add crawl permission to target: " + targetObj.toString());
            this.target_to_crawl_permission = targetObj;
		}
    	
    }
    
    /**
     * This method updates foreign key mapping between a CrawlPermission and a MailTemplate.
     */
    public void updateMailTemplate() {
        this.mailtemplate_to_crawlpermission = null;
		if (template != null
				&& template.length() > 0) {
			MailTemplate mailTemplate = MailTemplate.findByUrl(template);
//            Logger.info("Add crawl permission to mail template: " + mailTemplate.toString());
            this.mailtemplate_to_crawlpermission = mailTemplate;
		}    	
    }
    
    /**
     * This method updates foreign key mapping between a CrawlPermission and a ContactPerson.
     */
    public void updateContactPerson() {
        this.contactperson_to_crawlpermission = null;
		if (contactPerson != null
				&& contactPerson.length() > 0) {
			ContactPerson contactPersonObj = ContactPerson.findByUrl(contactPerson);
//            Logger.info("Add crawl permission to contactPerson: " + contactPerson.toString());
            this.contactperson_to_crawlpermission = contactPersonObj;
		}    	
    }
    
    public static CrawlPermission create(Long id, String url) {
    	return new CrawlPermission(id, url);
    }
    
    public static CrawlPermission create(Long id, String url, String name) {
    	return new CrawlPermission(id, url, name);
    }

}