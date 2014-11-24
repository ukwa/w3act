package models;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.*;

/**
 * Taxonomy vocabulary entity managed by Ebean
 */
@Entity 
@Table(name = "taxonomy_vocabulary")
public class TaxonomyVocabulary extends ActModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4407791639097588217L;

	public final static String TAXONOMY_VOCABULARY = "taxonomy_vocabulary";
	
	public final static String COLLECTION ="collections";
	
	public final static String COLLECTION_AREAS = "collection_areas";
	
	public final static String LICENSES = "licenses";
	
	public final static String QUALITY_ISSUES = "quality_issues";
	
	public final static String SUBJECT = "subject";
	
	public final static String TAGS = "tags";
	
	@JsonIgnore
	@OneToMany(mappedBy = "taxonomyVocabulary", cascade = CascadeType.PERSIST)
	public List<Taxonomy> taxonomies;
	
    public String name;  
    public String machine_name;
    @Column(columnDefinition = "text")
    public String description;
    
    private Long vid;

    @Transient 
    public Long term_count;

    @Transient
    private String url;
    
    //	{"vid":"5","name":"Web Archive Collections","machine_name":"collections","description":"Taxonomy for structuring collections.","term_count":"160"},
    
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
    
    public static TaxonomyVocabulary findByVid(Long vid) {
    	TaxonomyVocabulary tv = find.where().eq("vid", vid).findUnique();
    	return tv;
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
    
    public static TaxonomyVocabulary findByMachineName(String machineName) {
    	TaxonomyVocabulary taxonomyVocabulary = find.where().eq("machine_name", machineName).findUnique();
    	return taxonomyVocabulary;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public Long getTerm_count() {
		return term_count;
	}

	public void setTerm_count(Long term_count) {
		this.term_count = term_count;
	}

	public String toString() {
        return "Taxonomy vocabulary (" + id + ") with name: " + name;
    }

}

