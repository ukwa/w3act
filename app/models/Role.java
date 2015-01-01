package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.validation.ValidationError;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class Role extends ActModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5670206529564297517L;

	@ManyToMany
	@JoinTable(name = "permission_role", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") },
	inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName="id") }) 
	public List<Permission> permissions = new ArrayList<>();
	
    @ManyToMany
	@JoinTable(name = "role_user", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id") }) 
    public List<User> users;
 
	@Column(columnDefinition = "text")
    public String name;

    @JsonIgnore
    @Column(columnDefinition = "text")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 

    public static final Finder<Long, Role> find = new Finder<Long, Role>(Long.class, Role.class);

    
    public List<ValidationError> validate() {

        List<ValidationError> errors = new ArrayList<>();

        if (name == null || name.length() == 0) {
        	errors.add(new ValidationError("name", "No name was given."));
        }

        if (errors.size() > 0)
        	return errors;

        return null;
    }
    
    /**
     * Retrieve all roles.
     */
    public static List<Role> findAll() {
        return find.all();
    }

    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static Role findById(Long id) {
    	Role res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    public static Role findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
	/**
	 * This method filters roles by name and returns a list of filtered Role objects.
	 * @param name
	 * @return
	 */
	public static List<Role> filterByName(String name) {
		List<Role> res = new ArrayList<Role>();
        ExpressionList<Role> ll = find.where().icontains("name", name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * This method checks if this Role has a permission passed as string parameter.
     * @param permissionName
     * @return true if exists
     */
    public boolean hasPermission(String permissionName) {
    	if (StringUtils.isNotEmpty(permissionName)) {
    		Permission permission = Permission.findByName(permissionName);
    		if (this.permissions.contains(permission)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * This method checks whether user has a permission by its id.
     * @param permissionName
     * @return true if exists
     */
    public boolean hasPermission(Long permissionId) {
    	if (permissionId != null) {
    		Permission permission = Permission.findById(permissionId);
    		if (this.permissions.contains(permission)) {
    			return true;
    		}
    	}
    	return false;
    }

//    public static List<Permission> getNotAssignedPermissions(List<Permission> assignedPermissions) {
//    	return Permission.find.where().not(Expr.in("permissions", assignedPermissions)).findList();
//    }
    

    /**
     * This method returns permissions that are not assigned to this role.
     * @return list of Permission objects
     */
    public static List<Permission> getNotAssignedPermissions(List<Permission> assignedPermissions) {
    	List<Permission> allPermissionList = Permission.findAll();
//    	Logger.debug("Permissions count: " + allPermissionList.size());
        List<Permission> res = new ArrayList<Permission>();
    	if (assignedPermissions != null && assignedPermissions.size() > 0) {
			Iterator<Permission> itrAllPermissions = allPermissionList.iterator();
			while (itrAllPermissions.hasNext()) {
				Permission curPermission = itrAllPermissions.next();
//		    	Logger.debug("curPermission: " + curPermission.name);
				if (!assignedPermissions.contains(curPermission)) {
					res.add(curPermission);
				}
			}
    	}
        return res;
    }
//    
//    public static List<Permission> getNotAssignedPermissions(String permissionsStr) {
//    	List<Permission> allPermissionList = Permission.findAll();
////    	Logger.debug("Permissions count: " + allPermissionList.size());
//        List<Permission> res = new ArrayList<Permission>();
//    	if (permissionsStr != null && permissionsStr.length() > 0) {
//			List<String> assignedList = Arrays.asList(permissionsStr.split(Const.COMMA + " "));
////			Logger.debug("original permissions: " + permissionsStr);
////			Logger.debug("assignedList: " + assignedList);
//			Iterator<Permission> itrAllPermissions = allPermissionList.iterator();
//			while (itrAllPermissions.hasNext()) {
//				Permission curPermission = itrAllPermissions.next();
////		    	Logger.debug("curPermission: " + curPermission.name);
//				if (!assignedList.contains(curPermission.name)) {
//					res.add(curPermission);
//				}
//			}
//    	}
//        return res;
//    }
    

//    /**
//     * This method checks if a given role is included in the list of passed user roles.
//     * Simple "contains" method of string does not help for roles since part of the role name
//     * like "exper_user" could be a name of the other role like "user".
//     * @param roleName The given role name
//     * @param roles The user roles as a string separated by comma
//     * @return true if role name is included
//     */
//    public static boolean isIncludedByUrl(Long roleId, String url) {
//    	boolean res = false;
////    	Logger.debug("isIncludedByUrl() roleId: " + roleId + ",url: " + url);
//    	try {
//	    	if (StringUtils.isNotEmpty(url)) {
//		    	List<Role> roles = User.findByUrl(url).roles;
////		    	Logger.debug("roles.size: "+ roles.size());
//		    	res = isIncluded(roleId, roles);
//	    	}
//    	} catch (Exception e) {
//    		Logger.debug("User is not yet stored in database.");
//    	}
//    	return res;
//    }
    
    /**
     * This method evaluates index of the role in the role enumeration.
     * @param roles
     * @return
     */
    public static int getRoleSeverity(List<Role> roles) {
    	int res = Const.Roles.values().length;
    	if (roles != null && roles.size() > 0 ) {
   			Iterator<Role> itr = roles.iterator();
    		while (itr.hasNext()) {
        		Role currentRole = itr.next();
   				int currentLevel = Const.Roles.valueOf(currentRole.name).ordinal();
   				if (currentLevel < res) {
   					res = currentLevel;
    			}
    		}
    	}
    	return res;
    }
    
    /**
     * This method validates whether user is allowed to
     * change given role.
     * @param role
     * @param user
     * @return true if user is allowed
     */
    public static boolean isAllowed(Role role, User user) {
    	boolean res = false;
    	if (role != null && role.name != null && role.name.length() > 0) {
    		try {
	    		int roleIndex = Const.Roles.valueOf(role.name).ordinal();
	    		int userIndex = getRoleSeverity(user.roles);
//	    		Logger.debug("roleIndex: " + roleIndex + ", userIndex: " + userIndex);
	    		if (roleIndex >= userIndex) {
	    			res = true;
	    		}  
    		} catch (Exception e) {
    			Logger.debug("New created role is allowed.");
    			res = true;
    		}
    	}
//    	Logger.debug("role allowance check: " + role + ", user: " + user + ", res: " + res);
    	return res;
    }
    
	/**
	 * This method initializes User object by the default Role.
	 * @param userUrl
	 * @return
	 */
	public static List<Role> setDefaultRole() {
		List<Role> res = new ArrayList<Role>();
   		Role role = findByName(Const.DEFAULT_ROLE);
		if (role != null && role.name != null && role.name.length() > 0) {
			res.add(role);
		}
		return res;
	}       
    	
	/**
	 * This method initializes User object by the default Role by the role name.
	 * @param userUrl
	 * @return
	 */
	public static List<Role> setRoleByName(String roleName) {
		List<Role> res = new ArrayList<Role>();
   		Role role = findByName(roleName);
		if (role != null && role.name != null && role.name.length() > 0) {
			res.add(role);
		}
		return res;
	}       
 
    /**
     * Return a page of User
     *
     * @param page Page to display
     * @param pageSize Number of Roles per page
     * @param sortBy User property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Role> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

	public String permissionsAsString() {
//		Logger.debug("permissionsAsString");
		List<String> names = new ArrayList<String>();
		for (Permission permission : this.permissions) {
			names.add(permission.name);
		}
		return StringUtils.join(names, ", ");
	}
    
	public static Map<String, String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Role s : Role.findAll()) {
            options.put(s.id.toString(), s.name);
        }
        return options;
		
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
		Role other = (Role) obj;
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
		return "Role [permissions=" + permissions + ", users=" + users
				+ ", name=" + name + ", description=" + description
				+ ", revision=" + revision + "]";
	}
}