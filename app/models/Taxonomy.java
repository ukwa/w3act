package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Taxonomy entity managed by Ebean
 */
@Entity 
public class Taxonomy extends Model {

    @Id
    public Long vid;
    public String name;  
    public String machine_name;
    public String description;
    public Long term_count;
      
    public Taxonomy(String name) {
        this.name = name;
    }
    
    public Taxonomy() {
    }
    
    // -- Queries
    
    public static Model.Finder<Long,Taxonomy> find = new Model.Finder(Long.class, Taxonomy.class);
    
    /**
     * Retrieve Taxonomy for user
     */
    public static List<Taxonomy> findInvolving() {
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
        
    public String toString() {
        return "Taxonomy(" + vid + ") with name: " + name;
    }

}

