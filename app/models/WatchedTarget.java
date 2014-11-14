package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

@Entity
public class WatchedTarget extends Model {
	@Id
	public Long id;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_creator")
	public User user;
	public String fieldUrl;
	public boolean deepDocumentSearch;
	public String getUrl() { return ""+id; }
	public String getName() { return fieldUrl; }
	
	public static final Model.Finder<Long, WatchedTarget> find = new Model.Finder<>(Long.class, WatchedTarget.class);
	
	public WatchedTarget (User user, String url, boolean deepDocumentSearch) {
		this.user = user;
		this.fieldUrl = url;
		this.deepDocumentSearch = deepDocumentSearch;
	}
	
    public static Page<WatchedTarget> page(User user, int page, int pageSize, String sortBy, String order, String filter) {
    	
        return find.where().eq("id_creator", user.uid)
        		.icontains("fieldUrl", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    } 
}
