import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import uk.bl.api.Base64;

public class ExportAuthTest {

	String drupalUser = "";
	String drupalPassword = "";
	String userPassword = drupalUser+":"+drupalPassword;
	String authEncoded = "";
    
	InputStream in = null;
    OutputStream out = null;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("userPassword: " + userPassword);
        authEncoded = Base64.encodeBytes(userPassword.getBytes());
	}

	@Test
	public void testAuth() {
		try {
			String outputPath = "instanceres.txt";
			String urlString = "http://www.webarchive.org.uk/act/node.json?type=instance";
	        URL url = new URL(urlString);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + authEncoded);
	        
	        in = (InputStream) connection.getInputStream();
	        
	        File file = new File(outputPath);
	        out = new BufferedOutputStream(new FileOutputStream(file));
	        for (int b; (b = in.read()) != -1;) {
	            out.write(b);
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
		        if (in != null) in.close();
				if (out != null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	@Test
	public void testInstance() {
		try {
			String outputPath = "instancesingle.txt";
			String urlString = "http://www.webarchive.org.uk/act/node/12873.json";
	        URL url = new URL(urlString);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + authEncoded);
	        
	        in = (InputStream) connection.getInputStream();
	        
	        File file = new File(outputPath);
	        out = new BufferedOutputStream(new FileOutputStream(file));
	        for (int b; (b = in.read()) != -1;) {
	            out.write(b);
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
		        if (in != null) in.close();
				if (out != null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

}
