package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;

import models.*;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.*;

import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class Targets extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
    	List<Target> targetsAll = models.Target.findAllActive();
    	List<Target> targetsRes = targetsAll.subList(0, Const.ROWS_PER_PAGE);
        return ok(
            targets.render(
//                    "Targets", User.find.byId(request().username()), models.Target.findInvolving(), 
                    "Targets", User.find.byId(request().username()), targetsRes, 
                	User.findAll(), models.Organisation.findInvolving(),
                	Const.NONE, Const.NONE, Const.NONE, Const.NONE, Const.NONE, 
                	Const.NONE, Const.NONE, 0, models.Target.findAllActive().size(), "", Const.NONE
            )
        );
    }

    public static Result offset(int offset) {
    	List<Target> targetsAll = models.Target.findAllActive();
    	List<Target> targetsRes = targetsAll.subList(offset*Const.ROWS_PER_PAGE, (offset+1)*Const.ROWS_PER_PAGE);
        return ok(
            targets.render(
//                    "Targets", User.find.byId(request().username()), models.Target.findInvolving(), 
                    "Targets", User.find.byId(request().username()), targetsRes, 
                	User.findAll(), models.Organisation.findInvolving(),
                	Const.NONE, Const.NONE, Const.NONE, Const.NONE, Const.NONE, 
                	Const.NONE, Const.NONE, offset, targetsAll.size(), "", Const.NONE
            )
        );
    }

    public static Result filterUrl() {
        String filterUrl = getFormParam(Const.FILTER);
        String clear = getFormParam(Const.CLEAR);
        String field_collection_categories = Const.NONE;
        String field_nominating_organisation = Const.NONE;
        String author = Const.NONE;
        String field_subject = Const.NONE;
        String field_crawl_frequency = Const.NONE;
        String field_depth = Const.NONE;
        String field_scope = Const.NONE;
        String field_license = Const.NONE;
        int offset = 0;
        int limit = 0;
        boolean isClear = false;
        if (clear != null) {
        	filterUrl = "";
        	isClear = true;
        }
//		Logger.info("author: " + getFormParam(Const.AUTHOR) + ", user: " + User.findByName(getFormParam(Const.AUTHOR)).url);
        if (!isClear && getFormParam(Const.AUTHOR) != null && !getFormParam(Const.AUTHOR).toLowerCase().contains(Const.NONE)) {
       		author = User.findByName(getFormParam(Const.AUTHOR)).url;
        }
        if (!isClear && getFormParam(Const.FIELD_NOMINATING_ORGANISATION) != null
        	&& !getFormParam(Const.FIELD_NOMINATING_ORGANISATION).toLowerCase().contains(Const.NONE)) {
       		field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.FIELD_NOMINATING_ORGANISATION)).url;
        }
        if (!isClear && getFormParam(Const.FIELD_COLLECTION_CATEGORIES) != null
            	&& !getFormParam(Const.FIELD_COLLECTION_CATEGORIES).toLowerCase().contains(Const.NONE)) {
       		field_collection_categories = DCollection.findByTitle(getFormParam(Const.FIELD_COLLECTION_CATEGORIES)).url;
        }
        if (!isClear && getFormParam(Const.FIELD_SUBJECT) != null
            	&& !getFormParam(Const.FIELD_SUBJECT).toLowerCase().contains(Const.NONE)) {
       		field_subject = Taxonomy.findByName(getFormParam(Const.FIELD_SUBJECT)).url;
        }
        if (!isClear && getFormParam(Const.FIELD_CRAWL_FREQUENCY) != null
            	&& !getFormParam(Const.FIELD_CRAWL_FREQUENCY).toLowerCase().contains(Const.NONE)) {
       		field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
        }
        if (!isClear && getFormParam(Const.FIELD_DEPTH) != null
            	&& !getFormParam(Const.FIELD_DEPTH).toLowerCase().contains(Const.NONE)) {
       		field_depth = getFormParam(Const.FIELD_DEPTH);
        }
        if (!isClear && getFormParam(Const.FIELD_LICENSE) != null
            	&& !getFormParam(Const.FIELD_LICENSE).toLowerCase().contains(Const.NONE)) {
       		field_license = getFormParam(Const.FIELD_LICENSE);
        }
        if (!isClear && getFormParam(Const.FIELD_SCOPE) != null
            	&& !getFormParam(Const.FIELD_SCOPE).toLowerCase().contains(Const.NONE)) {
       		field_scope = getFormParam(Const.FIELD_SCOPE);
        }
        Logger.info("filterUrl offset param: " + getFormParam(Const.OFFSET));
        String offsetStr = getFormParam(Const.OFFSET);
        if (offsetStr != null) {
        	offset = Integer.valueOf(offsetStr);
        } 
        String limitStr = getFormParam(Const.LIMIT);
        if (limitStr != null) {
        	limit = Integer.valueOf(limitStr);
        }
        Logger.info("filterUrl offset: " + offset + ", limit: " + limit);

    	List<Target> targetsAll = processTargets(author, field_nominating_organisation, field_collection_categories, 
        		field_subject, field_crawl_frequency, field_depth, field_scope, offset, limit, filterUrl, field_license);
