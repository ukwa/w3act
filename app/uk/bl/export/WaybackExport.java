package uk.bl.export;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import models.FieldUrl;
import models.Instance;
import models.Target;
import play.Logger;
import uk.bl.api.Utils;
import uk.bl.api.models.Results;
import uk.bl.api.models.Wayback;
import uk.bl.exception.ActException;

public enum WaybackExport {
	
	INSTANCE;
	
	public Wayback export(String urlValue) throws ActException {
		JAXBContext context;
		Wayback wayback;
		try {
			context = JAXBContext.newInstance(Wayback.class);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	        URL url = new URL(urlValue);
	        wayback = (Wayback) unmarshaller.unmarshal(url);	        
		} catch (JAXBException | MalformedURLException e) {
			throw new ActException(e);
		}
		return wayback;
	}
	
	public void bulkTargetImport() {

    	String webArchiveUrl = play.Play.application().configuration().getString("application.wayback.url");

    	Logger.debug("webArchiveUrl: " + webArchiveUrl);
    	List<Target> targets = Target.findAll();
    	
    	for (Target target : targets) {
	    	for (FieldUrl fieldUrl : target.fieldUrls) {
	    		String urlValue = webArchiveUrl + fieldUrl.url;
	    		
	        	Logger.debug("urlValue: " + urlValue);
	
				try {
					Wayback wayback = export(urlValue);
					
					Results results = wayback.getResults();
					if (results != null && results.getResults() != null) {
						for (uk.bl.api.models.Result result : results.getResults()) {
							// check instance first
							String captureDatetitle = result.getCapturedate().toString();
							
							Instance instance = Instance.findbyTitleAndTargetId(captureDatetitle, target.id);
							if (instance == null) {
								instance = new Instance();
								instance.title = captureDatetitle;
								instance.createdAt = Utils.INSTANCE.getDateFromLongValue(result.getCapturedate());
								Logger.debug("instance.createdAt: " + instance.createdAt);
								instance.format = result.getMimetype();
								instance.revision = "initial revision";
								instance.fieldDate = Utils.INSTANCE.getDateFromLongValue(result.getCapturedate());
								instance.target = target;
//								instance.save();
							}
						}
					}
				} catch (ParseException | ActException e) {
					e.printStackTrace();
				}
	    	}
    	}
	}

	public static void main(String[] args) {
		Logger.debug("start");
		new play.core.StaticApplication(new java.io.File("."));
		WaybackExport.INSTANCE.bulkTargetImport();
		Logger.debug("finished");
	}
}
