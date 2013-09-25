package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * DCollection entity managed by Ebean
 */
@Entity 
public class DCollection extends Model {

    @Id
    public Long id;
    
    public String name; // to remove
    
    public String folder; // additional field if we want groups - could be ignored or removed
    
    public String value;
    public String summary;
    public String format;
    public List<String> field_targets;
    public List<String> field_sub_collections;
    public Long nid;
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
    
    @ManyToMany
    public List<User> members = new ArrayList<User>();
    
    public DCollection(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    // -- Queries
    
    public static Model.Finder<Long,DCollection> find = new Model.Finder(Long.class, DCollection.class);
    
    /**
     * Retrieve dcollection for user
     */
    public static List<DCollection> findInvolving(String user) {
        return find.where()
            .eq("members.email", user)
            .findList();
    }
    
    /**
     * Delete all dcollection in a folder
     */
    public static void deleteInFolder(String folder) {
        Ebean.createSqlUpdate(
            "delete from dcollection where folder = :folder"
        ).setParameter("folder", folder).execute();
    }
    
    /**
     * Create a new dcollection.
     */
    public static DCollection create(String name, String folder, String owner) {
        DCollection dcollection = new DCollection(name, folder, User.find.ref(owner));
        dcollection.save();
        dcollection.saveManyToManyAssociations("members");
        return dcollection;
    }
    
    /**
     * Rename a dcollection
     */
    public static String rename(Long dcollectionId, String newName) {
        DCollection dcollection = find.ref(dcollectionId);
        dcollection.name = newName;
        dcollection.update();
        return newName;
    }
    
    /**
     * Rename a folder
     */
    public static String renameFolder(String folder, String newName) {
        Ebean.createSqlUpdate(
            "update dcollection set folder = :newName where folder = :folder"
        ).setParameter("folder", folder).setParameter("newName", newName).execute();
        return newName;
    }
    
    /**
     * Add a member to this dcollection
     */
    public static void addMember(Long dcollection, String user) {
        DCollection p = DCollection.find.setId(dcollection).fetch("members", "email").findUnique();
        p.members.add(
            User.find.ref(user)
        );
        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Remove a member from this dcollection
     */
    public static void removeMember(Long dcollection, String user) {
        DCollection p = DCollection.find.setId(dcollection).fetch("members", "email").findUnique();
        p.members.remove(
            User.find.ref(user)
        );
        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Check if a user is a member of this dcollection
     */
    public static boolean isMember(Long dcollection, String user) {
        return find.where()
            .eq("members.email", user)
            .eq("id", dcollection)
            .findRowCount() > 0;
    } 
    
    // --
    
    public String toString() {
        return "DCollection(" + id + ") with " + (members == null ? "null" : members.size()) + " members";
    }

}

