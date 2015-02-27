import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;


public class DateConverterTest {

	Long createdAtLong = 1362658340L;
	Long startDateLong = 1365253200L;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		System.out.println("" + System.getProperty("user.timezone"));

		Date createdAt = getDateFromSeconds(createdAtLong);
		System.out.println(createdAtLong + " - " + createdAt);

		Date startDate = getDateFromSeconds(startDateLong);
		System.out.println(startDateLong + " - " + startDate);

		System.setProperty("user.timezone", "UTC");
		TimeZone.setDefault(null);
		System.out.println("" + System.getProperty("user.timezone"));

		createdAt = getDateFromSeconds(createdAtLong);
		System.out.println(createdAtLong + " - " + createdAt);

		startDate = getDateFromSeconds(startDateLong);
		System.out.println(startDateLong + " - " + startDate);
		
		
//		DateTime startJoda = new DateTime(startDateLong*1000L, DateTimeZone.UTC);
////        DateTime startJoda = new DateTime(startDateLong*1000L, DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")));
//		System.out.println(startJoda.getMillis() + " - " + startJoda.toDate());
//		
//		MutableDateTime createdJoda = new MutableDateTime(createdAtLong*1000L, DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")));
//        createdJoda.addMonths(1);
//		System.out.println(createdJoda.getMillis() + " - " + createdJoda.toDate());
		
//		Date d = convertDateTimeZone(startDate, TimeZone.getTimeZone("UTC"));
//		
//		System.out.println("converted: " + d);
		
//		System.out.println(Calendar.getInstance().getTimeZone().getDisplayName());
		
		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//		calendar.setTimeInMillis(startDateLong);
//
//	    int hour = calendar.get(Calendar.HOUR);
//	    System.out.println(hour);
//	    System.out.println(calendar.getTimeZone());


		
		
		
	}

	private Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			long altered = seconds*1000L;
			date = new Date(altered);
		}
		return date;
	}
}
