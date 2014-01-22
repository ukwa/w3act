package controllers;

import java.util.List;

import models.DCollection;
import models.Target;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.collections.*;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.BodyParser;                     
import play.libs.Json;

/**
 * Manage collections.
 */
@Security.Authenticated(Secured.class)
public class CollectionEdit extends AbstractController {
  
    /**
     * Display the collection.
     */
    public static Result index() {
        return ok(
        );
    }
    
    /**
     * Display the collection edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("collection url: " + url);
		DCollection collection = DCollection.findByUrl(url);
		Logger.info("collection title: " + collection.title + ", url: " + url);
        return ok(
                collectionedit.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                collectionview.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result sites(String url) {
        return ok(
                collectionsites.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }    
    
    /**
     * Administer collections
     * @param url
     * @return
     */
//    public static Result admin(String url) {
//        return ok(
//                collectionadmin.render(
//                        DCollection.findByUrl(url), User.find.byId(request().username())
//                )
//            );
//    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("CollectionEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        
		if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.CollectionEdit.addEntry(name));
        	} else {
        		Logger.info("DCollection name is empty. Please write name in search window.");
                res = ok(
                        dcollections.render(
                            "DCollections", User.find.byId(request().username()), models.DCollection.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		dcollections.render(
                        "DCollections", User.find.byId(request().username()), models.DCollection.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * Add new collection entry.
     * @param collection title
     * @return
     */
    public static Result addEntry(String title) {
    	DCollection collection = new DCollection();
    	collection.title = title;
        collection.nid = Target.createId();
        collection.url = Const.ACT_URL + collection.nid;
		Logger.info("add entry with url: " + collection.url + ", and title: " + collection.title);
        return ok(
                collectionedit.render(
                      collection, User.find.byId(request().username())
                )
            );
    }

  @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<DCollection> dCollections = DCollection.filterByName(name);
	        jsonData = Json.toJson(dCollections);
        }
        return ok(jsonData);
    }
    
}

