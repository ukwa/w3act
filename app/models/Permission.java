package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permission")
public class Permission extends ActModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2250099575468302989L;

	@JoinTable(name = "permission_role", joinColumns = { @JoinColumn(name = "permission_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") }) 
	@ManyToMany
	@JsonIgnore
    public List<Role> roles;
 
    @Required(message="Name is required")
    @Column(columnDefinition = "text")
    public String name;

    @JsonIgnore
    @Column(columnDefinition = "text")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 
    
    public static final Model.Finder<Long, Permission> find = new Model.Finder<Long, Permission>(Long.class, Permission.class);

    /**
     * Retrieve all permissions.
     */
    public static List<Permission> findAll() {
        return find.where().orderBy("name asc").findList();
    }
    
    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static Permission findById(Long id) {
    	Permission res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    public static Permission findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
    
	/**
	 * This method filters permissions by name and returns a list of filtered Permission objects.
	 * @param name
	 * @return
	 */
	public static List<Permission> filterByName(String name) {
        return find.where().icontains(Const.NAME, name).findList();
	}
        
    /**
     * Return a page of Permissions
     *
     * @param page Page to display
     * @param pageSize Number of Permissions per page
     * @param sortBy User property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Permission> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

    public static Map<String, Boolean> options(List<Permission> myPermissions) {
        Map<String, Boolean> permissionsMap = new HashMap<String, Boolean>();
        for (Permission permission : Permission.findAll()) {
          permissionsMap.put(permission.name, (myPermissions != null && myPermissions.contains(permission)));
        }
        return permissionsMap;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
    @Override
	public String toString() {
		return "Permission [name=" + name + ", description=" + description
				+ "]";
	}
}