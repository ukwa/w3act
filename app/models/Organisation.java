package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

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
        Organisation Organisation = new Organisation(title);
        Organisation.save();
        return Organisation;
    }
    
    /**
     * Rename a Organisation
     */
    public static String rename(Long OrganisationId, String newName) {
        Organisation Organisation = (Organisation) find.ref(OrganisationId);
        Organisation.title = newName;
        Organisation.update();
        return newName;
    }
        
    public String toString() {
        return "Organisation(" + nid + ") with title: " + title + 
        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

