import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;

import org.junit.Before;
import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;


public class UkHostingTest {

	List<FieldUrl> fieldUrls;

	@Before
	public void setUp() throws Exception {
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("http://www.camdentownshed.org/")); // 109.123.65.110
		fieldUrls.add(new FieldUrl("http://www.camdentownshed.org/")); // 109.123.65.110
	}

	@Test
	public void test() {
		assertTrue(isUkHosting(fieldUrls));
		fieldUrls.add(new FieldUrl("http://heartoftheschool.edublogs.org/")); // 104.16.0.23
		fieldUrls.add(new FieldUrl("http://www.newleftproject.org/")); // 205.186.179.65
		assertFalse(isUkHosting(fieldUrls));
		assertTrue(queryDb("109.123.65.110"));
		assertFalse(queryDb("104.16.0.23")); // false
		assertFalse(queryDb("205.186.179.65")); // US
	}
	
	public boolean isUkHosting(List<FieldUrl> fieldUrls) {
		for (FieldUrl fieldUrl : fieldUrls) {
			if (!this.checkGeoIp(fieldUrl.url)) return false;
		}
		return true;
	}
	
	public boolean checkGeoIp(String url) {
		boolean res = false;
		String ip = getIpFromUrl(url);
		System.out.println("ip: " + ip);
		res = queryDb(ip);
		return res;
	}
	
	public String getIpFromUrl(String url) {
		String ip = "";
		InetAddress address;
		try {
			address = InetAddress.getByName(new URL(url).getHost());
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("ip calculation unknown host error for url=" + url + ". " + e.getMessage());
		} catch (MalformedURLException e) {
			System.out.println("ip calculation error for url=" + url + ". " + e.getMessage());
		}
        return ip;
	}
	
	public boolean queryDb(String ip) {
		boolean res = false;
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("GeoLite2-City.mmdb");
	
		try {
			// This creates the DatabaseReader object, which should be reused across
			// lookups.
			DatabaseReader reader = new DatabaseReader.Builder(database).build();
		
			// Find city by given IP
			CityResponse response = reader.city(InetAddress.getByName(ip));
			System.out.println(response.getCountry().getIsoCode()); 
			System.out.println(response.getCountry().getName()); 
			// Check country code in city response
			if (response.getCountry().getIsoCode().equals("GB")) {
				res = true;
			}
		} catch (Exception e) {
			System.err.println("GeoIP error. " + e);
		}
		System.out.println("Geo IP query result: " + res);
		return res;
	}

}
