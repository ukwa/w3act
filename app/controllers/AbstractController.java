package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Collection;
import models.Subject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import play.Logger;
import play.cache.Cached;
import play.libs.Json;
import play.mvc.Controller;

public class AbstractController extends Controller {

	private static final String cacheName = "play";
	private static final CacheManager cacheManager = CacheManager.getInstance();

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static Cache getCache() {
		Cache cache = getCacheManager().getCache(cacheName);
		return cache;
	}

	protected static String getQueryParam(String name) {
                String[] value = request().queryString().get(name);
                if (value == null)
                        return null;
                
                if (value.length == 0)
                        return null;
                
                return value[0];
        }
        
        protected static int getQueryParamAsInt(String name, int defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Integer.parseInt(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static double getQueryParamAsDouble(String name, double defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Double.parseDouble(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static String getFormParam(String name) {
//    		Logger.debug("getFormParam: " + request().body().asFormUrlEncoded());
            Map<String, String[]> formParams = request().body().asFormUrlEncoded();
            if (formParams == null)
                    return null;
//            Logger.debug("form params: " + formParams);
            String[] values = formParams.get(name);
//            Logger.debug("values: " + values);
            if (values == null)
                    return null;
            
            if (values.length < 1)
                    return null;
            
            return values[0];
    }
    
        protected static String[] getFormParams(String name) {
//    		Logger.debug("getFormParam: " + request().body().asFormUrlEncoded());
            Map<String, String[]> formParams = request().body().asFormUrlEncoded();
            if (formParams == null)
                    return null;
//            Logger.debug("form params: " + formParams);
            String[] values = formParams.get(name);
//            Logger.debug("values: " + values);
            if (values == null)
                    return null;
            
            if (values.length < 1)
                    return null;
            
            return values;
    }

    protected static JsonNode getCollectionsDataByIds(List<Long> myCollectionIds) {
        List<ObjectNode> result = getCollectionTreeElementsByIds(getFirstLevelCollectionsData(), null, true, myCollectionIds);
        JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }

	@Cached(key = "CollectionsData")
	protected static List<Collection> getFirstLevelCollectionsData() {
		return Collection.getFirstLevelCollections();
	}

	protected static JsonNode getSingleCollectionDataById(Long myCollectionId, List<Long> selectedCollectionIds) {
		List<Collection> collections = Collection.findChildrenByParentId(myCollectionId);
		List<ObjectNode> result = getCollectionTreeElementsByIds(collections, null, true, selectedCollectionIds);
		JsonNode jsonData = Json.toJson(result);
		return jsonData;
	}

    protected static List<ObjectNode> getCollectionTreeElementsByIds(List<Collection> collections, String filter, boolean parent, List<Long> myCollectionIds) {
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

        for (Collection collection : collections) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", collection.name + " (" + collection.targets.size() + ")");
			child.put("url", String.valueOf(routes.CollectionController.view(collection.id)));

			//if selected in filter
			if (myCollectionIds != null && myCollectionIds.contains(collection.id)) {
				child.put("select", true);
			}

			child.put("key", "\"" + collection.id + "\"");

	    	List<Collection> children = Collection.findChildrenByParentId(collection.id);

	    	if (children.size() > 0) {
	    		child.put("children", true ); //Json.toJson(getCollectionTreeElementsByIds(children, filter, false, myCollectionIds)));
	    	}
			result.add(child);
    	}
    	return result;
    }

    protected static JsonNode getCollectionsData() {
    	return getCollectionsDataByFilter(null);
    }

    protected static JsonNode getCollectionsData(List<Collection> myCollections) {
    	List<Collection> collections = Collection.getFirstLevelCollections();
    	List<ObjectNode> result = getCollectionTreeElements(collections, null, true, myCollections);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }

    /**
     * This method computes a tree of collections in JSON format. 
     * @param collectionUrl This is an identifier for current selected object
     * @return tree structure
     */
    protected static JsonNode getCollectionsDataByFilter(String filter) {
    	List<Collection> collections = Collection.getFirstLevelCollections();
    	List<ObjectNode> result = getCollectionTreeElements(collections, filter, true);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }
    
    protected static List<ObjectNode> getCollectionTreeElements(List<Collection> collections, String filter, boolean parent) {
    	return getCollectionTreeElements(collections, filter, parent, null);
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collections The list of all collections
     * @param filter This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    protected static List<ObjectNode> getCollectionTreeElements(List<Collection> collections, String filter, boolean parent, List<Collection> myCollections) {
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

    	for (Collection collection : collections) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", collection.name + " (" + collection.targets.size() + ")");
			child.put("url", String.valueOf(routes.CollectionController.view(collection.id)));
			if (myCollections != null && myCollections.contains(collection)) {
				child.put("select", true);
			}

			child.put("key", collection.id );
	    	List<Collection> children = Collection.findChildrenByParentId(collection.id);
//	    	Logger.debug("collection: " + collection.name + " - " + collection.children.size());
//    	    	Logger.debug("children: " + children.size());
	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getCollectionTreeElements(children, filter, false, myCollections)));
	    	}
			result.add(child);
    	}
//        	Logger.debug("getTreeElements() res: " + result);
    	return result;
    }

