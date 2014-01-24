package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;

/**
 * This calls supports crawl permissions workflow and
 * handles crawl permission requests sent by e-mail to the owner.
 */
@Entity
public class CrawlPermission extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2250099575463302989L;

	@Id @JsonIgnore
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;

    @Column(columnDefinition = "TEXT")
    public String name;
    
    /**
     * This field contains target URL.
     */
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String target;
    
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String description;
    
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
    
    @JsonIgnore
    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, CrawlPermission> find = new Model.Finder<Long, CrawlPermission>(Long.class, CrawlPermission.class);

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
     * Retrieve all crawl permissions.
     */
    public static List<CrawlPermission> findAll() {
        return find.all();
    }
    
    public String toString() {
        return "CrawlPermission(" + name + ")" + ", id:" + id;
    }    

}