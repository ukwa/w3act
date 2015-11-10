package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

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

    public static QaIssue findById(Long id) {
    	return find.byId(id);
    }
    
    public static List<QaIssue> findAllQaIssue() {
    	return find.all();
    }
    
    public static QaIssue findByName(String name) {
    	return find.where().eq(Const.NAME, name).findUnique();
    }
    
    public static QaIssue findByUrl(String url) {
    	QaIssue QualityIssue = find.where().eq(Const.URL, url).findUnique();
    	return QualityIssue;
    }

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(QaIssue c: find.all()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
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
		QaIssue other = (QaIssue) obj;
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
	
	@Override
    public String toString() {
        return "QaIssue(" + name + ")" + ", id:" + id;
    }
}
