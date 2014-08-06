package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Organisation entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name = "organisation")
public class Organisation extends Model {

    @Id @JsonIgnore
    public Long nid;
    @ManyToOne
    public User fk_user;    
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String value;
    @JsonIgnore
    public String summary;
    @JsonIgnore
    public String format;
    @JsonIgnore
    @Required
    public String field_abbreviation;
    @JsonIgnore
    public Long vid;
    @JsonIgnore
    public Boolean is_new;
    @JsonIgnore
    public String type;
    @Required
    public String title;
    @JsonIgnore
    public String language;
    public String url;
    @JsonIgnore
    public String edit_url;
    @JsonIgnore
    public Long status;
    @JsonIgnore
    public Long promote;
    @JsonIgnore
    public Long sticky;
    @JsonIgnore
    public String created;
    @JsonIgnore
    public String changed;
    @JsonIgnore
    public String author;
    @JsonIgnore
    public String log;
    @JsonIgnore
    public Long comment;
    @JsonIgnore
    public Long comment_count;
    @JsonIgnore
    public Long comment_count_new;
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String revision;
    @JsonIgnore
    public Long feed_nid;
    
    @Version @JsonIgnore
    public Timestamp lastUpdate;
    
    
    public Organisation(String title) {
        this.title = title;
    }
    
    public Organisation() {
    }
    
    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<Long,Organisation> find = new Model.Finder(Long.class, Organisation.class);
    
    /**
     * Retrieve Organisation for user
     */
    public static List<Organisation> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve an object by Id (nid).
     * @param nid
     * @return object 
     */
    public static Organisation findById(Long nid) {
    	Organisation res = find.where().eq(Const.NID, nid).findUnique();
    	return res;
    }          
    
    /**
     * This method returns all organisations related alphabetically sorted.
     * @return user list
     */
    public static List<Organisation> findAllSorted() {
    	List<Organisation> res = new ArrayList<Organisation>();
    	Page<Organisation> page = page(0, find.all().size(), Const.TITLE, Const.ASC, "");
    	res = page.getList();
        return res;
    }
        
    /**
     * Create a new Organisation.
     */
    public static Organisation create(String title) {
        Organisation organisation = new Organisation(title);
        organisation.save();
        return organisation;
    }
    
    /**
     * Rename a Organisation
     */
    public static String rename(Long OrganisationId, String newName) {
        Organisation organisation = (Organisation) find.ref(OrganisationId);
        organisation.title = newName;
        organisation.update();
        return newName;
    }
        
    /**
     * Check if given by URL organisation exists in database.
     * @param url
     * @return true if exists
     */
    public static boolean existsByUrl(String url) {
    	boolean res = false;
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		Organisation organisation = find.where().eq(Const.URL, url).findUnique();
    		if (organisation != null) {
    			res = true;
    		}
    	}
    	return res;
    }
    
    /**
     * Check if given by title organisation exists in database.
     * @param url
     * @return true if exists
     */
    public static boolean existsByTitle(String title) {
    	boolean res = false;
    	if (title != null && title.length() > 0 && !title.equals(Const.NONE)) {
    		Organisation organisation = find.where().eq(Const.TITLE, title).findUnique();
    		if (organisation != null) {
    			res = true;
    		}
    	}
    	return res;
    }
    
    /**
     * Retrieve an organisation name by URL.
     * @param url
     * @return organisation name
     */
    public static Organisation findByUrl(String url) {
//    	Logger.info("organisation findByUrl: " + url);
    	Organisation res = new Organisation();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
//    		Logger.info("found: " + res);
    		if (res == null) {
    			res = new Organisation();
        		res.title = Const.NONE;
        		res.url = Const.NONE;
    		}
    	} else {
    		res.title = Const.NONE;
    		res.url = Const.NONE;
//    		Logger.info("not found: " + res);
    	}
//		Logger.info("return: " + res);
    	return res;
    }

    /**
     * Retrieve an organisation by title.
     * @param title
     * @return organisation object
     */
    public static Organisation findByTitle(String title) {
//    	Logger.info("organisation title: " + title);
    	Organisation res = new Organisation();
    	if (title != null && title.length() > 0) {
    		res = find.where().eq(Const.TITLE, title).findUnique();
//        	Logger.info("res: " + res);
    		if (res == null) {
    			res = new Organisation();
        		res.title = Const.NONE;
        		res.url = Const.NONE;
    		}
    	} else {
    		res.title = Const.NONE;
    		res.url = Const.NONE;
    	}
//    	Logger.info("final organisation res: " + res);
    	return res;
    }

	/**
	 * This method filters organisations by title and returns a list of filtered Organisation objects.
	 * @param title
	 * @return
	 */
	public static List<Organisation> filterByName(String name) {
		List<Organisation> res = new ArrayList<Organisation>();
        ExpressionList<Organisation> ll = find.where().icontains(Const.TITLE, name);
    	res = ll.findList();
		return res;
	}
    

    /**
     * This method is used for filtering by URL.
     * @param url
     * @return
     */
    public static List<Organisation> findFilteredByUrl(String url) {
    	List<Organisation> ll = new ArrayList<Organisation>();
//    	Logger.info("organisation findFilteredByUrl(): " + url);
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) { 
            Organisation organisation = find.where().eq(Const.URL, url).findUnique();
            ll.add(organisation);            
    	} else {
            ll = find.all();
    	}
    	return ll;
    }

	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
    public int getTargetNumberByOrganisationUrl() {
    	int res = 0;
    	res = Target.getTargetNumberByOrganisationUrl(this.url);
    	return res;
    }	

    /**
     * Retrieve all organisations.
     */
    public static List<Organisation> findAll() {
        return find.all();
    }

    /**
     * This method shows nominating organisation in HTML page.
     * @param organisationUrl The link to organisation in Target object field 
     * 'field_nominating_organisation'
     * @return
     */
    public static String getNominatingOrganisation(String organisationUrl) {
        return Organisation.findByUrl(organisationUrl).title; 
    }
            
    public String toString() {
        return "Organisation(" + nid + ") with title: " + title + 
        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

    /**
     * Return a page of Organisations
     *
     * @param page Page to display
     * @param pageSize Number of Organisations per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Organisation> page(int page, int pageSize, String sortBy, String order, String query) {
    	return find.where().icontains(Const.TITLE, query)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

}

