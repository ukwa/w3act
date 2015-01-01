package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import play.Logger;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
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
	
	
	public static Model.Finder<Long, FieldUrl> find = new Finder<Long, FieldUrl>(Long.class, FieldUrl.class);

	public FieldUrl(String url) {
		super();
		this.url = url;
	}

	public static FieldUrl findByUrl(String url) {
		return find.where().eq("url", url).findUnique();
	}
	public static List<FieldUrl> findHigherLevelUrls(String domain, String url) {
		Logger.debug("Parameters: " + domain + " - " + url.length());
		String query = "find fieldUrl fetch target fetch target.licenses where url like :domain and LENGTH(url) < :length";
        List<FieldUrl> fieldUrls = Ebean.createQuery(FieldUrl.class, query)
        		.setParameter("domain", "%" + domain + "%")
        		.setParameter("length", url.length()).where().or(Expr.isNotNull("target.licenses"), Expr.isNotNull("target.qaIssue")).findList();
		return fieldUrls;
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
