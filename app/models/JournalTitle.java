package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class JournalTitle extends Model {
	
    @Id
    public Long id;
	@ManyToOne
	@JoinColumn(name="id_target")
	public Target target;
	@Required
    public String title;
	public String issn;
	public String frequency;
	@Required
	public String publisherName;
	public String language;
	public String priorityCataloguing;
	public String blCollectionSubset;
	public String subject;

}