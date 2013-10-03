package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.Logger;
import play.db.ebean.Model;


/**
 * Target entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Target extends Model {

    @Id
    public Long nid;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Body> bodies = new ArrayList<Body>();
    public String field_scope;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_url = new ArrayList<Item>();
    public String field_depth;
    public String field_via_correspondence;
    public String field_uk_postal_address;
    public String field_uk_hosting;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_description;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_uk_postal_address_url;
    public String field_nominating_organisation;
    public String field_crawl_frequency;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_suggested_collections;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_collections;
    public String field_crawl_start_date;
    public Boolean field_uk_domain;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_license;
    public String field_crawl_permission;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_collection_categories;
    public String field_special_dispensation;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_notes;
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_instances;
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
    @OneToMany(cascade=CascadeType.ALL)  
    public List<Item> field_subject;
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
        
    /**
     * This method translates database view to the HTML view.
     * @return list of Strings
     */
    @SuppressWarnings("unchecked")
	public List<String> get_field_list(String fieldName) {
    	List<String> res = new ArrayList<String>();
    	try {
			Field field = this.getClass().getField(fieldName); 
	        Iterator<Item> itemItr = ((List<Item>) field.get(this)).iterator();
	        while (itemItr.hasNext()) {
	        	Item item = itemItr.next();
	        	res.add(item.value);
	        }
		} catch (IllegalArgumentException e) {
			Logger.info(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.info(e.getMessage());
		} catch (SecurityException e) {
			Logger.info(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.info(e.getMessage());
		}
    	return res;
    }
    
    public String toString() {
        return "Target(" + nid + ") with" + " url: " + url + ", field_crawl_frequency: " + field_crawl_frequency + ", type: " + type +
        ", field_uk_domain: " + field_uk_domain + ", field_url: " + field_url.size() + ", bodies: " + bodies.size() +
        ", field_description: " + field_description.size() + ", field_uk_postal_address_url: " + field_uk_postal_address_url.size() +
        ", field_suggested_collections: " + field_suggested_collections.size() + ", field_collections: " + field_collections.size() +
        ", field_license: " + field_license.size() + ", field_collection_categories: " + field_collection_categories.size() +
        ", field_notes: " + field_notes.size() + ", field_instances: " + field_instances.size() + 
        ", field_subject: " + field_subject.size();
    }

}

