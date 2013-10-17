package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.ExpressionList;

import play.Logger;
import play.db.ebean.*;
import uk.bl.Const;


/**
 * Organisation entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Organisation extends Model {

    @Id
    public Long nid;
    @Column(columnDefinition = "TEXT")
    public String value;
    public String summary;
    public String format;
    public String field_abbreviation;  
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
    // TODO difference between XML and JSON
    public String revision;
    public Long feed_nid;
    
    
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
     * Retrieve an organisation name by URL.
     * @param url
     * @return organisation name
     */
    public static Organisation findByUrl(String url) {
        return find.where().eq("url", url).findUnique();
    }

	/**
	 * This method computes a number of targets per organisation for given organisation URL.
	 * @return
	 */
    /**
     * This method calculates target number for given user URL.
     * @return
     */
    public int getTargetNumberByOrganisationUrl() {
    	int res = 0;
    	res = Target.getTargetNumberByOrganisationUrl(this.url);
    	return res;
    }	

    public String toString() {
        return "Organisation(" + nid + ") with title: " + title + 
        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

