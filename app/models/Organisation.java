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
    public Long id;
    
    public String name; // to remove
    
    public String folder; // additional field if we want groups - could be ignored or removed
    
    public String field_abbreviation;  
    public int nid;
    public int vid;
    public boolean is_new;
    public String type;
    public String title;
    public String language;
    public String url;
    public String edit_url;
    public int status;
    public int promote;
    public int sticky;
    public String created;
    public String changed;
    public String author;
    public String log;
    public int comment;
    public int comment_count;
    public int comment_count_new;
    
    @ManyToMany
    public List<User> members = new ArrayList<User>();
    
    public Organisation(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    // -- Queries
    
    public static Model.Finder<Long,Organisation> find = new Model.Finder(Long.class, Organisation.class);
    
    /**
     * Retrieve Organisation for user
     */
    public static List<Organisation> findInvolving(String user) {
        return find.where()
            .eq("members.email", user)
            .findList();
    }
    
    /**
     * Delete all Organisation in a folder
     */
    public static void deleteInFolder(String folder) {
        Ebean.createSqlUpdate(
            "delete from Organisation where folder = :folder"
        ).setParameter("folder", folder).execute();
    }
    
    /**
     * Create a new Organisation.
     */
    public static Organisation create(String name, String folder, String owner) {
        Organisation Organisation = new Organisation(name, folder, User.find.ref(owner));
        Organisation.save();
        Organisation.saveManyToManyAssociations("members");
        return Organisation;
    }
    
    /**
     * Rename a Organisation
     */
    public static String rename(Long OrganisationId, String newName) {
        Organisation Organisation = find.ref(OrganisationId);
        Organisation.name = newName;
        Organisation.update();
        return newName;
    }
    
    /**
     * Rename a folder
     */
    public static String renameFolder(String folder, String newName) {
        Ebean.createSqlUpdate(
            "update Organisation set folder = :newName where folder = :folder"
        ).setParameter("folder", folder).setParameter("newName", newName).execute();
        return newName;
    }
    
    /**
     * Add a member to this Organisation
     */
    public static void addMember(Long Organisation, String user) {
//        Organisation p = Organisation.find.setId(Organisation).fetch("members", "email").findUnique();
//        p.members.add(
//            User.find.ref(user)
//        );
//        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Remove a member from this Organisation
     */
    public static void removeMember(Long Organisation, String user) {
//        Organisation p = Organisation.find.setId(Organisation).fetch("members", "email").findUnique();
//        p.members.remove(
//            User.find.ref(user)
//        );
//        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Check if a user is a member of this Organisation
     */
    public static boolean isMember(Long Organisation, String user) {
        return find.where()
            .eq("members.email", user)
            .eq("id", Organisation)
            .findRowCount() > 0;
    } 
    
    // --
    
    public String toString() {
        return "Organisation(" + id + ") with " + (members == null ? "null" : members.size()) + " members";
    }

}

