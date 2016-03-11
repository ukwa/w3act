package models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.fasterxml.jackson.annotation.JsonIgnore;

import controllers.Portals;
import controllers.Users;
import controllers.WaybackController;
import play.Play;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;

@Entity
public class Document extends Model {
	
    /** */
	private static final long serialVersionUID = 4697602797902508209L;
	
	@Id
    public Long id;
	
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_watched_target")
	public WatchedTarget watchedTarget;
	
	public String waybackTimestamp;
	public Status status;
	public Date currentStatusSet;
	
	@OneToOne(mappedBy="document", cascade=CascadeType.REMOVE) @JsonIgnore
	public Book book;
	
	@OneToOne(mappedBy="document", cascade=CascadeType.REMOVE) @JsonIgnore
	public Journal journal;
	
	@ManyToMany(cascade=CascadeType.ALL) @JsonIgnore
	@JoinTable(name = "portal_document",
			joinColumns = { @JoinColumn(name = "id_document", referencedColumnName="id") },
			inverseJoinColumns = { @JoinColumn(name = "id_portal", referencedColumnName="id") })
	public List<Portal> portals = new ArrayList<>();
	
	@ManyToMany(cascade=CascadeType.REMOVE) @JsonIgnore
	@JoinTable(name = "fast_subject_document",
		joinColumns = { @JoinColumn(name = "id_document", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "id_fast_subject", referencedColumnName="id") })
	public List<FastSubject> fastSubjects = new ArrayList<>();
	
    public String landingPageUrl;
    public String documentUrl;
    public String sha256Hash;
    public String ctpHash;
    
    @Column(columnDefinition = "text")
	public String title;
    
	public String doi;
	
	/* The Logical ARK for the Logical Document */
	public String ark;
	
	/* The Logical MD-ARK for the Document Metadata (not used by eJournals here)*/
	public String md_ark;
	
	/* The Logical D-ARK for the Document Data (bitstream) */
	public String d_ark;

	/* The Logical D-ARK for the Mets Document */
	public String mets_d_ark;

	@DateTime(pattern="dd-MM-yyyy")
	public Date publicationDate;
	
	@Required
    public Integer publicationYear;
	
    @Required
	public String filename;
    
    public Long size;
    public boolean priorityCataloguing;
    public String type;
	public String author1Fn;
	public String author1Ln;
	public String author2Fn;
	public String author2Ln;
	public String author3Fn;
	public String author3Ln;
    
    public static final Model.Finder<Long, Document> find = new Model.Finder<>(Long.class, Document.class);
    
    public String htmlFilename() {
    	return id + ".html";
    }
    
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        String required = "This field is required";
        // Title required unless a journal issue:
        if( ! this.isJournalIssue() ) {
        	if( StringUtils.isEmpty(this.title)) {
                errors.add(new ValidationError("title", "A title is required for all documents except journal issues"));
        	}
        }
        // Sub-cases
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
    
    public boolean isWholeBook() { return type != null && type.equals(Type.BOOK.toString()); }
    
    public boolean isBookChapter() { return type != null && type.equals(Type.BOOK_CHAPTER.toString()); }
    
    public boolean isJournalArticle() { return type != null && type.equals(Type.JOURNAL_ARTICLE.toString()); }
    
    public boolean isJournalIssue() { return type != null && type.equals(Type.JOURNAL_ISSUE.toString()); }
	
	public boolean isBookOrBookChapter() { return isWholeBook() || isBookChapter(); }
	
	public boolean isJournalArticleOrIssue() { return isJournalArticle() || isJournalIssue(); }
	
	public boolean isOwnedBy(User user) {
		return user.id.equals(watchedTarget.target.authorUser.id);
	}
	
	public boolean isEditableFor(User user) {
		return isOwnedBy(user) || user.hasExpertUserRights();
	}
    
	public boolean hasPermissionForService(Portal portal) {
		List<License> licenses = watchedTarget.target.licenses;
		if (licenses.isEmpty())
			return false;
		else
			return licenses.get(0).portals.contains(portal);
	}
	
    public void clearImproperFields() {
    	if (isJournalIssue()) {
    		author1Fn = author1Ln = author2Fn = author2Ln = author3Fn = author3Ln = "";
    	}
    }
    
