package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

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
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = Const.BL_COLLECTION_SUBSET_BOOK,
		joinColumns = { @JoinColumn(name = "id_book", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_bl_collection_subset", referencedColumnName="id") })
	public List<BlCollectionSubset> blCollectionSubsets = new ArrayList<>();
	public String isbn;
	public String corporateAuthor;
	public String series;
	@Required
	public String publisher;
	public String edition;
	
}