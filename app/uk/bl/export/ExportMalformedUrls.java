package uk.bl.export;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import play.Logger;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;

public enum ExportMalformedUrls {

	INSTANCE;
	
	public File exportCSV(String filename) {
		List<SqlRow> targets = getTargets();
		
    	Logger.debug("targets: " + targets.size());

        StringWriter sw = new StringWriter();
	    sw.append("Target ID");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target ACT ID");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date requested");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (targets != null && targets.size() > 0) {
 	    	for (SqlRow target : targets) {
 	    		String id = target.get("id").toString();
 	    		String actUrl = target.get("actUrl").toString();
 	    		String title = target.get("title").toString();
 	    		String url = target.get("url").toString();
 	    		String createdAt = target.get("createdAt").toString();
				try {
					new URI(url).normalize().toURL();
//        			String[] schemes = {"http","https"};
//        			UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
        			boolean isValidUrl = Utils.INSTANCE.validUrl(url);
        			if (!isValidUrl) {
            			Logger.debug("Not valid? " + url);
        				throw new ActException("Invalid URL");
        			}
				} catch (MalformedURLException | URISyntaxException | ActException e) {
					e.printStackTrace();
		    		sw.append("\"");
		    		sw.append(id);
		    		sw.append("\"");
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append("\"");
		    		sw.append(actUrl);
		    		sw.append("\"");
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append("\"");
		    		sw.append(title);
		    		sw.append("\"");
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append("\"");
		    		sw.append(url);
		    		sw.append("\"");
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append("\"");
		    		sw.append(createdAt);
		    		sw.append("\"");
			 	    sw.append(Const.CSV_SEPARATOR);
		 	 	    sw.append(Const.CSV_LINE_END);
				}
    		}
 	    }
    	File file = Utils.INSTANCE.generateCsvFile(filename, sw.toString());
    	return file;
    }
	
	private List<SqlRow> getTargets() {
		String sql = "select t.id, t.url as actUrl, t.title as title, f.url as url, t.created_at as createdAt from Target t, Field_url f where f.target_id=t.id";
		List<SqlRow> results = Ebean.createSqlQuery(sql).findList();
		return results;
	}
	public static void main(String[] args) {
		Logger.debug("start");
		new play.core.StaticApplication(new java.io.File("."));
		ExportMalformedUrls.INSTANCE.exportCSV("badUrls.csv");
		Logger.debug("finished");
	}
}
