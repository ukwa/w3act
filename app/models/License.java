package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        
    	public static LicenseStatus getLicenseStatus(String name) {
    		for (LicenseStatus licenseStatus : LicenseStatus.values()) {
    			if (licenseStatus.name().equals(name)) {
    				return licenseStatus;
    			}
    		}
    		return null;
    	}
    	public static Map<String,String> options() {
            LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
            for(LicenseStatus t: LicenseStatus.values()) {
                options.put(t.name(), t.getValue());
            }
            return options;		
    	}
	}
	
	public String status;
	
//	A target should have only one Open UKWA Licence, but could also have one of the other licence types inherited from SPT/WCT (from before 2014), 
//	and/or have one of the globally applied licenses such as that from the HLF. These are agreed outside ACT for a group of targets, and then 
//	manually applied by the Archivist to the target record.
    
	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "license_target", joinColumns = { @JoinColumn(name = "license_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
	public List<Target> targets;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "license_instance", joinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "license_id", referencedColumnName="id") }) 
	public List<Instance> instances;
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,License> find = new Model.Finder(Long.class, License.class);
    
    public static License findById(Long id) {
    	License license = find.byId(id);
    	return license;
    }
    
    public static License findByUrl(String url) {
    	License license = find.where().eq(Const.URL, url).findUnique();
    	return license;
    }
    
    public static License findByName(String name) {
    	return find.where().eq("name", name).findUnique();
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
    
	public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(License t: License.findAllLicenses()) {
            options.put(t.id.toString(), t.name);
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
		License other = (License) obj;
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
}
