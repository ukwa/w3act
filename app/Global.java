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
        
    	/**
    	 * normalize URL if there is "_" e.g. in taxonomy_term
    	 */
    	public static void normalizeUrls() {
            List<Target> targets = Target.findAll();
            Iterator<Target> itr = targets.iterator();
            while (itr.hasNext()) {
            	Target target = itr.next();
				if (target.field_collection_categories != null && target.field_collection_categories.contains("_")) {
					target.field_collection_categories = target.field_collection_categories.replace("_", "/");
				}
				if (target.field_license != null && target.field_license.contains("_")) {
					target.field_license = target.field_license.replace("_", "/");
				}
	            Ebean.update(target);
			}
    	}
    	
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
	                Logger.info("load curators ...");
	                // aggregate original curators from drupal extracting information from aggregated data
			        List<Object> allCurators = JsonUtils.extractDrupalData(Const.NodeType.USER);
					// store urls in DB
	                Ebean.save(allCurators);
	                Logger.info("curators successfully loaded");
	                Logger.info("load taxonomies ...");
	                // aggregate original taxonomies from drupal extracting information from aggregated data
			        List<Object> allTaxonomies = JsonUtils.extractDrupalData(Const.NodeType.TAXONOMY);
					// store urls in DB
	                Ebean.save(allTaxonomies);
	                Logger.info("taxonomies successfully loaded");
	                Logger.info("load taxonomy vocabularies ...");
	                // aggregate original taxonomy vocabulary from drupal extracting information from aggregated data
			        List<Object> allTaxonomyVocabularies = JsonUtils.extractDrupalData(Const.NodeType.TAXONOMY_VOCABULARY);
					// store urls in DB
	                Ebean.save(allTaxonomyVocabularies);
	                Logger.info("taxonomy vocabularies successfully loaded");
	                normalizeUrls();
                } catch (Exception e) {
                	Logger.info("Store error: " + e);
                }
            }
        }
        
    }
    
}