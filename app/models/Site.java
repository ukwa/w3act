package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Site entity managed by Ebean
 */
@Entity 
public class Site extends Model {

    @Id
    public Long id;
    
    public String name;
    
    public String folder;
    
    @ManyToMany
    public List<User> members = new ArrayList<User>();
    
    public Site(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    public Site(Long id, String name, String folder, User owner) {
	    this.id = id;
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    // -- Queries
    
    public static Model.Finder<Long,Site> find = new Model.Finder(Long.class, Site.class);
    
    /**
     * Retrieve site for user
     */
    public static List<Site> findInvolving(String user) {
        return find.where()
            .eq("members.email", user)
            .findList();
    }
    
    /**
     * Delete all site in a folder
     */
    public static void deleteInFolder(String folder) {
        Ebean.createSqlUpdate(
            "delete from site where folder = :folder"
        ).setParameter("folder", folder).execute();
    }
    
    /**
     * Create a new site.
     */
    public static Site create(String name, String folder, String owner) {
        Site site = new Site(name, folder, User.find.ref(owner));
        site.save();
        site.saveManyToManyAssociations("members");
        return site;
    }
    
    /**
     * Rename a site
     */
    public static String rename(Long siteId, String newName) {
        Site site = find.ref(siteId);
        site.name = newName;
        site.update();
        return newName;
    }
    
    /**
     * Rename a folder
     */
    public static String renameFolder(String folder, String newName) {
        Ebean.createSqlUpdate(
            "update site set folder = :newName where folder = :folder"
        ).setParameter("folder", folder).setParameter("newName", newName).execute();
        return newName;
    }
    
    /**
     * Add a member to this site
     */
    public static void addMember(Long site, String user) {
        Site p = Site.find.setId(site).fetch("members", "email").findUnique();
        p.members.add(
            User.find.ref(user)
        );
        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Remove a member from this site
     */
    public static void removeMember(Long site, String user) {
        Site p = Site.find.setId(site).fetch("members", "email").findUnique();
        p.members.remove(
            User.find.ref(user)
        );
        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Check if a user is a member of this site
     */
    public static boolean isMember(Long site, String user) {
        return find.where()
            .eq("members.email", user)
            .eq("id", site)
            .findRowCount() > 0;
    } 
    
    // --
    
    public String toString() {
        return "Site(" + id + ") with " + (members == null ? "null" : members.size()) + " members";
    }

}

