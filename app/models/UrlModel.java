package models;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import uk.bl.api.models.FieldModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
abstract class UrlModel extends ActModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2497175981252269093L;

	// bi-directional many-to-one association to Organisation
	@ManyToOne
	@JoinColumn(name = "organisation_id")
	@JsonIgnore
	public Organisation organisation;

	@JsonProperty
	public String title;
	
	@JsonProperty
	public String edit_url;

	@JsonProperty
	public String language;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String revision;

	@Transient
	@JsonProperty
	private Object body;
	
	@Transient
	@JsonProperty
	private String nid;

	@Transient
	@JsonProperty
	public String vid;
	
	@Transient
	@JsonProperty
	private Boolean is_new;

	@Transient
	@JsonProperty
	protected String type;
	
	@Transient
	@JsonProperty
	public Long status;

	@Transient
	@JsonProperty
	protected Long promote;

	@Transient
	@JsonProperty
	protected Long sticky;

	@Transient
	@JsonProperty
	protected Long created;

	@Transient
	@JsonProperty
	protected Long changed;
	
	@Transient
	@JsonProperty
	protected String log;

	@Transient
	@JsonProperty
	protected Long comment;

	@Transient
	@JsonProperty
	protected Object comments;

	@Transient
	@JsonProperty
	protected Long comment_count;

	@Transient
	@JsonProperty
	protected Long comment_count_new;

	@Transient
	@JsonProperty
	protected Long feed_nid;

	@Transient
	@JsonProperty
	protected FieldModel author;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Boolean getIs_new() {
		return is_new;
	}

	public void setIs_new(Boolean is_new) {
		this.is_new = is_new;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getPromote() {
		return promote;
	}

	public void setPromote(Long promote) {
		this.promote = promote;
	}

	public Long getSticky() {
		return sticky;
	}

	public void setSticky(Long sticky) {
		this.sticky = sticky;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getChanged() {
		return changed;
	}

	public void setChanged(Long changed) {
		this.changed = changed;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Long getComment() {
		return comment;
	}

	public void setComment(Long comment) {
		this.comment = comment;
	}

	public Object getComments() {
		return comments;
	}

	public void setComments(Object comments) {
		this.comments = comments;
	}

	public Long getComment_count() {
		return comment_count;
	}

	public void setComment_count(Long comment_count) {
		this.comment_count = comment_count;
	}

	public Long getComment_count_new() {
		return comment_count_new;
	}

	public void setComment_count_new(Long comment_count_new) {
		this.comment_count_new = comment_count_new;
	}

	public Long getFeed_nid() {
		return feed_nid;
	}

	public void setFeed_nid(Long feed_nid) {
		this.feed_nid = feed_nid;
	}

	public FieldModel getAuthor() {
		return author;
	}

	public void setAuthor(FieldModel author) {
		this.author = author;
	}

//	same	"nid":"12954",
//	same	"vid":"20109",
//	same	"is_new":false,
//	same	"type":"instance",
//	same 	"title":"20130908110118",
//	same 	"language":"und",
//	same	"url":"http:\/\/webarchive.org.uk\/act\/node\/12954",
//	same	"edit_url":"http:\/\/webarchive.org.uk\/act\/node\/12954\/edit",
//	same 	"status":"1",
//	same 	"promote":"0",
//	same 	"sticky":"0",
//	same	"created":"1394535642",
//	same	"changed":"1394551688",
//	same	"log":"",
//	same	"revision":null,	
//	same	"comment":"1",
//	same	"comments":[],
//	same	"comment_count":"0",
//	same	"comment_count_new":"0",
//	same	"feed_nid":null}


	
}
