package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
abstract class ActModelMappedSuperClass extends Model {

    /**
	 * Needed as "Properties from superclasses not mapped as @MappedSuperclass are ignored"
	 */
	private static final long serialVersionUID = 8159795832730112892L;
	
	@Id
    public Long id;
	
	@JsonProperty
	@Column(unique=true)
	public String url;

    public Date createdAt;

    @Version
    public Timestamp updatedAt;
   
    @Override
    @Transactional
	public void save() {
    	// need to save to get the ID
    	super.save();
    	if (StringUtils.isEmpty(this.url)) {
    		this.url = Const.ACT_URL + this.id;
    	}
    	if (createdAt == null) {
    		this.createdAt = new Date();
    	}
    	super.save();
    }
    
	public String toCreatedAtString() {
		return Utils.INSTANCE.convertToDateString(createdAt);
	}
	
	public String toUpdatedAtString() {
		return Utils.INSTANCE.convertToDateTime(updatedAt);
	}
	

	@Required(message="Name is required")
    @JsonProperty
    public String name; 
    // additional field to make a difference between collection, subject, license and quality issue. 

    @Column(columnDefinition = "text")
    @JsonProperty
    public String description;

    @JsonProperty(value="field_publish")
    public Boolean publish;
    
    @Column(columnDefinition = "text") 
    @JsonIgnore
    public String parentsAll;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String revision;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

}