    /**
     * This method calculates children of collections.
     * @param collections The list of all collections
     * @param filter This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    /*
    protected static List<ObjectNode> getCollectionChildTreeElements(int parentCollectionId, String filter) {
        List<ObjectNode> result = new ArrayList<ObjectNode>();
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

        for (Collection collection : collections) {
            ObjectNode child = nodeFactory.objectNode();
            child.put("title", collection.name + " (" + collection.targets.size() + ")");
            child.put("url", String.valueOf(routes.CollectionController.view(collection.id)));
            if (myCollections != null && myCollections.contains(collection)) {
                child.put("select", true);
            }

            child.put("key", collection.id );
            List<Collection> children = Collection.findChildrenByParentId(collection.id);
//	    	Logger.debug("collection: " + collection.name + " - " + collection.children.size());
//    	    	Logger.debug("children: " + children.size());
            if (children.size() > 0) {
                child.put("children", Json.toJson(getCollectionTreeElements(children, filter, false, myCollections)));
            }
            result.add(child);
        }
//        	Logger.debug("getTreeElements() res: " + result);
        return result;
    }
    */


//    [{"title":"100 Best Sites (95)","url":"/actdev/collections/act-170","key":"\"act-170\""},
//     {"title":"19th Century English Literature (0)","url":"/actdev/collections/act-153","key":"\"act-153\""},
//     {"title":"Commonwealth Games Glasgow 2014 (625)","url":"/actdev/collections/act-252","key":"\"act-252\"","children":[
//        {"title":"Competitors (350)","url":"/actdev/collections/act-266","key":"\"act-266\""},{"title":"Cultural Programme (44)","url":"/actdev/collections/act-295","key":"\"act-295\""},{"title":"Organisational bodies/venues (57)","url":"/actdev/collections/act-267","key":"\"act-267\""},{"title":"Press & Media Comment (41)","url":"/actdev/collections/act-269","key":"\"act-269\""},{"title":"Sponsors (15)","url":"/actdev/collections/act-270","key":"\"act-270\""},{"title":"Sports (405)","url":"/actdev/collections/act-268","key":"\"act-268\"","children":[{"title":"Aquatics (74)","url":"/actdev/collections/act-287","key":"\"act-287\""},{"title":"Athletics (107)","url":"/actdev/collections/act-286","key":"\"act-286\""},{"title":"Badminton (18)","url":"/actdev/collections/act-285","key":"\"act-285\""},{"title":"Boxing (26)","url":"/actdev/collections/act-284","key":"\"act-284\""},{"title":"Cycling (34)","url":"/actdev/collections/act-283","key":"\"act-283\""},{"title":"Gymnastics (29)","url":"/actdev/collections/act-282","key":"\"act-282\""},{"title":"Hockey (17)","url":"/actdev/collections/act-281","key":"\"act-281\""},{"title":"Judo (9)","url":"/actdev/collections/act-280","key":"\"act-280\""},{"title":"Lawn bowls (11)","url":"/actdev/collections/act-279","key":"\"act-279\""},{"title":"Netball (6)","url":"/actdev/collections/act-278","key":"\"act-278\""},{"title":"Rugby Sevens (16)","url":"/actdev/collections/act-277","key":"\"act-277\""},{"title":"Shooting (14)","url":"/actdev/collections/act-276","key":"\"act-276\""},{"title":"Squash (16)","url":"/actdev/collections/act-275","key":"\"act-275\""},{"title":"Table tennis (6)","url":"/actdev/collections/act-274","key":"\"act-274\""},{"title":"Triathlon (7)","url":"/actdev/collections/act-273","key":"\"act-273\""},{"title":"Weightlifting (7)","url":"/actdev/collections/act-272","key":"\"act-272\""},{"title":"Wrestling (7)","url":"/actdev/collections/act-271","key":"\"act-271\""}]}]},{"title":"Conservative Party Website deletions - Press articles November 2013 (10)","url":"/actdev/collections/act-175","key":"\"act-175\""},{"title":"Ebola (192)","url":"/actdev/collections/act-296","key":"\"act-296\""},{"title":"European Parliament Elections 2014 (1743)","url":"/actdev/collections/act-250","key":"\"act-250\"","children":[{"title":"Academia & think tanks (15)","url":"/actdev/collections/act-257","key":"\"act-257\""},{"title":"Blogs (215)","url":"/actdev/collections/act-263","key":"\"act-263\""},{"title":"Candidates (520)","url":"/actdev/collections/act-262","key":"\"act-262\""},{"title":"EU Institutions (1)","url":"/actdev/collections/act-260","key":"\"act-260\""},{"title":"Interest groups (63)","url":"/actdev/collections/act-254","key":"\"act-254\""},{"title":"Opinion Polls (7)","url":"/actdev/collections/act-261","key":"\"act-261\""},{"title":"Political Parties: National (99)","url":"/actdev/collections/act-256","key":"\"act-256\""},{"title":"Political Parties: Regional & Local (65)","url":"/actdev/collections/act-258","key":"\"act-258\""},{"title":"Press & Media Comment (744)","url":"/actdev/collections/act-255","key":"\"act-255\""},{"title":"Regulation and Guidance (13)","url":"/actdev/collections/act-253","key":"\"act-253\""},{"title":"Social Media (1)","url":"/actdev/collections/act-259","key":"\"act-259\""}]},{"title":"Evolving role of libraries in the UK (0)","url":"/actdev/collections/act-154","key":"\"act-154\""},{"title":"First World War Centenary, 2014-18 (208)","url":"/actdev/collections/act-174","key":"\"act-174\"","children":[{"title":"Heritage Lottery Fund (66)","url":"/actdev/collections/act-265","key":"\"act-265\""}]},

