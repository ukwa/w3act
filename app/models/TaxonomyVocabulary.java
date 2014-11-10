package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;


/**
 * Taxonomy vocabulary entity managed by Ebean
 */
@Entity 
@Table(name = "TaxonomyVocabulary")
public class TaxonomyVocabulary extends ActModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4407791639097588217L;

    public String name;  
    public String machine_name;
    @Column(columnDefinition = "text")
    public String description;
    public Long termCount;
    
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

