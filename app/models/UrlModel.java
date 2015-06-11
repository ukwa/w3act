package models;

import java.io.IOException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import play.Logger;
import play.data.validation.Constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
abstract class UrlModel extends ActModel {

	private static final long serialVersionUID = 2497175981252269093L;

	@JsonIgnore
	@Transient
	protected final String HTTP            = "http://";

	@JsonIgnore
	@Transient
	protected final String HTTPS           = "https://";

	@JsonIgnore
	@Transient
	protected final String WWW             = "www.";

	@JsonIgnore
	@Transient
	protected final String END_STR         = "/";
	
    @Constraints.Required(message="Title Required")
	public String title;
	
	public String language;

	@Column(columnDefinition = "text")
	public String revision;

	@JsonProperty
	public String edit_url;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "qaissue_id")
	public QaIssue qaIssue;

	@JsonIgnore
	@Column(columnDefinition = "text")
	public String notes;
	
	@JsonIgnore
	public String format;


	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}
	
	public String getDomainFromUrl(String url) throws IOException {
		return new URL(url).getHost().replace(WWW, "");
    }
	
	public String normalizeUrl(String url) {
		return this.normalizeUrl(url, true);
	}

	public String normalizeUrlNoSlash(String url) {
		return this.normalizeUrl(url, false);
	}
	
	private String normalizeUrl(String url, boolean slash) {
		if (url != null && url.length() > 0) {
	        if (!url.contains(WWW) && !url.contains(HTTP) && !url.contains(HTTPS)) {
	        	url = WWW + url;
	        }
	        if (!url.contains(HTTP)) {
		        if (!url.contains(HTTPS)) {
		        	url = HTTP + url;
		        }
	        }
	        if (slash && !url.endsWith(END_STR)) {
	        	url = url + END_STR;
	        }
		}
        Logger.debug("normalized URL: " + url);
		return url;
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
