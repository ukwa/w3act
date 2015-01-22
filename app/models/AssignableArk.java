package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class AssignableArk extends Model {
	@Id
	public String ark;
	
	public static final Model.Finder<String, AssignableArk> find = new Model.Finder<>(String.class, AssignableArk.class);
	
	public AssignableArk(String ark) {
		this.ark = ark;
	}
}