package models;

import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import uk.bl.Const;


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
//        Logger.info("taxonomy url: " + url);
        // in order to replace "taxonomy_term" read from target.collection_categories by "taxonomy/term"
        url = url.replace("_", "/"); 
        Taxonomy res = find.where().eq(Const.URL, url).findUnique();
//        Logger.info("taxonomy name: " + res.name);
//        return find.where().eq(Const.URL, url).findUnique();
    	return res;
    }
        
    public String toString() {
        return "Taxonomy(" + tid + ") with name: " + name;
    }

}

