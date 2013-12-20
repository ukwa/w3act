package models;

import java.sql.Timestamp;
import java.util.List;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Permission extends Model
{
    @Id
    public Long id;

    @Column(columnDefinition = "TEXT")
    public String name;

    @Column(columnDefinition = "TEXT")
    public String url;
    
    @Column(columnDefinition = "TEXT")
    public String description;
    
    @Column(columnDefinition = "TEXT")
    public String revision; 
    
    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, Permission> find = new Model.Finder<Long, Permission>(Long.class, Permission.class);

    public String getName()
    {
        return name;
    }

    public static Permission findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve all permissions.
     */
    public static List<Permission> findAll() {
        return find.all();
    }
    
    public String toString() {
        return "Permission(" + name + ")" + ", id:" + id;
    }

}