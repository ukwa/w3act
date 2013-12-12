package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Permission extends Model
{
    @Id
    public Long id;

    public String name;

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
    
    public String toString() {
        return "Permission(" + name + ")" + ", id:" + id;
    }

}