package models;

import java.util.ArrayList;
import java.util.List;

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
	@JoinColumn(name="id_target")
	public Target target;
	@ManyToMany
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
	public boolean priorityCataloguing;
	public String blCollectionSubset;
	@Transient
	public String subject;

}