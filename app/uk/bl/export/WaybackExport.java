package uk.bl.export;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
	        System.out.println("instance: " + wayback);
	        System.out.println("request: " + wayback.getRequest());
	        System.out.println("results: " + wayback.getResults().getResults().size());
	        
		} catch (JAXBException | MalformedURLException e) {
			throw new ActException(e);
		}
		return wayback;
	}

}