//    	Logger.info("target edit targetsAll: " + targetsAll.size() + ", offset: " + offset + ", limit: " + limit);
    	int rowCount = Const.ROWS_PER_PAGE;
    	List<Target> targetsRes = new ArrayList<Target>();
    	if (targetsAll.size() < (offset+1)*Const.ROWS_PER_PAGE
    			|| targetsAll.size() < offset*Const.ROWS_PER_PAGE
    			|| targetsAll.size() < Const.ROWS_PER_PAGE) {
    		rowCount = targetsAll.size();
    		offset = 0;
        	targetsRes = targetsAll.subList(0, rowCount);
    	} else {
    		targetsRes = targetsAll.subList(offset*Const.ROWS_PER_PAGE, (offset+1)*Const.ROWS_PER_PAGE);
    	}
//       	Logger.info("target edit rowCount: " + rowCount + ", offset: " + offset);
//    	Logger.info("target edit targetsRes: " + targetsRes.size());
        return ok(
                targets.render(
   			        "Targets", User.find.byId(request().username()), targetsRes, 
		        	User.findFilteredByUrl(author), models.Organisation.findFilteredByUrl(field_nominating_organisation),
			        	author, field_nominating_organisation, field_collection_categories, field_subject, 
			        	field_crawl_frequency, field_depth, field_scope, offset, targetsAll.size(), filterUrl, 
			        	field_license
                        )
                );

//    	List<Target> targetsAll = models.Target.filterActiveUrl(filterUrl);
//    	int rowCount = Const.ROWS_PER_PAGE;
//    	if (targetsAll.size() < Const.ROWS_PER_PAGE) {
//    		rowCount = targetsAll.size();
//    	}
//    	List<Target> targetsRes = targetsAll.subList(0, rowCount);
//        return ok(
//                targets.render(
//                    "Targets", User.find.byId(request().username()), targetsRes, 
//                	User.findAll(), models.Organisation.findAll(),
//                	Const.NONE, Const.NONE, Const.NONE, Const.NONE, Const.NONE, 
//                	Const.NONE, Const.NONE, 0, targetsAll.size(), filterUrl
//                )
//            );
    }
    
