package uk.bl.export;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import models.FieldUrl;
import models.Target;
import play.Logger;
import uk.bl.Const;
import uk.bl.api.Utils;

public enum ExportMalformedUrls {

	INSTANCE;
	
	public File exportCSV(String filename) {
		List<SqlRow> targets = getTargets();
		
    	Logger.debug("targets: " + targets.size());

        StringWriter sw = new StringWriter();
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date requested");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (targets != null && targets.size() > 0) {
 	    	for (SqlRow target : targets) {
 	    		String title = target.get("title").toString();
 	    		String url = target.get("url").toString();
 	    		String createdAt = target.get("createdAt").toString();
            	Logger.debug("url: " + url);
				try {
					new URI(url).normalize().toURL();
				} catch (MalformedURLException | URISyntaxException e) {
					e.printStackTrace();
		    		sw.append(title);
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append(url);
			 	    sw.append(Const.CSV_SEPARATOR);
		    		sw.append(createdAt);
			 	    sw.append(Const.CSV_SEPARATOR);
		 	 	    sw.append(Const.CSV_LINE_END);
				}
    		}
 	    }
    	File file = Utils.INSTANCE.generateCsvFile(filename, sw.toString());
    	return file;
    }
	
	private List<SqlRow> getTargets() {
		String sql = "select t.title as title, f.url as url, t.created_at as createdAt from Target t, Field_url f where f.target_id=t.id";
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
