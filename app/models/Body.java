package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Body entity managed by Ebean
 */
@Entity 
@Table(name="body")
public class Body extends Model {

    @Id
//    @Constraints.Required
    public Long id;
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public String value; 
    public String summary;
    public String format; 
    
    @ManyToOne(cascade=CascadeType.ALL)  
    public Target target;

    
    // -- Queries
    
    public static Model.Finder<String,Body> find = new Model.Finder(String.class, Body.class);
    
    public Body() {
    }
    
    /**
     * Retrieve all bodies.
     */
    public static List<Body> findAll() {
        return find.all();
    }

    /**
     * Retrieve a Body from value.
     */
    public static Body findByValue(String value) {
        return find.where().eq("value", value).findUnique();
    }
    // --
    
    public String toString() {
        return "Body(" + value + ")";
    }

}


