package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
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
	public Target target;
	
	public Long position;
	
	@Column(columnDefinition = "text")
	public String domain;
	
	
	public static Model.Finder<Long, FieldUrl> find = new Finder<Long, FieldUrl>(Long.class, FieldUrl.class);
	
	public FieldUrl(String url) throws ActException {
		super();
		this.url = url.trim();
		this.domain = Scope.getDomainFromUrl(this.url);
	}

	public static FieldUrl findByUrl(String url) {
		return find.where().eq("url", url).findUnique();
	}
	
	public static List<FieldUrl> findByContains(String url) {
		return find.where().endsWith("url", url).findList();
	}
	
	public static FieldUrl hasDuplicate(String url) throws ActException {
		url = Utils.INSTANCE.getPath(url);
		String urlSlash = url.endsWith("/") ? StringUtils.stripEnd(url, "/") : StringUtils.appendIfMissing(url, "/");
		Logger.debug("url: " + url);
		Logger.debug("urlNoSlash: " + urlSlash);
		
		List<FieldUrl> fieldUrls = find.where().add(Expr.or(
				Expr.iendsWith("url", url),
				Expr.iendsWith("url", urlSlash))).findList();
		
		// filter this list
		for (FieldUrl fieldUrl : fieldUrls) {
			Logger.info("Comparing with "+fieldUrl.url);
			String dbUrl = Utils.INSTANCE.getPath(fieldUrl.url);
			String dbUrlSlash = dbUrl.endsWith("/") ? StringUtils.stripEnd(dbUrl, "/") :  StringUtils.appendIfMissing(dbUrl, "/");
			Logger.debug("url: " + url);
			Logger.debug("urlSlash: " + urlSlash);
			Logger.debug("dbUrl: " + dbUrl);
			Logger.debug("dbUrlSlash: " + dbUrlSlash);
			if (url.equalsIgnoreCase(dbUrl) || url.equalsIgnoreCase(dbUrlSlash) ||
					urlSlash.equalsIgnoreCase(dbUrl) || urlSlash.equalsIgnoreCase(dbUrlSlash)) {
				return fieldUrl;
			}
		}
		
		return null; 
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
		return this.url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		FieldUrl other = (FieldUrl) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
