package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

@Entity
public class WatchedTarget extends Model {
	@Id
	public Long id;
	@ManyToOne
	@JoinColumn(name="id_creator")
	public User user;
	public String fieldUrl;
	public boolean deepDocumentSearch;
	
	public static final Model.Finder<Long, WatchedTarget> find = new Model.Finder<>(Long.class, WatchedTarget.class);
	
	public WatchedTarget (User user, String url, boolean deepDocumentSearch) {
		this.user = user;
		this.fieldUrl = url;
		this.deepDocumentSearch = deepDocumentSearch;
	}
}
