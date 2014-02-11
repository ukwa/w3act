package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;
import uk.bl.api.*;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.io.FileNotFoundException;
import play.libs.Json;

import uk.bl.Const;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class Article extends AbstractController {
  
	private static Target previewObj;
	
    /**
     * Display the About tab.
     */
    public static Result index() {
		return ok(
            article.render("Article", User.find.byId(request().username()))
        );
    }
    
    public static Result preview() {
		return ok(
            articlepreview.render("ArticlePreview", User.find.byId(request().username()), previewObj)
        );
    }
    
    public static Result addArticle() {
    	Result res;
        Target target = new Target();
        target.title = getFormParam(Const.TITLE);
        target.summary = getFormParam(Const.SUMMARY);
        UUID id = UUID.randomUUID();
        Logger.info("id: " + id.toString());
        target.nid = id.getMostSignificantBits();
        String save = getFormParam("save");
        String preview = getFormParam("preview");
//        Logger.info("save: " + save + ", preview: " + preview);
        if (save != null) {
	        target.save();
	        Logger.info("add article: " + target.toString());
	        res = redirect(routes.Article.index());
        } else {
        	previewObj = target;
	        res = redirect(routes.Article.preview());
        }
        return res;
    }
	
}

