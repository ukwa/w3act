package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;


/**
 * Taxonomy vocabulary entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name = "taxonomy_vocabulary")
public class TaxonomyVocabulary extends Model {

    @Id
    public Long id;
    public String name;  
    public String machine_name;
    @Column(columnDefinition = "TEXT")
    public String description;
    public Long term_count;
    
    public TaxonomyVocabulary() {
    	super();
    }
      
    public TaxonomyVocabulary(String name) {
        this.name = name;
    }
    
    
    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<Long,TaxonomyVocabulary> find = new Model.Finder(Long.class, TaxonomyVocabulary.class);
    
    /**
     * Retrieve Taxonomy for user
     */
    public static List<TaxonomyVocabulary> findInvolving() {
        return find.all();
    }
    
    /**
     * Create a new Taxonomy.
     */
    public static TaxonomyVocabulary create(String name) {
        TaxonomyVocabulary Taxonomy = new TaxonomyVocabulary(name);
        Taxonomy.save();
        return Taxonomy;
    }
    
    /**
     * Rename a Taxonomy
     */
    public static String rename(Long TaxonomyId, String newName) {
        TaxonomyVocabulary Taxonomy = (TaxonomyVocabulary) find.ref(TaxonomyId);
        Taxonomy.name = newName;
        Taxonomy.update();
        return newName;
    }
        
    public String toString() {
        return "Taxonomy vocabulary (" + id + ") with name: " + name;
    }

}

