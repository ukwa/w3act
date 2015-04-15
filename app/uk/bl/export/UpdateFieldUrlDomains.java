package uk.bl.export;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import models.FieldUrl;
import play.Logger;
import uk.bl.exception.ActException;
import uk.bl.scope.Scope;

public enum UpdateFieldUrlDomains {

	INSTANCE;
		
	public void update() {
		
		List<FieldUrl> fieldUrls = FieldUrl.find.where().isNull("domain").findList();
		
 	    if (fieldUrls != null && fieldUrls.size() > 0) {
 	    	for (FieldUrl fieldUrl : fieldUrls) {
 	    		Logger.debug(fieldUrl.target.title + " : " + fieldUrl.domain);
    			String domain = "";
				try {
					domain = Scope.INSTANCE.getDomainFromUrl(fieldUrl.url);
	    			Logger.debug(fieldUrl.url + " - " + domain);
				} catch (ActException e) {
					e.printStackTrace();
				}
				if (StringUtils.isNotBlank(domain)) {
	    			fieldUrl.domain = domain;
	    			fieldUrl.update();
				} else {
	    			Logger.debug("failed: " + fieldUrl.url + " - " + domain);
				}
 	    	}
 	    }
    }
	
	public static void main(String[] args) {
		Logger.debug("start");
		new play.core.StaticApplication(new java.io.File("."));
		UpdateFieldUrlDomains.INSTANCE.update();
		Logger.debug("finished");
	}
}

