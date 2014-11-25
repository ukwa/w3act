package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("subjects")
public class Subject extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3535758346565569620L;

	@SuppressWarnings("rawtypes")
	public static Model.Finder<Long,Subject> find = new Model.Finder(Long.class, Subject.class);

    public static Subject findByUrl(String url) {
    	Subject subject = find.where().eq(Const.URL, url).findUnique();
    	return subject;
    }
}
