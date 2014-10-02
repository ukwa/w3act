package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Journal extends Model {
	
    @Id
    public String title;
	@OneToOne
	@JoinColumn(name="id_document")
	public Document document;
    public Integer publication_year;
    @Required
	public String volume;
	public String issue;

}