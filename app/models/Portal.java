package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import uk.bl.configurable.Configurable;

@Entity
public class Portal extends Model implements Configurable {

	@Id
	public Long id;
	public String title;
	public boolean active;
	@ManyToMany(mappedBy="portals")
	public List<Document> documents = new ArrayList<>();
	
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

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}