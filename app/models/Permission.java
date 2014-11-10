package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Permission")
public class Permission extends ActModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2250099575468302989L;

	//bi-directional many-to-one association to Role
//	@ManyToOne
//	@JoinColumn(name="id_role")
//	public Role role;
    
    //bi-directional many-to-many association to Role
    @ManyToMany
	@JoinTable(name = Const.PERMISSION_ROLE, joinColumns = { @JoinColumn(name = "permission_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") }) 
    private List<Role> roles = new ArrayList<Role>();
 
    @Required
    @Column(columnDefinition = "text")
    public String name;

    @JsonIgnore
    @Column(columnDefinition = "text")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 
    
    public static final Model.Finder<Long, Permission> find = new Model.Finder<Long, Permission>(Long.class, Permission.class);

    public String getName() {
        return name;
    }

    public List<Role> getRoles() {
    	return this.roles;
    }
    
    public void setRoles(List<Role> roles) {
    	this.roles = roles;
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
    
    public static Permission findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve a permission by URL.
     * @param url
     * @return permission name
     */
    public static Permission findByUrl(String url) {
//    	Logger.info("permission findByUrl: " + url);
    	Permission res = new Permission();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

	/**
	 * This method filters permissions by name and returns a list of filtered Permission objects.
	 * @param name
	 * @return
	 */
	public static List<Permission> filterByName(String name) {
		List<Permission> res = new ArrayList<Permission>();
        ExpressionList<Permission> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
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
    
    public static boolean isIncluded(String permissionName, String permissions) {
    	boolean res = false;
    	if (permissionName != null && permissionName.length() > 0 && permissions != null && permissions.length() > 0 ) {
    		if (permissions.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(permissions.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			String currentRoleName = itr.next();
        			currentRoleName = currentRoleName.replaceAll(" ", "");
        			if (currentRoleName.equals(permissionName)) {
        				res = true;
        				break;
        			}
    			}
    		} else {
    			if (permissions.equals(permissionName)) {
    				res = true;
    			}
    		}
    	}
    	return res;
    }

    /**
     * This method checks whether given permission ID already is in a given permission list.
     * @param permissionId
     * @param permissions
     * @return
     */
    public static boolean isIncluded(long permissionId, List<Permission> permissions) {
    	boolean res = false;
    	if (permissions != null && permissions.size() > 0 ) {
   			Iterator<Permission> itr = permissions.iterator();
    		while (itr.hasNext()) {
        		Permission currentPermission = itr.next();
       			if (currentPermission.id == permissionId) {
       				res = true;
       				break;
    			}
    		}
    	}
    	return res;
    }
    
    public static boolean isIncludedByUrl(Long permissionId, String url) {
    	boolean res = false;
//    	Logger.info("isIncludedByUrl() url: " + url);
    	try {
	    	if (StringUtils.isNotEmpty(url)) {
		    	List<Permission> permissions = Role.findByUrl(url).getPermissionsMap();
//		    	Logger.info("permissions.size: "+ permissions.size());
		    	res = isIncluded(permissionId, permissions);
	    	}
		} catch (Exception e) {
			Logger.debug("User is not yet stored in database.");
		}
    	return res;
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

    /**
     * This method updates foreign key mapping between a Permission and a Role.
     */
    public void updateRole() {
        List<Role> roleList = (List<Role>) Role.find.all();
        Iterator<Role> roleItr = roleList.iterator();
        while (roleItr.hasNext()) {
        	Role role = roleItr.next();
//            Logger.info("Test role test object: " + role.toString());
    		if (role.hasPermission(name)) {
    			this.roles.add(role);
    		}    	
        }    	
    }
    
	/**
	 * This method retrieves selected permissions from user object.
	 * @param permissionUrl
	 * @return
	 */
	public static List<Permission> convertUrlsToObjects(String urls) {
		List<Permission> res = new ArrayList<Permission>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
//		    	Logger.info("convertUrlsToObjects part: " + part);
	    		Permission permission = findByName(part);
	    		if (permission != null && permission.name != null && permission.name.length() > 0) {
//			    	Logger.info("add permission to the list: " + permission.name);
	    			res.add(permission);
	    		}
	    	}
    	}
		return res;
	}       
    	 
}