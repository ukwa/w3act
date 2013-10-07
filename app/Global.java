import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;
import uk.bl.Const;
import uk.bl.api.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
        InitialData.insert(app);
    }
    
    static class InitialData {
        
        @SuppressWarnings("unchecked")
		public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                try {
	                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
	
	                // Insert users first
	                Ebean.save(all.get("users"));
	                Logger.info("load urls");
					// aggregate url data from drupal and store JSON content in a file
			        List<Object> allUrls = JsonUtils.getDrupalData(Const.NodeType.URL);
					// store urls in DB
	                Ebean.save(allUrls);
	                Logger.info("targets successfully loaded");
	//                List<Target> targetList = (List<Target>) Target.find.all();
	//                Iterator<Target> targetItr = targetList.iterator();
	//                while (targetItr.hasNext()) {
	//                	Target target = targetItr.next();
	//                    Logger.info("Target test object: " + target.toString());
	//                }
			        // aggregate collections data from drupal and store JSON content in a file
	                Logger.info("load collections ...");
			        List<Object> allCollections = JsonUtils.getDrupalData(Const.NodeType.COLLECTION);
					// store collections in DB
	                Ebean.save(allCollections);
	                Logger.info("collections successfully loaded");
	                Logger.info("load organisations ...");
					// aggregate organisations data from drupal and store JSON content in a file
			        List<Object> allOrganisations = JsonUtils.getDrupalData(Const.NodeType.ORGANISATION);
					// store organisations in DB
	                Ebean.save(allOrganisations);
	                Logger.info("organisations successfully loaded");
                } catch (Exception e) {
                	Logger.info("Store error: " + e);
                }
            }
        }
        
    }
    
}