package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Portal extends Model {

	@Id
	public Long id;
	public String title;
	
	public Portal(String title) {
		this.title = title;
	}
	
	public static final Model.Finder<Long, Portal> find = new Model.Finder<>(Long.class, Portal.class);
}