//    /**
//     * This method converts Target list to string in order to export it in a CSV file.
//     * @param targetsList
//     * @return resulting string
//     */
//    public static String convertTargetListToString(List<Target> targetsList) {
//    	String res = "";
//        Logger.info("convertTargetListToString before getFormParam");
//        String exportBtn = getFormParam(Const.EXPORT);
//        Logger.info("convertTargetListToString exportBtn: " + exportBtn);
////        if (exportBtn != null) {        
//	        StringWriter sw = new StringWriter();
//	        // header of CSV file
//    		sw.append(Const.NID);
////    		Logger.info("res1: " + res);
//	 	    sw.append(Const.CSV_SEPARATOR);
//    		sw.append(Const.TITLE);
//	 	    sw.append(Const.CSV_SEPARATOR);
//    		sw.append(Const.URL);
//	 	    sw.append(Const.CSV_SEPARATOR);
//    		sw.append(Const.AUTHOR);
//	 	    sw.append(Const.CSV_SEPARATOR);
//    		sw.append(Const.FIELD_COLLECTION_CATEGORIES);
//	 	    sw.append(Const.CSV_SEPARATOR);
//    		sw.append(Const.FIELD_NOMINATING_ORGANISATION); //TODO url or name?
//	 	    sw.append(Const.CSV_SEPARATOR);
//	 	    sw.append(Const.CSV_LINE_END);
//
//	    	Logger.info("export list size: " + targetsList.size());
//	    	Iterator<Target> itrTargets = targetsList.iterator();
//	    	while (itrTargets.hasNext()) {
//	    		Target target = itrTargets.next();
//	    		sw.append(String.valueOf(target.nid));
//	//    		Logger.info("res1: " + res);
//		 	    sw.append(Const.CSV_SEPARATOR);
//	    		sw.append(target.title);
//		 	    sw.append(Const.CSV_SEPARATOR);
//	    		sw.append(target.url);
//		 	    sw.append(Const.CSV_SEPARATOR);
//	    		sw.append(target.author);
//		 	    sw.append(Const.CSV_SEPARATOR);
//	    		sw.append(target.field_collection_categories);
//		 	    sw.append(Const.CSV_SEPARATOR);
//	    		sw.append(target.field_nominating_organisation);
//		 	    sw.append(Const.CSV_SEPARATOR);
//		 	    sw.append(Const.CSV_LINE_END);
//	    	}
//	    	try {
//				res = URLEncoder.encode(sw.toString(), Const.STR_FORMAT);
//			} catch (UnsupportedEncodingException e) {
//				Logger.info("Encoding error. " + e);
//			}
////        }
//    	Logger.info("export string: " + res);
//    	return res;
//    }
    
    /**
     * This method exports selected targets to CSV file.
     * @param curatorUrl
     * @param organisationUrl
     * @param collectionCategoryUrl
     * @param subjectUrl
     * @param crawlFrequency
     * @param depth
     * @param scope
     * @param offset The current page number
     * @param limit The maximal row count
     * @return
     */
