package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;
import uk.bl.Const;

@MappedSuperclass
abstract class ActModel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4282393560240621524L;

	@Id
    public Long id;
    
	public String url;

    @JsonIgnore
    public Date createdAt;

    @Version
    public Timestamp updatedAt;

    @Override
	public void save() {
    	// need to save to get the ID
    	super.save();
    	this.url = Const.ACT_URL + this.id;
    	this.createdAt = new Date();
    	super.save();
    }

}
