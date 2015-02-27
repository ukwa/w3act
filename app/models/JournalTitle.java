package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class JournalTitle extends Model {
	
    @Id
    public Long id;
	@ManyToOne
	@JoinColumn(name="id_watched_target")
	public WatchedTarget watchedTarget;
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = "bl_collection_subset_journal_title",
		joinColumns = { @JoinColumn(name = "id_journal_title", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_bl_collection_subset", referencedColumnName="id") })
	public List<BlCollectionSubset> blCollectionSubsets = new ArrayList<>();
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = "subject_journal_title",
		joinColumns = { @JoinColumn(name = "id_journal_title", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_taxonomy", referencedColumnName="id") })
	public List<Subject> subjects = new ArrayList<>();
	@Required
    public String title;
	public String issn;
	public String frequency;
	@Required
	public String publisherName;
	public String language;
	@Transient
	public String subject;
	
	public static final Model.Finder<Long, JournalTitle> find = new Model.Finder<>(Long.class, JournalTitle.class);
	
	public static List<String> frequencies() {
		List<String> frequencies = new ArrayList<String>();
		Frequency[] values = Frequency.values();
		for (int i=0; i < values.length; i++) {
			frequencies.add(values[i].toString());
	    }
		return frequencies;
	}
	
	public enum Frequency {
		NO_FREQUENCY(""),
		MONTHLY("Monthly"),
		QUARTERLY("Quarterly"),
		TWO_ISSUES_PER_YEAR("Two Issues per Year"),
		YEARLY("Yearly");
		
        private String value;

        private Frequency(String value) {
                this.value = value;
        }
        
        public String toString() {
        	return value;
        }
    }
	
}