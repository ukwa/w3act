package models;

import java.io.IOException;
import java.net.URL;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import play.Logger;
import play.db.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
abstract class UrlModel extends Model {

	private static final long serialVersionUID = 2497175981252269093L;


	
	@JsonIgnore
	@Transient
	protected final static String HTTP            = "http://";

	@JsonIgnore
	@Transient
	protected final static String HTTPS           = "https://";

	@JsonIgnore
	@Transient
	protected final static String WWW             = "www.";

	@JsonIgnore
	@Transient
	protected final static String END_STR         = "/";
	
	public static String getDomainFromUrl(String url) throws IOException {
		return new URL(url).getHost().replace(WWW, "");
    }
	
	public static String normalizeUrl(String url) {
		return normalizeUrl(url, true);
	}

	public static String normalizeUrlNoSlash(String url) {
		return normalizeUrl(url, false);
	}
	
	private static String normalizeUrl(String url, boolean slash) {
		if (url != null && url.length() > 0) {
	        //if (!url.contains(WWW) && !url.contains(HTTP) && !url.contains(HTTPS)) {
	        //	url = WWW + url;
	        //}
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

	
}
