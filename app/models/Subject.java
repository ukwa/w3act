package models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("subject")
public class Subject extends Taxonomy {

	private static final long serialVersionUID = 3535758346565569620L;

	@JsonIgnore
    @ManyToOne
	@JoinColumn(name = "parent_id")
	public Subject parent;
	
    @OneToMany(mappedBy="parent")
	public List<Subject> children;
	
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
	       return rootSubjects;
	}
	
	public static List<Subject> findChildrenByParentId(Long parentId) {
		return find.where().eq("t0.parent_id", parentId).order().asc("name").findList();
	}
	
	public static Page<Subject> pager(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().contains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

}
