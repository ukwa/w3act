package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import controllers.Users;
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
	@ManyToMany(cascade=CascadeType.ALL) @JsonIgnore
	@JoinTable(name = Const.PORTAL_DOCUMENT,
			joinColumns = { @JoinColumn(name = "id_document", referencedColumnName="id") },
			inverseJoinColumns = { @JoinColumn(name = "id_portal", referencedColumnName="id") })
	public List<Portal> portals = new ArrayList<>();
    public String landingPageUrl;
    public String documentUrl;
    public String sha256Hash;
    @Required
    @Column(columnDefinition = "TEXT")
	public String title;
	public String doi;
	public String ark;
	@DateTime(pattern="dd-MM-yyyy")
	public Date publicationDate;
	@Required
    public Integer publicationYear;
    @Required
	public String filename;
    public boolean priorityCataloguing;
    public String type;
	public String author1Fn;
	public String author1Ln;
	public String author2Fn;
	public String author2Ln;
	public String author3Fn;
	public String author3Ln;
    public String getUrl() { return ""+id; }
    
    public static final Model.Finder<Long, Document> find = new Model.Finder<>(Long.class, Document.class);
    
    public String getHtmlFilename() {
    	return id + ".html";
    }
    
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
    
    public static Page<Document> page(Long userId, Long watchedTargetId, boolean submitted, int page, int pageSize, String sortBy, String order, String filter) {

    	ExpressionList<Document> el = find.where();
    	if (watchedTargetId != null) {
    		el = el.eq("id_watched_target", watchedTargetId);
    	} else if (userId != null) {
    		//el = el.eq("watched_target.id_creator", userId);
    		el = el.in("id_watched_target", Users.getWatchedTargetIds(userId));
    	}
    	
    	return el.eq("submitted", submitted)
        		.icontains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
}