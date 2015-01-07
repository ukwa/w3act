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
import uk.bl.Const;

@Entity
public class JournalTitle extends Model {
	
    @Id
    public Long id;
	@ManyToOne
	@JoinColumn(name="id_watched_target")
	public WatchedTarget watchedTarget;
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = Const.SUBJECT_JOURNAL_TITLE,
		joinColumns = { @JoinColumn(name = "id_journal_title", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_taxonomy", referencedColumnName="id") })
	public List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();;
	@Required
    public String title;
	public String issn;
	public String frequency;
	@Required
	public String publisherName;
	public String language;
	public String blCollectionSubset;
	@Transient
	public String subject;
	
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