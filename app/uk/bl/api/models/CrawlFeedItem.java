/**
 * 
 */
package uk.bl.api.models;

import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

/**
 * @author andy
 *
 */
public class CrawlFeedItem {
	
	public final Long id;
	public final String title;
	public final List<String> seeds;
	public final String depth;
	public final String scope;
	public final Boolean ignoreRobotsTxt;
	public final List<CrawlSchedule> schedules;
	public final Boolean watched;
	public final String documentUrlScheme;
	public final String loginPageUrl;
	public final String logoutUrl;
	public final Integer secretId;
	
	public CrawlFeedItem( Target t ) {
		this.id = t.id;
		this.title = t.title;
		List<String> seeds = new ArrayList<String>();
		for( FieldUrl furl : t.fieldUrls) {
			seeds.add(furl.url);
		}
		this.seeds = seeds;
		this.depth = t.depth;
		this.scope = t.scope;
		this.ignoreRobotsTxt = t.ignoreRobotsTxt;
		List<CrawlSchedule> scheds = new ArrayList<CrawlSchedule>();
		scheds.add(new CrawlSchedule(t.crawlStartDate, t.crawlEndDate, t.crawlFrequency));
		this.schedules = scheds;
		this.watched = t.isWatched();
		if( t.watchedTarget != null ) {
			this.documentUrlScheme = t.watchedTarget.documentUrlScheme;
			this.loginPageUrl = t.watchedTarget.loginPageUrl;
			this.logoutUrl = t.watchedTarget.logoutUrl;
			this.secretId = t.watchedTarget.secretId;
		} else {
			this.documentUrlScheme = null;
			this.loginPageUrl = null;
			this.logoutUrl = null;
			this.secretId = null;
		}
	}
}
