package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;

/**
 * Target entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Target extends Model {

    @Id
    public Long nid;
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
    public Long feed_nid;
    
    /**
     * Constructor
     * @param title
     * @param url
     */
    public Target(String title, String url) {
    	this.title = title;
    	this.url = url;
    }

    // -- Queries
    
    public static Model.Finder<Long,Target> find = new Model.Finder(Long.class, Target.class);
    
    /**
     * Retrieve target for user
     */
    public static List<Target> findInvolving() {
	    return find.all();
	}

    /**
     * Create a new target.
     */
    public static Target create(String title, String url) {
        Target target = new Target(title, url);
        target.save();
        return target;
    }

   /**
     * Rename a target
     */
    public static String rename(Long targetId, String newName) {
        Target target = (Target) find.ref(targetId);
        target.title = newName;
        target.update();
        return newName;
    }
        
    public String toString() {
        return "Target(" + nid + ") with" + " url: " + url + ", field_crawl_frequency: " + field_crawl_frequency + ", type: " + type;
    }

}

