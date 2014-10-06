package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Page;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

@Entity
public class Document extends Model {
	
    @Id
    public Long id;
	@ManyToOne
	@JoinColumn(name="id_instance")
	public Instance instance;
    public String landingPageUrl;
    public String documentUrl;
    @Required
    @Column(columnDefinition = "TEXT")
	public String title;
	public String doi;
	@DateTime(pattern="dd/MM/yyyy")
	public Date publicationDate;
    @Required
	public String filename;
    
    public static final Model.Finder<Long, Document> find = new Model.Finder<Long, Document>(Long.class, Document.class);
	
    public static Page<Document> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
    public static Document findById(Long id) {
    	Document res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }
    
}