//    public static Result export(String targetList) {
//        public static Result export(List<Target> targetList) {
        public static Result export() {
    	Logger.info("export()");
//        String exportBtn = getFormParam(Const.EXPORT);
//        Logger.info("export exportBtn: " + exportBtn);

        StringWriter sw = new StringWriter();
		Field[] fields = Target.class.getFields();
		for (Field f : fields) {
//			Logger.info("Target fields: " + f.getName());
    		sw.append(f.getName());
	 	    sw.append(Const.CSV_SEPARATOR);
		}
 	    sw.append(Const.CSV_LINE_END);
 	    
// 	    String csv = "";
// 	    if (targetList != null && targetList.size() > 0) {
// 	    	Iterator<Target> itr = targetList.iterator();
// 	    	while (itr.hasNext()) {
// 	    		Target target = itr.next();
// 	    		csv = csv + ", " + target.toString();
// 	    	}
// 	    }
		String csv = getFormParam(Const.CSV);
        Logger.info("csv: " + csv);
        if (csv != null) {
	        String content = csv.replace(", " + Const.TARGET_DEF,  "").replace("[", "").replace("]", "").substring(Const.TARGET_DEF.length());
	        sw.append(content);
//        Logger.info("content: " + content);
        }

    	Utils.generateCsvFile(Const.EXPORT_FILE, sw.toString());
    	return redirect(routes.Targets.index());
    }
    
    /**
     * Display the targets panel for this user URL.
     * @param curatorUrl
     * @param organisationUrl
     * @param collectionCategoryUrl
     * @param subjectUrl
     * @param crawlFrequency
     * @param depth
     * @param scope
     * @param offset The current page number
     * @param limit The maximal row count
     * @param license
     * @return
     */
    public static Result edit(String curatorUrl, String organisationUrl, String collectionCategoryUrl, 
    		String subjectUrl, String crawlFrequency, String depth, String scope, int offset, int limit, String license) {
    	
        String filterUrl = getFormParam(Const.FILTER);
        Logger.info("Filter: " + filterUrl);

    	List<Target> targetsAll = processTargets(curatorUrl, organisationUrl, collectionCategoryUrl, 
        		subjectUrl, crawlFrequency, depth, scope, offset, limit, filterUrl, license);
//    	Logger.info("target edit targetsAll: " + targetsAll.size() + ", offset: " + offset + ", limit: " + limit);
    	int rowCount = Const.ROWS_PER_PAGE;
    	List<Target> targetsRes = new ArrayList<Target>();
    	if (targetsAll.size() < (offset+1)*Const.ROWS_PER_PAGE
    			|| targetsAll.size() < offset*Const.ROWS_PER_PAGE
    			|| targetsAll.size() < Const.ROWS_PER_PAGE) {
    		rowCount = targetsAll.size();
    		offset = 0;
        	targetsRes = targetsAll.subList(0, rowCount);
    	} else {
    		targetsRes = targetsAll.subList(offset*Const.ROWS_PER_PAGE, (offset+1)*Const.ROWS_PER_PAGE);
    	}
//       	Logger.info("target edit rowCount: " + rowCount + ", offset: " + offset);
//    	Logger.info("target edit targetsRes: " + targetsRes.size());
        return ok(
                targets.render(
   			        "Targets", User.find.byId(request().username()), targetsRes, 
		        	User.findFilteredByUrl(curatorUrl), models.Organisation.findFilteredByUrl(organisationUrl),
			        	curatorUrl, organisationUrl, collectionCategoryUrl, subjectUrl, crawlFrequency, depth, 
			        	scope, offset, targetsAll.size(), filterUrl, license
                        )
                );
    }
    
    /**
     * Process targets according to passed filter parameters.
     * @param curatorUrl
     * @param organisationUrl
     * @param collectionCategoryUrl
     * @param subjectUrl
     * @param crawlFrequency
     * @param depth
     * @param scope
     * @param offset The current page number
     * @param limit The maximal row count
     * @param filterUrl
     * @param license
     * @return
     */
    public static List<Target> processTargets(String curatorUrl, String organisationUrl, String collectionCategoryUrl, 
    		String subjectUrl, String crawlFrequency, String depth, String scope, int offset, int limit, String filterUrl, String license) {
//    	Logger.info("target edit curatorUrl: " + curatorUrl + ", organisationUrl: " + organisationUrl);
    	boolean isProcessed = false;
    	ExpressionList<Target> exp = Target.find.where();
    	List<Target> res = new ArrayList<Target>();
    	if (filterUrl != null && filterUrl.length() > 0) {
    		exp = exp.eq(Const.ACTIVE, true).contains(Const.FIELD_URL_NODE, filterUrl);
    		isProcessed = true;
    	}
    	if (curatorUrl != null && !curatorUrl.equals(Const.NONE)) {
    		exp = exp.eq(Const.AUTHOR, curatorUrl);
    		isProcessed = true;
    	}
    	if (organisationUrl != null && !organisationUrl.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_NOMINATING_ORGANISATION, organisationUrl);
    		isProcessed = true;
    	} 
    	if (subjectUrl != null && !subjectUrl.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_SUBJECT, subjectUrl);
    		isProcessed = true;
    	} 
    	if (crawlFrequency != null && !crawlFrequency.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_CRAWL_FREQUENCY, crawlFrequency);
    		isProcessed = true;
    	} 
    	if (depth != null && !depth.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_DEPTH, depth);
    		isProcessed = true;
    	} 
    	if (scope != null && !scope.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_SCOPE, scope);
    		isProcessed = true;
    	} 
    	if (collectionCategoryUrl != null && !collectionCategoryUrl.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_COLLECTION_CATEGORIES, collectionCategoryUrl);
    		isProcessed = true;
    	} 
    	if (license != null && !license.equals(Const.NONE)) {
    		exp = exp.eq(Const.FIELD_LICENSE, scope);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size());

        if (!isProcessed) {
    		res = models.Target.findAllActive();
    	}
        return res;
    }
    
    /**
     * This method defines range for current page
     * @param offset
     * @return start page number for range
     */
    public static int getStartPage(int offset) {
    	int res = 0;
    	res = offset/Const.PAGINATION_OFFSET; 
//    	Logger.info("get start page offset: " + offset + ", start page: " + res);
    	return res*Const.PAGINATION_OFFSET;
    }
    
	/**
	 * This method filters targets by given URLs.
	 * @return duplicate count
	 */
	public static List<Target> getSubjects() {
		List<Target> res = new ArrayList<Target>();
//		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_subject != null && target.field_subject.length() > 0 && !res.contains(target)) {
//				if (target.field_subject != null && target.field_subject.length() > 0 && !subjects.contains(target.field_subject)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_subject", target.field_subject);
		        if (ll.findRowCount() > 0) {
//		        	subjects.add(target.field_subject);
		        	res.add(target);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by given scope.
	 * @return scope list
	 */
	public static List<Target> getScope() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_scope != null && target.field_scope.length() > 0 && !subjects.contains(target.field_scope)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_scope", target.field_scope);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_scope);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method calculates current pagination range based on given current page number.
	 * @param offset
	 * @return a set of page numbers
	 */
	public static List<Integer> getRange(int offset) {
		List<Integer> ll = new ArrayList<Integer>();
		int i = offset;
		while (i < offset + Const.PAGINATION_OFFSET) {
			ll.add(i);
			i++;
		}
		return ll;
	}
	
	/**
	 * This method calculates previous pagination range based on given current page number.
	 * @param offset
	 * @return a set of page numbers
	 */
	public static int getPrev(int offset) { 
	    return offset - Const.PAGINATION_OFFSET;
	}
	
	/**
	 * This method calculates next pagination range based on given current page number.
	 * @param offset
	 * @return a set of page numbers
	 */
	public static int getNext(int offset) { 
	    return offset + Const.PAGINATION_OFFSET;
	}
	
	/**
	 * This method checks if previous pages exist.
	 * @param offset
	 * @return
	 */
	public static boolean checkPrev(int offset) {
		boolean res = true;
		if (offset - (Const.PAGINATION_OFFSET) < 0) {
			res = false;
		}
//		Logger.info("check prev offset: " + offset + ", res: " + res);
		return res;
	}
	
	/**
	 * This method checks if next pages exist.
	 * @param offset
	 * @return
	 */
	public static boolean checkNext(int offset, int limit) {
		boolean res = true;
		if (offset > limit/Const.ROWS_PER_PAGE - 2) { // because of starting by 0 and already presented last page
			res = false;
		}
//		Logger.info("check next offset: " + offset + ", limit: " + limit + ", res: " + res);
		return res;
	}
	
	/**
	 * This method calculates maximal page number.
	 * @param offset
	 * @return a set of page numbers
	 */
	public static int getMaxPageNumber(int limit) { 
	    return limit/Const.ROWS_PER_PAGE;
	}
	
	/**
	 * This method is required for checking of "Next" button in pagination.
	 * @return
	 */
	public static int getPaginationOffset() { 
	    return Const.PAGINATION_OFFSET;
	}
	
	/**
	 * This method retrieves targets from database for given taxonomy URL.
	 * @param url
	 * @return
	 */
	public static List<Target> getTargetsForTaxonomy(String url) {
		List<Target> res = new ArrayList<Target>();
//		Logger.info("url: " + url);
		if (url != null) {
	        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, url);
	        res = ll.findList();
		}
//		Logger.info("res size: " + res.size());
		return res;
	}
	
	/**
	 * This method filters targets by given license.
	 * @return scope list
	 */
	public static List<Taxonomy> getLicense() {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_license != null && target.field_license.length() > 0 && !subjects.contains(target.field_license)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_license", target.field_license);
		        if (ll.findRowCount() > 0) {
		        	Taxonomy taxonomy = Taxonomy.findByUrl(target.field_license);
//		        	Logger.info("target.field_license: " + target.field_license + ".");
//		        	Logger.info("taxonomy url: " + taxonomy.url);
//		        	Logger.info("license: " + taxonomy.name);
		        	res.add(taxonomy);
		        	subjects.add(target.field_license);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by crawl frequency.
	 * @return scope list
	 */
	public static List<Target> getCrawlFrequency() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_crawl_frequency != null && target.field_crawl_frequency.length() > 0 && !subjects.contains(target.field_crawl_frequency)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_crawl_frequency", target.field_crawl_frequency);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_crawl_frequency);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by depth.
	 * @return scope list
	 */
	public static List<Target> getDepth() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_depth != null && target.field_depth.length() > 0 && !subjects.contains(target.field_depth)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_depth", target.field_depth);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_depth);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by collection categories.
	 * @return scope list
	 */
	public static List<Taxonomy> getCollectionCategories() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_collection_categories != null && target.field_collection_categories.length() > 0 && !subjects.contains(target.field_collection_categories)) {
		        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, target.field_collection_categories);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_collection_categories);
		        	Taxonomy taxonomy = Taxonomy.findByUrl(target.field_collection_categories);
		        	taxonomies.add(taxonomy);
		        }
			}
		}
    	return taxonomies;
	}
	
}

