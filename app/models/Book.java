package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
	
	public Book(Document document) {
		this.document = document;
	}
	@Id
	public Long id;
	@OneToOne
	@JoinColumn(name="id_document")
	public Document document;
	public String isbn;
	public String corporateAuthor;
	public boolean priorityCataloguing;
	public String series;
	@Required
	public String publisher;
	public String edition;
	@Required
    public Integer publicationYear;
	
}