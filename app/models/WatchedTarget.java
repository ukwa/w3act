package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Page;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

@Entity
public class WatchedTarget extends Model {
	@Id
	public Long id;
	@OneToOne @JsonIgnore
	@JoinColumn(name="id_target")
	public Target target;
	@ManyToOne @JsonIgnore
	@JoinColumn(name="id_creator")
	public User user;
	@OneToMany(mappedBy="watchedTarget") @JsonIgnore
	@JoinColumn(name="id_watched_target")
	public List<Document> documents;
	public String documentUrlScheme;
	public String getUrl() { return ""+id; }
	public String getName() { return target.title; }
	@Transient
	public int documentCount;
	
	public static final Model.Finder<Long, WatchedTarget> find = new Model.Finder<>(Long.class, WatchedTarget.class);
	
	public WatchedTarget(User user, String title, String url, String field_url, String documentUrlScheme) {
		this.user = user;
		this.documentUrlScheme = documentUrlScheme;
		target = new Target(title, url);
		target.field_url = field_url;
		target.active = true;
		target.author = user.url;
	}
	
    public WatchedTarget(Target target, String documentUrlScheme) {
		this.target = target;
		user = User.findByUrl(target.author);
		this.documentUrlScheme = documentUrlScheme;
	}
	public static Page<WatchedTarget> page(User user, int page, int pageSize, String sortBy, String order, String filter) {
    	
		String sql = "select wt.id, wt.id_creator, t.id, t.url, t.title, t.field_url, count(d.id) as documentCount"
				+ " from watched_target wt"
				+ " left outer join target t on t.id = wt.id_target"
				+ " left outer join document d on id_watched_target = wt.id"
				+ " group by wt.id, wt.id_creator, t.id, t.url, t.title, t.field_url";
		
		RawSql rawSql = RawSqlBuilder.parse(sql)
				.columnMapping("wt.id_creator",  "user.uid")
				.columnMapping("t.id",  "target.nid")
				.columnMapping("t.url",  "target.url")
				.columnMapping("t.title",  "target.title")
				.columnMapping("t.field_url",  "target.field_url")
				.create();
		
        return find.setRawSql(rawSql).where()
        		.eq("id_creator", user.uid)
        		.icontains("target.field_url", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
}
