package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;

/**
 * Site entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Target extends Model {

    @Id
    public Long id;
    
    public String name; // to remove
    
    public String folder; // additional field if we want groups - could be ignored or removed
    
    public String field_scope;
    public List<String> field_url;
    public String field_depth;
    public String field_via_correspondence;
    public String field_uk_postal_address;
    public String field_uk_hosting;
    public String field_description;
    public String field_uk_postal_address_url;
    public String field_nominating_organisation;
    public String field_crawl_frequency;
    public List<String> field_suggested_collections;
    public List<String> field_collections;
    public String field_crawl_start_date;
    public boolean field_uk_domain;
    public String field_license;
    public String field_crawl_permission;
    public List<String> field_collection_categories;
    public String field_special_dispensation;
    public List<String> field_notes;
    public List<String> field_instances;
    public boolean field_uk_geoip;
    public String field_professional_judgement;
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
    public int feed_nid;
    
    @ManyToMany
    public List<User> members = new ArrayList<User>();
    
    public Target(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    // -- Queries
    
    public static Model.Finder<Long,Target> find = new Model.Finder(Long.class, Target.class);
    
    /**
     * Retrieve site for user
     */
    public static List<Target> findInvolving(String user) {
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
    public static Target create(String name, String folder, String owner) {
        Target site = new Target(name, folder, User.find.ref(owner));
        site.save();
        site.saveManyToManyAssociations("members");
        return site;
    }
    
    /**
     * Rename a site
     */
    public static String rename(Long siteId, String newName) {
        Target site = find.ref(siteId);
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
        Target p = Target.find.setId(site).fetch("members", "email").findUnique();
        p.members.add(
            User.find.ref(user)
        );
        p.saveManyToManyAssociations("members");
    }
    
    /**
     * Remove a member from this site
     */
    public static void removeMember(Long site, String user) {
        Target p = Target.find.setId(site).fetch("members", "email").findUnique();
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
        return "Target(" + id + ") with " + (members == null ? "null" : members.size()) + " members" + ", url: " + url + ",field_crawl_frequency: " + field_crawl_frequency + ", type: " + type;
    }

}