 //    	 {"title":"Health and Social Care Act 2012 - NHS Reforms (752)","url":"/actdev/collections/act-24","key":"\"act-24\"","children":[
    
//    	     {"title":"NHS (720)","url":"/actdev/collections/act-25","key":"\"act-25\"","children":[
//    	         {"title":"Acute Trusts (161)","url":"/actdev/collections/act-27","key":"\"act-27\""},{"title":"Ambulance Trusts (12)","url":"/actdev/collections/act-136","key":"\"act-136\""},{"title":"Campaigning and Advocacy Groups (18)","url":"/actdev/collections/act-148","key":"\"act-148\""},{"title":"Cancer Networks (28)","url":"/actdev/collections/act-137","key":"\"act-137\""},{"title":"Care Trust (29)","url":"/actdev/collections/act-139","key":"\"act-139\""},{"title":"Clinical Commissioning Groups (191)","url":"/actdev/collections/act-28","key":"\"act-28\""},{"title":"Gateways (11)","url":"/actdev/collections/act-141","key":"\"act-141\""},{"title":"Health and Wellbeing Boards (108)","url":"/actdev/collections/act-29","key":"\"act-29\""},{"title":"Healthwatch (127)","url":"/actdev/collections/act-144","key":"\"act-144\""},{"title":"Legislation (17)","url":"/actdev/collections/act-143","key":"\"act-143\""},{"title":"Local Authorities (142)","url":"/actdev/collections/act-166","key":"\"act-166\""},{"title":"Local Involvement Networks (LINks) (159)","url":"/actdev/collections/act-30","key":"\"act-30\""},{"title":"Mental Health Trusts (50)","url":"/actdev/collections/act-138","key":"\"act-138\""},{"title":"NHS programmes (3)","url":"/actdev/collections/act-142","key":"\"act-142\""},{"title":"Press Comment (248)","url":"/actdev/collections/act-150","key":"\"act-150\""},{"title":"Primary Care Trusts (15)","url":"/actdev/collections/act-26","key":"\"act-26\""},{"title":"Private and voluntary sector providers (8)","url":"/actdev/collections/act-169","key":"\"act-169\""},{"title":"Professional Bodies Trade Union (49)","url":"/actdev/collections/act-147","key":"\"act-147\""},{"title":"Public Health Agencies (1)","url":"/actdev/collections/act-134","key":"\"act-134\""},{"title":"Public Health England (138)","url":"/actdev/collections/act-149","key":"\"act-149\""},{"title":"Regulators & Central Government (1)","url":"/actdev/collections/act-135","key":"\"act-135\""},{"title":"Social Media (Facebook, Twitter etc) (42)","url":"/actdev/collections/act-167","key":"\"act-167\""},{"title":"Special Health Authorities (13)","url":"/actdev/collections/act-140","key":"\"act-140\""},{"title":"Specialised Commissioning Group (12)","url":"/actdev/collections/act-145","key":"\"act-145\""},{"title":"Strategic Health Authorities (20)","url":"/actdev/collections/act-82","key":"\"act-82\"","children":[{"title":"London SHA Cluster (1)","url":"/actdev/collections/act-83","key":"\"act-83\"","children":[{"title":"London SHA (1)","url":"/actdev/collections/act-87","key":"\"act-87\"","children":[{"title":"London (1)","url":"/actdev/collections/act-31","key":"\"act-31\""},{"title":"North Central London (6)","url":"/actdev/collections/act-33","key":"\"act-33\""},{"title":"North East London and City (9)","url":"/actdev/collections/act-32","key":"\"act-32\""},{"title":"North West London (8)","url":"/actdev/collections/act-34","key":"\"act-34\""},{"title":"South East London (6)","url":"/actdev/collections/act-36","key":"\"act-36\""},{"title":"South West London (6)","url":"/actdev/collections/act-35","key":"\"act-35\""}]}]},{"title":"Midlands and East SHA Cluster (4)","url":"/actdev/collections/act-85","key":"\"act-85\"","children":[{"title":"East Midlands (1)","url":"/actdev/collections/act-91","key":"\"act-91\"","children":[{"title":"Derby City and Derbyshire (2)","url":"/actdev/collections/act-53","key":"\"act-53\""},{"title":"East Midlands (2)","url":"/actdev/collections/act-52","key":"\"act-52\""},{"title":"Leicestershire County & Rutland and Leicestershire City (2)","url":"/actdev/collections/act-54","key":"\"act-54\""},{"title":"Lincolnshire (1)","url":"/actdev/collections/act-55","key":"\"act-55\""},{"title":"Milton Keynes and Northamptonshire (2)","url":"/actdev/collections/act-56","key":"\"act-56\""},{"title":"Nottinghamshhire County and Nottingham City (2)","url":"/actdev/collections/act-57","key":"\"act-57\""}]},{"title":"East of England (1)","url":"/actdev/collections/act-92","key":"\"act-92\"","children":[{"title":"Bedfordshire and Luton (1)","url":"/actdev/collections/act-59","key":"\"act-59\""},{"title":"Cambridgeshire and Peterborough (3)","url":"/actdev/collections/act-62","key":"\"act-62\""},{"title":"Hertfordshire (1)","url":"/actdev/collections/act-58","key":"\"act-58\""},{"title":"Norfolk and Waveney (2)","url":"/actdev/collections/act-63","key":"\"act-63\""},{"title":"North, Mid and East Essex (1)","url":"/actdev/collections/act-60","key":"\"act-60\""},{"title":"South Essex (2)","url":"/actdev/collections/act-61","key":"\"act-61\""},{"title":"Suffolk (1)","url":"/actdev/collections/act-64","key":"\"act-64\""}]},{"title":"West Midlands (1)","url":"/actdev/collections/act-93","key":"\"act-93\"","children":[{"title":"Arden (2)","url":"/actdev/collections/act-65","key":"\"act-65\""},{"title":"Birmingham and Solihull (5)","url":"/actdev/collections/act-66","key":"\"act-66\""},{"title":"Black Country (4)","url":"/actdev/collections/act-67","key":"\"act-67\""},{"title":"Staffordshire (3)","url":"/actdev/collections/act-69","key":"\"act-69\""},{"title":"West Mercia (5)","url":"/actdev/collections/act-70","key":"\"act-70\""},{"title":"West Midlands (1)","url":"/actdev/collections/act-68","key":"\"act-68\""}]}]},{"title":"North of England SHA Cluster (7)","url":"/actdev/collections/act-84","key":"\"act-84\"","children":[{"title":"North East (3)","url":"/actdev/collections/act-88","key":"\"act-88\"","children":[{"title":"County Durham and Darlington (3)","url":"/actdev/collections/act-37","key":"\"act-37\""},{"title":"North of Tyne (5)","url":"/actdev/collections/act-38","key":"\"act-38\""},{"title":"South of Tyne (4)","url":"/actdev/collections/act-39","key":"\"act-39\""},{"title":"Tees (5)","url":"/actdev/collections/act-40","key":"\"act-40\""}]},{"title":"North West (1)","url":"/actdev/collections/act-89","key":"\"act-89\"","children":[{"title":"Cheshire, Warrington, Wirral (4)","url":"/actdev/collections/act-41","key":"\"act-41\""},{"title":"Cumbria (1)","url":"/actdev/collections/act-42","key":"\"act-42\""},{"title":"Greater Manchester (9)","url":"/actdev/collections/act-43","key":"\"act-43\""},{"title":"Lancashire (5)","url":"/actdev/collections/act-44","key":"\"act-44\""},{"title":"Merseyside (3)","url":"/actdev/collections/act-45","key":"\"act-45\""}]},{"title":"Yorkshire and the Humber (3)","url":"/actdev/collections/act-90","key":"\"act-90\"","children":[{"title":"Bradford (2)","url":"/actdev/collections/act-49","key":"\"act-49\""},{"title":"Calderdale, Kirklees and Wakefield (3)","url":"/actdev/collections/act-46","key":"\"act-46\""},{"title":"Humber (5)","url":"/actdev/collections/act-47","key":"\"act-47\""},{"title":"Leeds (1)","url":"/actdev/collections/act-50","key":"\"act-50\""},{"title":"North Yorkshire and York (1)","url":"/actdev/collections/act-51","key":"\"act-51\""},{"title":"South Yorkshire and Bassetlaw (5)","url":"/actdev/collections/act-48","key":"\"act-48\""}]}]},{"title":"South of England SHA Cluster (1)","url":"/actdev/collections/act-86","key":"\"act-86\"","children":[{"title":"South Central (0)","url":"/actdev/collections/act-94","key":"\"act-94\"","children":[{"title":"Berkshire (6)","url":"/actdev/collections/act-71","key":"\"act-71\""},{"title":"Buckinghamshire and Oxfordshire (2)","url":"/actdev/collections/act-73","key":"\"act-73\""},{"title":"Southampton, Hampshire, Isle of Wight & Portsmouth (5)","url":"/actdev/collections/act-72","key":"\"act-72\""}]},{"title":"South East Coast (1)","url":"/actdev/collections/act-95","key":"\"act-95\"","children":[{"title":"Kent and Medway (4)","url":"/actdev/collections/act-74","key":"\"act-74\""},{"title":"Surrey (1)","url":"/actdev/collections/act-75","key":"\"act-75\""},{"title":"Sussex (5)","url":"/actdev/collections/act-76","key":"\"act-76\""}]},{"title":"South West (0)","url":"/actdev/collections/act-96","key":"\"act-96\"","children":[{"title":"Bath, North East Somerset and Wiltshire (2)","url":"/actdev/collections/act-77","key":"\"act-77\""},{"title":"Bournemouth, Poole and Dorset (2)","url":"/actdev/collections/act-78","key":"\"act-78\""},{"title":"Bristol, North Somerset and South Gloucestershire (4)","url":"/actdev/collections/act-79","key":"\"act-79\""},{"title":"Devon, Plymouth and Torbay (3)","url":"/actdev/collections/act-80","key":"\"act-80\""},{"title":"Gloucestershire and Swindon (3)","url":"/actdev/collections/act-81","key":"\"act-81\""}]}]}]},{"title":"Think Tanks (30)","url":"/actdev/collections/act-146","key":"\"act-146\""}]}]},{"title":"History of Libraries Collection (0)","url":"/actdev/collections/act-155","key":"\"act-155\""},{"title":"History of the Book (0)","url":"/actdev/collections/act-156","key":"\"act-156\""},{"title":"Legal Aid Reform (0)","url":"/actdev/collections/act-157","key":"\"act-157\""},{"title":"Margaret Thatcher (77)","url":"/actdev/collections/act-151","key":"\"act-151\""},{"title":"Nelson Mandela (174)","url":"/actdev/collections/act-178","key":"\"act-178\""},{"title":"News Sites (576)","url":"/actdev/collections/act-173","key":"\"act-173\"","children":[{"title":"Hyperlocal (515)","url":"/actdev/collections/act-297","key":"\"act-297\""}]},{"title":"Oral History in the UK (2)","url":"/actdev/collections/act-158","key":"\"act-158\""},{"title":"Political Action and Communication (5)","url":"/actdev/collections/act-159","key":"\"act-159\""},{"title":"Religion, politics and law since 2005 (9)","url":"/actdev/collections/act-160","key":"\"act-160\""},{"title":"Religion/Theology (237)","url":"/actdev/collections/act-172","key":"\"act-172\""},{"title":"Scottish Independence Referendum (1144)","url":"/actdev/collections/act-171","key":"\"act-171\"","children":[{"title":"Charities, Churches and Third Sector (16)","url":"/actdev/collections/act-293","key":"\"act-293\""},{"title":"Commercial Publishers (1)","url":"/actdev/collections/act-294","key":"\"act-294\""},{"title":"Government (UK and Scottish) (83)","url":"/actdev/collections/act-291","key":"\"act-291\""},{"title":"National Campaigning Groups (80)","url":"/actdev/collections/act-288","key":"\"act-288\""},{"title":"Political Parties and Trade Unions (249)","url":"/actdev/collections/act-289","key":"\"act-289\""},{"title":"Press, Media & Comment (263)","url":"/actdev/collections/act-292","key":"\"act-292\""},{"title":"Think Tanks and Research Institutes (47)","url":"/actdev/collections/act-290","key":"\"act-290\""}]},{"title":"Slavery and Abolition in the Caribbean (0)","url":"/actdev/collections/act-161","key":"\"act-161\""},{"title":"Tour de France (Yorkshire) 2014 (60)","url":"/actdev/collections/act-264","key":"\"act-264\""},{"title":"UK relations with the Low Countries (0)","url":"/actdev/collections/act-162","key":"\"act-162\""},{"title":"UK response to Philippines disaster 2013 (501)","url":"/actdev/collections/act-176","key":"\"act-176\""},{"title":"Video Games (0)","url":"/actdev/collections/act-163","key":"\"act-163\""},{"title":"Winter Olympics Sochi 2014 (128)","url":"/actdev/collections/act-177","key":"\"act-177\""}]    

//        [{"title":"Health and Social Care Act 2012 - NHS Reforms","select":true,"key":"act-24","children":[
//       {"title":"NHS","key":"act-25","children":[
//          {"title":"Campaigning and Advocacy Groups","key":"act-148"},{"title":"Acute Trusts","key":"act-27"},{"title":"Clinical Commissioning Groups","key":"act-28"},{"title":"Strategic Health Authorities","key":"act-82","children":[
//              {"title":"London SHA Cluster","key":"act-83","children":[{"title":"London SHA","key":"act-87","children":[
//                   {"title":"North West London","key":"act-34"},{"title":"London","key":"act-31"},{"title":"North East London and City","key":"act-32"},{"title":"North Central London","key":"act-33"},{"title":"South West London","key":"act-35"},{"title":"South East London","key":"act-36"}]}]},{"title":"Midlands and East SHA Cluster","key":"act-85","children":[{"title":"East Midlands","key":"act-91","children":[{"title":"Nottinghamshhire County and Nottingham City","key":"act-57"},{"title":"Derby City and Derbyshire","key":"act-53"},{"title":"Leicestershire County & Rutland and Leicestershire City","key":"act-54"},{"title":"Lincolnshire","key":"act-55"},{"title":"Milton Keynes and Northamptonshire","key":"act-56"}]},{"title":"West Midlands","key":"act-93","children":[{"title":"Black Country","key":"act-67"},{"title":"Arden","key":"act-65"},{"title":"Birmingham and Solihull","key":"act-66"},{"title":"Staffordshire","key":"act-69"},{"title":"West Mercia","key":"act-70"}]},{"title":"East of England","key":"act-92","children":[{"title":"Hertfordshire","key":"act-58"},{"title":"Bedfordshire and Luton","key":"act-59"},{"title":"North,  Mid and East Essex","key":"act-60"},{"title":"South Essex","key":"act-61"},{"title":"Cambridgeshire and Peterborough","key":"act-62"},{"title":"Norfolk and Waveney","key":"act-63"},{"title":"Suffolk","key":"act-64"}]}]},{"title":"North of England SHA Cluster","key":"act-84","children":[{"title":"North West","key":"act-89","children":[{"title":"Greater Manchester","key":"act-43"},{"title":"Cheshire,  Warrington,  Wirral","key":"act-41"},{"title":"Cumbria","key":"act-42"},{"title":"Lancashire","key":"act-44"},{"title":"Merseyside","key":"act-45"}]},{"title":"North East","key":"act-88","children":[{"title":"County Durham and Darlington","key":"act-37"},{"title":"North of Tyne","key":"act-38"},{"title":"South of Tyne","key":"act-39"},{"title":"Tees","key":"act-40"}]},{"title":"Yorkshire and the Humber","key":"act-90","children":[{"title":"Calderdale,  Kirklees and Wakefield","key":"act-46"},{"title":"Humber","key":"act-47"},{"title":"South Yorkshire and Bassetlaw","key":"act-48"},{"title":"Bradford","key":"act-49"},{"title":"Leeds","key":"act-50"},{"title":"North Yorkshire and York","key":"act-51"}]}]},{"title":"South of England SHA Cluster","key":"act-86","children":[{"title":"South Central","key":"act-94","children":[{"title":"Berkshire","key":"act-71"},{"title":"Southampton,  Hampshire,  Isle of Wight & Portsmouth","key":"act-72"},{"title":"Buckinghamshire and Oxfordshire","key":"act-73"}]},{"title":"South East Coast","key":"act-95","children":[{"title":"Kent and Medway","key":"act-74"},{"title":"Surrey","key":"act-75"},{"title":"Sussex","key":"act-76"}]},{"title":"South West","key":"act-96","children":[{"title":"Bath,  North East Somerset and Wiltshire","key":"act-77"},{"title":"Bournemouth,  Poole and Dorset","key":"act-78"},{"title":"Bristol,  North Somerset and South Gloucestershire","key":"act-79"},{"title":"Devon,  Plymouth and Torbay","key":"act-80"},{"title":"Gloucestershire and Swindon","key":"act-81"}]}]}]},{"title":"Health and Wellbeing Boards","key":"act-29"},{"title":"Local Involvement Networks (LINks)","key":"act-30"},{"title":"Care Trust","key":"act-139"},{"title":"Public Health England","key":"act-149"},{"title":"Special Health Authorities","key":"act-140"},{"title":"Public Health Agencies","key":"act-134"},{"title":"Ambulance Trusts","key":"act-136"},{"title":"Mental Health Trusts","key":"act-138"},{"title":"Gateways","key":"act-141"},{"title":"NHS programmes","key":"act-142"},{"title":"Professional Bodies Trade Union","key":"act-147"},{"title":"Legislation","key":"act-143"},{"title":"Healthwatch","key":"act-144"},{"title":"Specialised Commissioning Group","key":"act-145"},{"title":"Think Tanks","key":"act-146"},{"title":"Press Comment","key":"act-150"},{"title":"Cancer Networks","key":"act-137"},{"title":"Regulators & Central Government","key":"act-135"},{"title":"Local Authorities","key":"act-166"},{"title":"Social Media (Facebook,  Twitter etc)","key":"act-167"},{"title":"Primary Care Trusts","key":"act-26"},{"title":"Private and voluntary sector providers","key":"act-169"}]}]},{"title":"Scottish Independence Referendum","key":"act-171","children":[{"title":"Press,  Media & Comment","key":"act-292"},{"title":"Political Parties and Trade Unions","key":"act-289"},{"title":"Think Tanks and Research Institutes","key":"act-290"},{"title":"National Campaigning Groups","key":"act-288"},{"title":"Government (UK and Scottish)","key":"act-291"},{"title":"Charities,  Churches and Third Sector","key":"act-293"}]},{"title":"Science,  Technology & Medicine","key":"act-99","children":[{"title":"Physics","key":"act-108"},{"title":"Astronomy","key":"act-107"}]},{"title":"Religion,  politics and law since 2005","key":"act-160"},{"title":"News Sites","key":"act-173"},{"title":"100 Best Sites","key":"act-170"},{"title":"Margaret Thatcher","key":"act-151"},{"title":"First World War Centenary,  2014-18","key":"act-174","children":[{"title":"Heritage Lottery Fund","key":"act-265"}]},{"title":"Religion/Theology","key":"act-172"},{"title":"European Parliament Elections 2014","key":"act-250","children":[{"title":"Political Parties: National","key":"act-256"},{"title":"Academia & think tanks","key":"act-257"},{"title":"Interest groups","key":"act-254"},{"title":"Regulation and Guidance","key":"act-253"},{"title":"Blogs","key":"act-263"},{"title":"Political Parties: Regional & Local","key":"act-258"},{"title":"Social Media","key":"act-259"},{"title":"EU Institutions","key":"act-260"},{"title":"Opinion Polls","key":"act-261"},{"title":"Candidates","key":"act-262"}]},{"title":"Nelson Mandela","key":"act-178"},{"title":"UK response to Philippines disaster 2013","key":"act-176"},{"title":"Commonwealth Games Glasgow 2014","key":"act-252","children":[{"title":"Organisational bodies/venues","key":"act-267"},{"title":"Sports","key":"act-268","children":[{"title":"Rugby Sevens","key":"act-277"},{"title":"Badminton","key":"act-285"},{"title":"Cycling","key":"act-283"},{"title":"Wrestling","key":"act-271"},{"title":"Hockey","key":"act-281"},{"title":"Boxing","key":"act-284"},{"title":"Aquatics","key":"act-287"},{"title":"Athletics","key":"act-286"},{"title":"Gymnastics","key":"act-282"},{"title":"Judo","key":"act-280"},{"title":"Lawn bowls","key":"act-279"},{"title":"Netball","key":"act-278"},{"title":"Shooting","key":"act-276"},{"title":"Squash","key":"act-275"},{"title":"Table tennis","key":"act-274"},{"title":"Triathlon","key":"act-273"},{"title":"Weightlifting","key":"act-272"}]},{"title":"Press & Media Comment","key":"act-269"},{"title":"Sponsors","key":"act-270"},{"title":"Competitors","key":"act-266"},{"title":"Cultural Programme","key":"act-295"}]},{"title":"Winter Olympics Sochi 2014","key":"act-177"},{"title":"Oral History in the UK","key":"act-158"},{"title":"Conservative Party Website deletions - Press articles November 2013","key":"act-175"},{"title":"Political Action and Communication","key":"act-159"},{"title":"Tour de France (Yorkshire) 2014","key":"act-264"}]

