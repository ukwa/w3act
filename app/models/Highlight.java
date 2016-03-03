package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;


/**
 * Instance instance entity managed by Ebean
 */
@Entity 
@Table(name = "highlight")
public class Highlight extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4285620218930401425L;

	@Id
    public Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "taxonomy_id", referencedColumnName = "id")
	public Taxonomy taxonomy;
	
	@Column(name="url")
	public String url;
	
	@Column(name="start_date")
	public Timestamp startDate;
	
	@Column(name="end_date")
	public Timestamp endDate;
	
}

