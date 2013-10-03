package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;


/**
 * Item entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name="item")
public class Item extends Model {

    public String value; 
       
    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<String,Item> find = new Model.Finder(String.class, Item.class);
    
    public Item() {
    }
    
    /**
     * Retrieve all bodies.
     */
    public static List<Item> findAll() {
        return find.all();
    }

    /**
     * Retrieve a Item from value.
     */
    public static Item findByValue(String value) {
        return find.where().eq("value", value).findUnique();
    }
    // --
    
    public String toString() {
        return "Item(" + value + ")";
    }

}


