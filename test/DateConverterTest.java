import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;


public class DateConverterTest {

	Long startDateLong = 1365253200L;
	Long createdAtLong = 1362658340L;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Date createdAt = getDateFromSeconds(createdAtLong);
		System.out.println(createdAt);
		Date startDate = getDateFromSeconds(startDateLong);
		System.out.println(startDate);
		
	}

	private Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			date = new Date(seconds*1000L);
			DateFormat formatter = new SimpleDateFormat();
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String formatted = formatter.format(date);
			System.out.println("converted date: " + formatted);
			System.out.println("converted date: " + date);
		}
		return date;
	}
}