    public static Query<Document> query(DocumentFilter documentFilter, String sortBy, String order, String filter) {

    	ExpressionList<Document> el = find.where();
    	if (documentFilter.watchedtarget != null) {
    		el = el.eq("id_watched_target", documentFilter.watchedtarget);
    	} else if (documentFilter.user != null) {
    		el = el.in("id_watched_target", Users.getWatchedTargetIds(documentFilter.user));
    	}
    	if (documentFilter.service != null && !documentFilter.service.isEmpty()) {
    		el = el.in("id", Portals.getDocumentIds(documentFilter.service));
    	}
    	if (documentFilter.fastSubjects != null && !documentFilter.fastSubjects.isEmpty()) {
    		el = el.in("fastSubjects.fastId", documentFilter.fastSubjects);
    	}
    	SimpleDateFormat waybackFormat = new SimpleDateFormat("yyyyMMdd");
    	if (documentFilter.startdate != null) {
    		el = el.gt("waybackTimestamp", waybackFormat.format(documentFilter.startdate));
    	}
    	if (documentFilter.enddate != null) {
    		el = el.lt("waybackTimestamp", waybackFormat.format(documentFilter.enddate));
    	}
    	
    	return el.eq("status", documentFilter.status.ordinal()).add(Expr.or(
					Expr.icontains("documentUrl", filter), 
					Expr.icontains("title", filter))
    			).orderBy(sortBy + " " + order);
    }
    
    public static Page<Document> page(DocumentFilter documentFilter, int page,
    		int pageSize, String sortBy, String order, String filter) {
    	return query(documentFilter, sortBy, order, filter)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
    public String getType() {
    	return this.type;
    }

	public void setStatus(Status status) {
		this.status = status;
		currentStatusSet = new Date();
	}
	
	public boolean isSubmissable() {
		if( type != null ) {
			return true;
		}
		return false;
	}
	
	public String currentStatusSetUTCString() {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatGmt.format(currentStatusSet);
	}
	
	public boolean hasARKs() {
		if( StringUtils.isEmpty(ark) ||  StringUtils.isEmpty(md_ark) ||  StringUtils.isEmpty(d_ark) ){
			return false;
		}
		return true;
	}
	
	public String waybackUrl() {
		return WaybackController.getWaybackEndpoint() +
				waybackTimestamp + "/" + documentUrl;
	}
	
	public String globalAccessDocumentUrl() {
		return WaybackController.getAccessResolverEndpoint() + 
				waybackTimestamp + "/" + documentUrl;
	}
	
	public String actualSourceUrl() {
		return waybackTimestamp != null ? waybackUrl() : documentUrl;
	}

	public static String getPdf2HtmlEndpoint() {
		return Play.application().configuration().getString("application.pdftohtmlex.url");
	}
	
	public String pdf2htmlUrl() throws UnsupportedEncodingException {
		return getPdf2HtmlEndpoint()+URLEncoder.encode(this.waybackUrl(), "UTF-8");
	}
	
	public enum Type {
    	BOOK ("Book"),
    	BOOK_CHAPTER ("Book Chapter"),
    	JOURNAL_ARTICLE ("Journal Article"),
    	JOURNAL_ISSUE ("Journal Issue");
    	
    	private final String name;
    	
    	Type(String name) {
    		this.name = name;
    	}
    	
    	@Override
    	public String toString() {
    		return name;
    	}
    }
    
	public enum Status {
		NEW,
		SAVED,
		SUBMITTED,
		IGNORED,
		DELETED;
    }

	@Override
	public String toString() {
		return "Document [id=" + id + ", waybackTimestamp=" + waybackTimestamp
				+ ", status=" + status + ", currentStatusSet="
				+ currentStatusSet + ", book=" + book + ", journal=" + journal
				+ ", portals=" + portals + ", fastSubjects=" + fastSubjects
				+ ", landingPageUrl=" + landingPageUrl + ", documentUrl="
				+ documentUrl + ", sha256Hash=" + sha256Hash + ", ctpHash="
				+ ctpHash + ", title=" + title + ", doi=" + doi + ", ark="
				+ ark + ", publicationDate=" + publicationDate
				+ ", publicationYear=" + publicationYear + ", filename="
				+ filename + ", size=" + size + ", priorityCataloguing="
				+ priorityCataloguing + ", type=" + type + ", author1Fn="
				+ author1Fn + ", author1Ln=" + author1Ln + ", author2Fn="
				+ author2Fn + ", author2Ln=" + author2Ln + ", author3Fn="
				+ author3Fn + ", author3Ln=" + author3Ln + "]";
	}
    
}