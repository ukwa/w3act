package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Page;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import uk.bl.Const;

@Entity
public class Document extends Model {
	
    @Id
    public Long id;
	@ManyToOne
	@JoinColumn(name="id_instance")
	public Instance instance;
	@OneToOne(mappedBy="document")
	public Book book;
	@OneToOne(mappedBy="document")
	public Journal journal;
    public String landingPageUrl;
    public String documentUrl;
    @Transient
    public String htmlFilename;
    @Required
    @Column(columnDefinition = "TEXT")
	public String title;
	public String doi;
	@DateTime(pattern="dd-MM-yyyy")
	public Date publicationDate;
    @Required
	public String filename;
    public String type;
    
    public static final Model.Finder<Long, Document> find = new Model.Finder<Long, Document>(Long.class, Document.class);
    
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        String required = "This field is required";
        if (type.toLowerCase().startsWith("journal")) {
        	if (journal.journalTitleId == null)
                errors.add(new ValidationError("journal.journalTitleId", required));
        	if (journal.publicationYear == null)
                errors.add(new ValidationError("journal.publicationYear", required));
        	if (journal.volume.isEmpty() && journal.issue.isEmpty())
                errors.add(new ValidationError("journal.issue", "Complete Volume or Issue/Part or both"));
        } else if (type.toLowerCase().startsWith("book")) {
        	if (type.toLowerCase().equals("book")) {
        		if (book.publisher.isEmpty())
        			errors.add(new ValidationError("book.publisher", required));
        		if (book.publicationYear == null)
        			errors.add(new ValidationError("book.publicationYear", required));
        	}
        }
        return errors.isEmpty() ? null : errors;
    }
    
    public static Page<Document> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
}