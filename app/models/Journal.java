package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Journal extends Model {
	
	public Journal(Document document) {
		this.document = document;
	}
	@Id
	public Long id;
	@OneToOne
	@JoinColumn(name="id_document")
	public Document document;
	@ManyToOne
	@JoinColumn(name="id_journal_title")
	public JournalTitle journalTitle;
	@Required
    public Integer publicationYear;
    @Required
	public String volume;
	public String issue;
	public String author;
}