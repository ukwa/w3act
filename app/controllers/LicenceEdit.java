package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Taxonomy;
import models.DCollection;
import models.Organisation;
import models.Role;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;

import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.licence.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

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
                    "Licences", User.find.byId(request().username()), resList, ""
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
                        "Licences", User.find.byId(request().username()), resList, name
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
    		exp = exp.contains(Const.TYPE, Const.LICENCE);
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

