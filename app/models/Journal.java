package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Journal extends Model {
	
	/** */
	private static final long serialVersionUID = 6919765250249408178L;
	
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
	@Transient
	public Long journalTitleId;
	public String volume;
	public String issue;

}