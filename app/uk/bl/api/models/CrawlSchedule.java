/**
 * 
 */
package uk.bl.api.models;

import java.util.Date;

/**
 * @author andy
 *
 */
public class CrawlSchedule {
	
	public final Date startDate;
	public final Date endDate;
	public final String frequency;

	public CrawlSchedule(Date crawlStartDate, Date crawlEndDate,
			String crawlFrequency) {
		this.startDate = crawlStartDate;
		this.endDate = crawlEndDate;
		this.frequency = crawlFrequency;
	}
}
