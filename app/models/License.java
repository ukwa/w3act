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

	private static final long serialVersionUID = 1L;

	public enum LicenseStatus {

		GRANTED("Granted"),
		NOT_INITIATED("Not Initiated"),
		QUEUED("Queued"),
		PENDING("Pending"),
		REFUSED("Refused"),
		EMAIL_REJECTED("Email Rejected"),
		SUPERSEDED("Superseded");
		
        private String value;

        private LicenseStatus(String value) {
                this.value = value;
        }
        
        public String getValue() {
        	return value;
        }
	}
	
	public String status;
	
//	A target should have only one Open UKWA Licence, but could also have one of the other licence types inherited from SPT/WCT (from before 2014), 
//	and/or have one of the globally applied licenses such as that from the HLF. These are agreed outside ACT for a group of targets, and then 
//	manually applied by the Archivist to the target record.
    
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
    
    public boolean hasStatus(String status) {
    	return (this.status.equals(status));
    }
    
    public boolean isGranted() {
    	return this.hasStatus(LicenseStatus.GRANTED.name());
    }
    
    public boolean isNotInitiated() {
    	return this.hasStatus(LicenseStatus.NOT_INITIATED.name());
    }
    
    public boolean isQueued() {
    	return this.hasStatus(LicenseStatus.QUEUED.name());
    }
    
    public boolean isPending() {
    	return this.hasStatus(LicenseStatus.PENDING.name());
    }
    
    public boolean isRefused() {
    	return this.hasStatus(LicenseStatus.REFUSED.name());
    }
    
    public boolean isEmailRejected() {
    	return this.hasStatus(LicenseStatus.EMAIL_REJECTED.name());
    }
    
    public boolean isSuperseded() {
    	return this.hasStatus(LicenseStatus.SUPERSEDED.name());
    }
}
