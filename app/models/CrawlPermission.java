package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import scala.NotImplementedError;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This calls supports crawl permissions workflow and
 * handles crawl permission requests sent by e-mail to the owner.
 */
@Entity
@Table(name = "crawl_permission")
public class CrawlPermission extends ActModel {

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2250099575463302989L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crawl_permission_seq")
	public Long id;
	
//	the permission can be inherited from a 'parent' target. 
//	Targets could in theory have multiple crawl permissions. This is most likely in the case where a 
//	permission was sent and then cancelled for whatever reason, and then another one sent to supersede it.
	@ManyToOne
	@JoinColumn(name="target_id")
    @Required(message="Target is required")
	public Target target;
    
	//bi-directional many-to-one association to MailTemplate
	@ManyToOne
	@JoinColumn(name="mailTemplate_id")
	public MailTemplate mailTemplate;
	
	//bi-directional many-to-one association to ContactPerson
	@ManyToOne
	@JoinColumn(name="contactPerson_id")
    @Required(message="Contact Person is required")
	public ContactPerson contactPerson;
	
    @Column(columnDefinition = "text")
    @Required(message="Name is required")
    public String name;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String anyOtherInformation;
    
    @JsonIgnore
	@ManyToOne
	@JoinColumn(name="archivist_id")
    public User user; 
    
    /**
     * Records status of permission process e.g. 
     * Not Initiated, Queued, Pending, Refused, Granted
     * Usually populated by system actions, but may also be modified by Archivist 
     */
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String status; 
    
    @JsonIgnore
	@ManyToOne
	@JoinColumn(name="license_id")
    public License license; 
    
	public String token;

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
    
    public static final Model.Finder<Long, CrawlPermission> find = new Model.Finder<Long, CrawlPermission>(Long.class, CrawlPermission.class);

    public CrawlPermission() {
    	this(null, null, null);
    }
    
    public CrawlPermission(Long id, String url) {
		this(id, url, null);
	}

	public CrawlPermission(Long id, String url, String name) {
		this.id = id;
		this.url = url;
		this.name = name;
		this.token = UUID.randomUUID().toString();
		
	}

    public static CrawlPermission findByName(String name) {
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
    
    public static CrawlPermission findByToken(String token) {
    	CrawlPermission res = find.where().eq("token", token).findUnique();
    	return res;
    }          
    
    /**
     * Retrieve a crawl permission by URL.
     * @param url
     * @return crawl permission name
     */
    public static CrawlPermission findByUrl(String url) {
//    	Logger.debug("permission findByUrl: " + url);
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
//    	Logger.debug("permission findByUrl: " + url);
    	CrawlPermission res = new CrawlPermission();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		try {
    			res = find.where().eq(Const.URL, url).findUnique();
            	if (res == null) {
                	res = new CrawlPermission();
                	res.name = Const.NONE;            	}
    		} catch (Exception e) {
    			Logger.debug("crawl permission could not be find in database: " + e);
    		}
    	} else {
        	res.name = Const.NONE;
    	}
//    	Logger.debug("permission res: " + res);
    	return res;
    }

    public static CrawlPermission showByToken(String token) {
    	CrawlPermission crawlPermission = CrawlPermission.findByToken(token);
    	return crawlPermission;
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
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param subject
     * @return true if in list
     */
    public boolean hasContactPerson(String curContactPerson) {
//    	boolean res = false;
//    	res = Utils.hasElementInList(curContactPerson, contactPerson);
//    	return res;
    	throw new NotImplementedError();
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
    			Logger.debug("replacePlaceholdersInText placeholder: " + placeHolders.get(i) +
    					", value: " + values.get(i));
    	    	res = res.replace(placeHolders.get(i), values.get(i));
    		}
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
    		String status) {

        return find.where()
        		.icontains("name", filter)
        		.eq("status", status)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
    public static CrawlPermission create(Long id, String url) {
    	return new CrawlPermission(id, url);
    }
    
    public static CrawlPermission create(Long id, String url, String name) {
    	return new CrawlPermission(id, url, name);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrawlPermission other = (CrawlPermission) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(CrawlPermission c: find.all()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

	@Override
	public String toString() {
		return "CrawlPermission [target=" + target + ", mailTemplate="
				+ mailTemplate + ", contactPerson=" + contactPerson + ", name="
				+ name + ", description=" + description
				+ ", anyOtherInformation=" + anyOtherInformation + ", user="
				+ user + ", status=" + status + ", license=" + license
				+ ", requestFollowup=" + requestFollowup + ", numberRequests="
				+ numberRequests + ", thirdPartyContent=" + thirdPartyContent
				+ ", publish=" + publish + ", agree=" + agree + "]";
	}  
}