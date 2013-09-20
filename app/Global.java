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
        
        public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

                // Insert users first
                Ebean.save(all.get("users"));
                System.out.println("load urls");
				// aggregate url data from drupal and store JSON content in a file
		        List<Object> allUrls = JsonUtils.getDrupalData(Const.NodeType.URL);
				// store urls in DB
                Ebean.save(allUrls);
		        // aggregate collections data from drupal and store JSON content in a file
                System.out.println("load collections");
		        List<Object> allCollections = JsonUtils.getDrupalData(Const.NodeType.COLLECTION);
				// store collections in DB
                Ebean.save(allCollections);
                System.out.println("load projects");
            }
        }
        
    }
    
}