package models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.avaje.ebean.Page;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("subject")
public class Subject extends Taxonomy {

	private static final long serialVersionUID = 3535758346565569620L;

	public static Model.Finder<Long,Subject> find = new Model.Finder<Long, Subject>(Long.class, Subject.class);

    /**
     * Retrieve all collections.
     */
    public static List<Subject> findAllSubjects() {
        return find.orderBy("name asc").findList();
    }

    public static Subject findById(Long id) {
    	Subject subject = find.byId(id);
    	return subject;
    }

	public static Subject findByUrl(String url) {
    	Subject subject = find.where().eq(Const.URL, url).findUnique();
    	return subject;
    }
    
	public static List<Subject> getFirstLevelSubjects() {
	       List<Subject> rootSubjects = find.where().isNull("parent").order().asc("name").findList();
//	       Logger.info("getFirstLevelSubjects list size: " + rootSubjects.size());
	       return rootSubjects;
	}
	
	public static List<Subject> findChildrenByParentId(Long parentId) {
		return find.where().eq("t0.parent_id", parentId).order().asc("name").findList();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static Page<Subject> pager(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().contains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

}
