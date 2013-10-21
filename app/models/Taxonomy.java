package models;

import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;


/**
 * Taxonomy entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Taxonomy extends Model {
     
    @Id
    public Long tid;
    public String name;  
    @Column(columnDefinition = "TEXT")
    public String description;
    public Long weight;
    public Long node_count;
    @Column(columnDefinition = "TEXT")
    public String url;
    @Column(columnDefinition = "TEXT")
    public String vocabulary;
    public Long feed_nid;    
    // lists
    @Column(columnDefinition = "TEXT") 
    public String field_owner;
    @Column(columnDefinition = "TEXT") 
    public String parent;
    @Column(columnDefinition = "TEXT") 
    public String parents_all;

    public Taxonomy(String name) {
        this.name = name;
    }
    
    public Taxonomy() {
    }
    
    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,Taxonomy> find = new Model.Finder(Long.class, Taxonomy.class);
    
    /**
     * Retrieve Taxonomy for user
     */
    public static List<Taxonomy> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve all Taxonomies
     */
    public static List<Taxonomy> findAll() {
        return find.all();
    }
    
    /**
     * Create a new Taxonomy.
     */
    public static Taxonomy create(String name) {
        Taxonomy Taxonomy = new Taxonomy(name);
        Taxonomy.save();
        return Taxonomy;
    }
    
    /**
     * Rename a Taxonomy
     */
    public static String rename(Long TaxonomyId, String newName) {
        Taxonomy Taxonomy = (Taxonomy) find.ref(TaxonomyId);
        Taxonomy.name = newName;
        Taxonomy.update();
        return newName;
    }
    
    /**
     * Retrieve a Taxonomy by URL.
     * @param url
     * @return taxonomy name
     */
    public static Taxonomy findByUrl(String url) {
    	Taxonomy res = new Taxonomy();
    	res.name = url;
    	try {
    		find.where().eq("url", url).findUnique();
    	} catch (Exception e) {
    		Logger.info("Cann't find taxonomy for url: " + url + ". " + e);
    	}
//        return find.where().eq("url", url).findUnique();
    	return res;
    }
        
    public String toString() {
        return "Taxonomy(" + tid + ") with name: " + name;
    }

}

