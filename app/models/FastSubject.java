package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class FastSubject extends Model {
	@Id
	public String id;
	public String name;
	
	public static final Model.Finder<String, FastSubject> find = new Model.Finder<>(String.class, FastSubject.class);
}