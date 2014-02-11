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
public class WebSite extends Controller {
  
    /**
     * Display the create content page.
     */
    public static Result index() {
		return ok(
            website.render("Website", User.find.byId(request().username()), 
            		models.Organisation.findInvolving(), models.DCollection.findInvolving(), "")
        );
    }
	
	
    /**
     * This method displays add content page for given URL.
     * @param url
     * @return
     */
    public static Result addEntry(String url) {
		return ok(
            website.render("Website", User.find.byId(request().username()), 
            		models.Organisation.findInvolving(), models.DCollection.findInvolving(), url)
        );
    }
    
}

