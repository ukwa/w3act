package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import play.db.ebean.Model;
import uk.bl.Const;

@Entity
@DiscriminatorValue("quality_issues")
public class QaIssue extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2451400178750071013L;

	public static Model.Finder<Long,QaIssue> find = new Model.Finder<Long, QaIssue>(Long.class, QaIssue.class);

	public QaIssue() {}
	
    public QaIssue(String name, String description) {
    	super(name, description);
    }
    
    public static QaIssue findByName(String name) {
    	return find.where().eq(Const.NAME, name).findUnique();
    }
    
    public static QaIssue findByUrl(String url) {
    	QaIssue QualityIssue = find.where().eq(Const.URL, url).findUnique();
    	return QualityIssue;
    }
}
