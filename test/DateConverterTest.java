import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;


public class DateConverterTest {

	Long createdAtLong = 1362658340L;
	Long startDateLong = 1365253200L;
	Long nzlTime = 1317951113613L; // 2.32pm NZDT
	String createdAt = "1362658340";
	String startDate = "1365253200";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Date createdAt = getDateFromSeconds(createdAtLong);
		System.out.println(createdAt);
		Date startDate = getDateFromSeconds(startDateLong);
		System.out.println(startDate);
//		Date nzlDate = getDateFromSeconds(nzlTime);
//		System.out.println(nzlDate);
		
	}

	private Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			long altered = seconds*1000L;
			date = new Date(altered);
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(altered);
			
			DateFormat formatter = new SimpleDateFormat();
			formatter.setCalendar(calendar);
			
//			TimeZone timeZone = Calendar.getInstance().getTimeZone();
//			formatter.setTimeZone(timeZone);
			String formatted = formatter.format(calendar.getTime());
			System.out.println("formatted: " + formatted);
		}
		return date;
	}
}
