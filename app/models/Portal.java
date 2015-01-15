package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Portal extends Model {

	@Id
	public Long id;
	public String title;
	public boolean active;
	
	public static final Model.Finder<Long, Portal> find = new Model.Finder<>(Long.class, Portal.class);
	
	/**
	 * @param title must not be null
	 */
	public Portal(String title) {
		this.title = title;
		active = true;
	}
	
	@Override
	public int hashCode() {
		return title.hashCode();
    }
	
	@Override
    public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Portal) {
			Portal other = (Portal) obj;
			return title.equals(other.title);
		} else {
			return false;
		}
	}
}