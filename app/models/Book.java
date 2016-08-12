package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
	
	/** */
	private static final long serialVersionUID = -1862880935979934744L;
	
	public Book(Document document) {
		this.document = document;
	}
	@Id
	public Long id;
	@OneToOne
	@JoinColumn(name="id_document")
	public Document document;
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = "bl_collection_subset_book",
		joinColumns = { @JoinColumn(name = "id_book", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_bl_collection_subset", referencedColumnName="id") })
	public List<BlCollectionSubset> blCollectionSubsets = new ArrayList<>();
	public String isbn;
	public String printIsbn;
	public String corporateAuthor;
    public String corporateAuthorSubordinateUnit;
    public String corporateAuthor2;
    @Column(name = "corporate_author2_subordinate_unit")
    public String corporateAuthor2SubordinateUnit;
    public String corporateAuthor3;
    @Column(name = "corporate_author3_subordinate_unit")
    public String corporateAuthor3SubordinateUnit;
    public String governmentBody;
    public String governmentBodySubordinateUnit;
    public String governmentBody2;
    @Column(name = "government_body2_subordinate_unit")
    public String governmentBody2SubordinateUnit;
    public String governmentBody3;
    @Column(name = "government_body3_subordinate_unit")
    public String governmentBody3SubordinateUnit;
    public String series;
	public String seriesNumber;
    public String partNumber;
    public String partName;
	@Required
	public String publisher;
	public String edition;
	
}