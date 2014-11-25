package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	public String documentUrlScheme;
	public String getUrl() { return ""+id; }
	public String getName() { return target.title; }
	
	public static final Model.Finder<Long, WatchedTarget> find = new Model.Finder<>(Long.class, WatchedTarget.class);
	
	public WatchedTarget(User user, String title, String url, String field_url, String documentUrlScheme) {
		this.user = user;
		this.documentUrlScheme = documentUrlScheme;
		target = new Target(title, url);
		target.field_url = field_url;
	}
	
    public WatchedTarget(Target target, String documentUrlScheme) {
		this.target = target;
		user = User.findByUrl(target.author);
		this.documentUrlScheme = documentUrlScheme;
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
