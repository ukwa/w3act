package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "field_url")
public class FieldUrl extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2076421295020360166L;

	@Id
    public Long id;
	
	@Column(columnDefinition="text")
	public String url;

    public Date createdAt;

    @Version
    public Timestamp updatedAt;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "target_id")
	public Target target;

	@Column(columnDefinition = "text")
	public String domain;
	
	public FieldUrl(String url) {
		super();
		this.url = url;
	}

    @Override
	public void save() {
    	// need to save to get the ID
		this.createdAt = new Date();
    	super.save();
    }

	@Override
	public String toString() {
		return "FieldUrl [url=" + url + "]";
	}
}
