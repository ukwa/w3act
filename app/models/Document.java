package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import uk.bl.Const;

@Entity
public class Document extends Model {
	
    @Id
    public Long id;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_instance")
	public Instance instance;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_watched_target")
	public WatchedTarget watchedTarget;
	public boolean submitted;
	@OneToOne(mappedBy="document", cascade=CascadeType.REMOVE) @JsonIgnore
	public Book book;
	@OneToOne(mappedBy="document", cascade=CascadeType.REMOVE) @JsonIgnore
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
    public Integer publicationYear;
    @Required
	public String filename;
    public boolean priorityCataloguing;
    public boolean mbsPortal;
    public boolean enviaPortal;
    public boolean otherPortal;
    public String type;
	public String author1Fn;
	public String author1Ln;
	public String author2Fn;
	public String author2Ln;
	public String author3Fn;
	public String author3Ln;
    public String getUrl() { return ""+id; }
    
    public static final Model.Finder<Long, Document> find = new Model.Finder<>(Long.class, Document.class);
    
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        String required = "This field is required";
        if (isJournalArticleOrIssue()) {
        	if (journal.journalTitleId == null)
                errors.add(new ValidationError("journal.journalTitleId", required));
        	if (journal.volume.isEmpty() && journal.issue.isEmpty())
                errors.add(new ValidationError("journal.issue", "Complete Volume or Issue/Part or both"));
        } else if (isWholeBook()) {
        	if (book.publisher.isEmpty())
        		errors.add(new ValidationError("book.publisher", required));
        }
        return errors.isEmpty() ? null : errors;
    }
    
    public boolean isWholeBook() { return type != null && type.equals(DocumentType.BOOK.toString()); }
    
    public boolean isBookChapter() { return type != null && type.equals(DocumentType.BOOK_CHAPTER.toString()); }
    
    public boolean isJournalArticle() { return type != null && type.equals(DocumentType.JOURNAL_ARTICLE.toString()); }
    
    public boolean isJournalIssue() { return type != null && type.equals(DocumentType.JOURNAL_ISSUE.toString()); }
	
	public boolean isBookOrBookChapter() { return isWholeBook() || isBookChapter(); }
	
	public boolean isJournalArticleOrIssue() { return isJournalArticle() || isJournalIssue(); }
    
    public void clearImproperFields() {
    	if (isJournalIssue()) {
    		author1Fn = author1Ln = author2Fn = author2Ln = author3Fn = author3Ln = "";
    	}
    }
    
    public static Page<Document> page(Long watchedTargetId, boolean submitted, int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().eq("id_watched_target", watchedTargetId)
        		.eq("submitted", submitted)
        		.icontains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
}