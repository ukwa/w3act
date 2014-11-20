package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("quality issue")
public class QualityIssue extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2451400178750071013L;

	@SuppressWarnings("rawtypes")
	public static Model.Finder<Long,QualityIssue> find = new Model.Finder(Long.class, QualityIssue.class);

    public QualityIssue(String name, String description) {
    	super(name, description);
    }
    
    public static QualityIssue findByName(String name) {
    	return find.where().eq(Const.NAME, name).findUnique();
    }
}
