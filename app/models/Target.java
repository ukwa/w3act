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
    public String value; // body[] TODO
    public String summary; // body[]
    public String format; // body[]
    public String field_scope;
    public List<String> field_url;
    public String field_depth;
    public String field_via_correspondence;
    public String field_uk_postal_address;
    public String field_uk_hosting;
    public List<String> field_description;
    public List<String> field_uk_postal_address_url;
    public String field_nominating_organisation;
    public String field_crawl_frequency;
    public List<String> field_suggested_collections;
    public List<String> field_collections;
    public String field_crawl_start_date;
    public Boolean field_uk_domain;
    public List<String> field_license;
    public String field_crawl_permission;
    public List<String> field_collection_categories;
    public String field_special_dispensation;
    public List<String> field_notes;
    public List<String> field_instances;
    public Boolean field_uk_geoip;
    public String field_professional_judgement;
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
    public String author; // uri, id, resource TODO User
    public String log;
    public Long comment;
    public Long comment_count;
    public Long comment_count_new;
    public Long feed_nid;
    //TODO difference between XML and JSON
    public List<String> field_subject;
    //public Taxonomy taxonomy_term; (id-Long, resource-String) TODO
    public String field_crawl_end_date;
    public Long field_live_site_status;
    public Long field_wct_id;
    public Long field_spt_id;
    public Boolean field_no_ld_criteria_met;
    public String field_key_site;
    public String field_professional_judgement_exp;
    public String field_ignore_robots_txt;
    public String revision;
    
    /**
     * Constructor
     * @param title
     * @param url
     */
    public Target(String title, String url) {
    	this.title = title;
    	this.url = url;
    }

    public Target() {
    }

    // -- Queries
    
    public static Model.Finder<Long,Target> find = new Model.Finder(Long.class, Target.class);
    
    /**
     * Retrieve targets
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
        return "Target(" + nid + ") with" + " url: " + url + ", field_crawl_frequency: " + field_crawl_frequency + ", type: " + type +
        ", field_uk_domain: " + field_uk_domain + ", field_url: " + field_url;
    }

}

