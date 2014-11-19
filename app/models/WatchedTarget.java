package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

@Entity
public class WatchedTarget extends Model {
	@Id
	public Long id;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_target")
	public Target target;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_creator")
	public User user;
	public boolean deepDocumentSearch;
	public String getUrl() { return ""+id; }
	public String getName() { return target.field_url; }
	
	public static final Model.Finder<Long, WatchedTarget> find = new Model.Finder<>(Long.class, WatchedTarget.class);
	
	public WatchedTarget (User user, String title, String url, String field_url, boolean deepDocumentSearch) {
		this.user = user;
		this.deepDocumentSearch = deepDocumentSearch;
		target = new Target(title, url);
		target.field_url = field_url;
	}
	
    public static Page<WatchedTarget> page(User user, int page, int pageSize, String sortBy, String order, String filter) {
    	
        return find.where().join("target").where()
        		.eq("id_creator", user.uid)
        		.icontains("target.field_url", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    } 
}
