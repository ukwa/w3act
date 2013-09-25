package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Organisation entity managed by Ebean
 */
@Entity 
public class Organisation extends Model {

    @Id
    public Long nid;
    public String field_abbreviation;  
    public Long vid;
    public boolean is_new;
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
      
    public Organisation(String title) {
        this.title = title;
    }
    
    public Organisation() {
    }
    
    // -- Queries
    
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
        return "Organisation(" + nid + ") with title: " + title;
    }

}

