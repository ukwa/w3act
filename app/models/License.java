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
@DiscriminatorValue("licenses")
public class License extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = Const.LICENSE_TARGET, joinColumns = { @JoinColumn(name = "license_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
	public List<Target> targets;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,License> find = new Model.Finder(Long.class, License.class);
    
    public static License findByUrl(String url) {
    	License license = find.where().eq(Const.URL, url).findUnique();
    	return license;
    }
    
    public static List<License> findAllLicenses() {
    	return find.all();
    }
}