	public static JsonNode getSubjectsDataByIds(List<Long> mySubjectIds) {
		Logger.debug("getSubjectsDataByIds as List - selected = " + mySubjectIds.size());

		//List<Subject> firstLevel = Subject.getFirstLevelSubjects();

		List<ObjectNode> result = getSubjectTreeElementsByIds(getSubjectsData2(), null, true, mySubjectIds);
		//Logger.debug("subjects main level size: " + firstLevel.size());
		JsonNode jsonData = Json.toJson(result);
	    return jsonData;
	}

	@Cached(key = "SubjectsData")
	public static List<Subject> getSubjectsData2() {
		Logger.debug("SubjectsData");
		return Subject.getFirstLevelSubjects();
	}



	protected static List<ObjectNode> getSubjectTreeElementsByIds(List<Subject> subjects, String filter, boolean parent, List<Long> mySubjectIds) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
	
		for (Subject subject : subjects) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", subject.name);
			child.put("url", String.valueOf(routes.SubjectController.view(subject.id)));
			if (mySubjectIds != null && mySubjectIds.contains(subject.id)) {
				child.put("select", true);
			}
			child.put("key", subject.id);
	    	List<Subject> children = Subject.findChildrenByParentId(subject.id);
	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getSubjectTreeElementsByIds(children, filter, false, mySubjectIds)));
	    	}
			result.add(child);
		}
	//        	Logger.debug("getSubjectTreeElements() res: " + result);
		return result;
	}
    
    
	public static JsonNode getSubjectsData() {
		return getSubjectsData(null);
	}

	/**
	 * This method computes a tree of subjects in JSON format. 
	 * @param subjectUrl This is an identifier for current selected object
	 * @return tree structure
	 */
	protected static JsonNode getSubjectsData(List<Subject> mySubjects) {    	
		List<Subject> firstLevel = Subject.getFirstLevelSubjects();
		List<ObjectNode> result = getSubjectTreeElements(firstLevel, true, mySubjects);
		Logger.debug("subjects main level size: " + firstLevel.size());
		JsonNode jsonData = Json.toJson(result);
	    return jsonData;
	}
	
    protected static JsonNode getSubjectsDataByFilter(String filter) {
		List<Subject> firstLevel = Subject.getFirstLevelSubjects();
    	List<ObjectNode> result = getSubjectTreeElements(firstLevel, filter, true);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }

    protected static List<ObjectNode> getSubjectTreeElements(List<Subject> subjects, String filter, boolean parent) {
    	return getSubjectTreeElements(subjects, filter, parent, null);
    }
    
	protected static List<ObjectNode> getSubjectTreeElements(List<Subject> firstLevel, boolean parent, List<Subject> mySubjects) { 
		return getSubjectTreeElements(firstLevel, null, parent, mySubjects);
	}
	
	/**
	 * This method calculates first order subjects.
	 * @param subjectList The list of all subjects
	 * @param filter This is an identifier for current selected object
	 * @param parent This parameter is used to differentiate between root and children nodes
	 * @return subject object in JSON form
	 */
	protected static List<ObjectNode> getSubjectTreeElements(List<Subject> subjects, String filter, boolean parent, List<Subject> mySubjects) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
	
		for (Subject subject : subjects) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", subject.name);
			child.put("url", String.valueOf(routes.SubjectController.view(subject.id)));
			if (mySubjects != null && mySubjects.contains(subject)) {
				child.put("select", true);
			}
			child.put("key", subject.id);
	    	List<Subject> children = Subject.findChildrenByParentId(subject.id);
	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getSubjectTreeElements(children, filter, false, mySubjects)));
	    	}
			result.add(child);
		}
	//        	Logger.debug("getSubjectTreeElements() res: " + result);
		return result;
	}
    
}