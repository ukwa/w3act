package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonProperty;

import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;

@MappedSuperclass
abstract class ActModel extends Model {

	private static final long serialVersionUID = -4282393560240621524L;

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
	
}
