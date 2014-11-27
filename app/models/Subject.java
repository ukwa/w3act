package models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("subjects")
public class Subject extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3535758346565569620L;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.SUBJECT_TARGET, joinColumns = { @JoinColumn(name = "subject_id", referencedColumnName="id") },
			inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
	public List<Target> targets;

	@SuppressWarnings("rawtypes")
	public static Model.Finder<Long,Subject> find = new Model.Finder(Long.class, Subject.class);

    public static Subject findByUrl(String url) {
    	Subject subject = find.where().eq(Const.URL, url).findUnique();
    	return subject;
    }
}
