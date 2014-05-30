package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.licence.licences;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage persons.
 */
@Security.Authenticated(Secured.class)
public class LicenceEdit extends AbstractController {
  
    /**
     * Display the person.
     */
    public static Result index() {
        List<Taxonomy> resList = processFilterLicences("");
        return ok(
                licences.render(
                    "Licences", User.findByEmail(request().username()), resList, ""
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("LicenceEdit.filter()");
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<Taxonomy> resList = processFilterLicences(name);
        Logger.info("search: " + search + ", name: " + name);
        if (search != null) {
            res = ok(
            		licences.render(
                        "Licences", User.findByEmail(request().username()), resList, name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl persons.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<Taxonomy> processFilterLicences(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<Taxonomy> exp = Taxonomy.find.where();
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);    		
    		exp = exp.contains(Const.TTYPE, Const.LICENCE);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.Taxonomy.findListByType(Const.LICENCE);
    	}
        return res;
    }        

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Taxonomy> licences = Taxonomy.filterByName(name);
	        jsonData = Json.toJson(licences);
        }
        return ok(jsonData);
    }
}

