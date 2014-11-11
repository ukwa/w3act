/*
* Copyright 2012 Steve Chaloner
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package models;

import java.util.ArrayList;
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

	//bi-directional many-to-many association to Permission
	@JoinTable(name = Const.PERMISSION_ROLE, joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") },
			inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName="id") }) 
	@ManyToMany(mappedBy="roles")
	public List<Permission> permissions = new ArrayList<Permission>();
	
//    //bi-directional many-to-one association to Role
//    @OneToMany(mappedBy="role", cascade=CascadeType.PERSIST)
////    @Column(name="permissionsMap")
//    public List<Permission> permissionsMap = new ArrayList<Permission>();
//
        
    //bi-directional many-to-many association to User
    @ManyToMany
	@JoinTable(name = Const.ROLE_USER, joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id") }) 
    private List<User> users = new ArrayList<User>();
 
	@Required
	@Column(columnDefinition = "text")
    public String name;

//    @JsonIgnore
//    @Column(columnDefinition = "text")
//    public String permissions;

    @JsonIgnore
    @Column(columnDefinition = "text")
    public String description;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 

    public static final Finder<Long, Role> find = new Finder<Long, Role>(Long.class, Role.class);

    public String getName()
    {
        return name;
    }

    public List<Permission> getPermissionsMap() {
    	return this.permissions;
    }
    
    public void setPermissions(List<Permission> permissionsMap) {
    	this.permissions = permissionsMap;
    }
    
    public List<User> getUsers() {
    	return this.users;
    }
    
    public void setUsers(List<User> users) {
    	this.users = users;
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
     * Retrieve a role by URL.
     * @param url
     * @return role name
     */
    public static Role findByUrl(String url) {
//    	Logger.info("role findByUrl: " + url);
    	Role res = new Role();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

	/**
	 * This method filters roles by name and returns a list of filtered Role objects.
	 * @param name
	 * @return
	 */
	public static List<Role> filterByName(String name) {
		List<Role> res = new ArrayList<Role>();
        ExpressionList<Role> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * This method checks if this Role has a permission passed as string parameter.
     * @param permissionName
     * @return true if exists
     */
    public boolean hasPermission(String permissionName) {
    	boolean res = false;
    	if (permissionName != null && permissionName.length() > 0) {
    		Permission permission = Permission.findByName(permissionName);
//        	Logger.info("permission id: " + permission.id);
    		res = hasPermission(permission.id);
    	}
    	return res;
    }
//    public boolean hasPermission(String permissionName) {
//    	boolean res = false;
//    	if (permissionName != null && permissionName.length() > 0 
//    			&& permissions.contains(permissionName)) {
//    		res = true;
//    	}
//    	return res;
//    }
    
    /**
     * This method checks whether user has a permission by its id.
     * @param permissionName
     * @return true if exists
     */
    public boolean hasPermission(long permissionId) {
    	boolean res = false;
//    	Logger.info("hasPermission() permission id: " + permissionId + ", permission_to_user.size(): " + permission_to_user.size());
    	if (permissions != null && permissions.size() > 0) {
    		Iterator<Permission> itr = permissions.iterator();
    		while (itr.hasNext()) {
    			Permission permission = itr.next();
//    	    	Logger.info("hasPermission() permission id: " + permissionId + ", permissionMap id: " + permission.id);
    			if (permission.id == permissionId) {
    				res = true;
    				break;
    			}
    		}
    	}
//    	Logger.info("hasPermission res: " + res);
    	return res;
    }
    
    /**
     * This method returns permissions assigned to this role.
     * @return list of Permission objects
     */
    public List<? extends Permission> getPermissions()
    {
    	return permissions;
    }

//    public List<Permission> getPermissions()
//    {
//    	List<Permission> res = new ArrayList<Permission>();
//    	if (permissions != null && permissions.length() > 0) {
//			List<String> resList = Arrays.asList(permissions.split(Const.COMMA + " "));
//			Iterator<String> itr = resList.iterator();
//			while (itr.hasNext()) {
//				res.add(Permission.findByName(itr.next()));
//			}
//    	}
//        return res;
//    }
    
    /**
     * This method returns permissions that are not assigned to this role.
     * @return list of Permission objects
     */
    public static List<Permission> getNotAssignedPermissions(List<Permission> assignedPermissions)
    {
    	List<Permission> allPermissionList = Permission.findAll();
//    	Logger.info("Permissions count: " + allPermissionList.size());
        List<Permission> res = new ArrayList<Permission>();
    	if (assignedPermissions != null && assignedPermissions.size() > 0) {
			Iterator<Permission> itrAllPermissions = allPermissionList.iterator();
			while (itrAllPermissions.hasNext()) {
				Permission curPermission = itrAllPermissions.next();
//		    	Logger.info("curPermission: " + curPermission.name);
				if (!assignedPermissions.contains(curPermission.name)) {
					res.add(curPermission);
				}
			}
    	}
        return res;
    }
    
//    public static List<Permission> getNotAssignedPermissions(String permissionsStr)
//    {
//    	List<Permission> allPermissionList = Permission.findAll();
////    	Logger.info("Permissions count: " + allPermissionList.size());
//        List<Permission> res = new ArrayList<Permission>();
//    	if (permissionsStr != null && permissionsStr.length() > 0) {
//			List<String> assignedList = Arrays.asList(permissionsStr.split(Const.COMMA + " "));
////			Logger.info("original permissions: " + permissionsStr);
////			Logger.info("assignedList: " + assignedList);
//			Iterator<Permission> itrAllPermissions = allPermissionList.iterator();
//			while (itrAllPermissions.hasNext()) {
//				Permission curPermission = itrAllPermissions.next();
////		    	Logger.info("curPermission: " + curPermission.name);
//				if (!assignedList.contains(curPermission.name)) {
//					res.add(curPermission);
//				}
//			}
//    	}
//        return res;
//    }
//    
    /**
     * Retrieve all roles.
     */
    public static List<Role> findAll() {
        return find.all();
    }

    /**
     * This method checks if a given role is included in the list of passed user roles.
     * Simple "contains" method of string does not help for roles since part of the role name
     * like "exper_user" could be a name of the other role like "user".
     * @param roleName The given role name
     * @param roles The user roles as a string separated by comma
     * @return true if role name is included
     */
    public static boolean isIncluded(long roleId, List<Role> roles) {
    	boolean res = false;
    	if (roles != null && roles.size() > 0 ) {
   			Iterator<Role> itr = roles.iterator();
    		while (itr.hasNext()) {
        		Role currentRole = itr.next();
       			if (currentRole.id == roleId) {
       				res = true;
       				break;
    			}
    		}
    	}
    	return res;
    }
    
    /**
     * This method checks if a given role is included in the list of passed user roles.
     * Simple "contains" method of string does not help for roles since part of the role name
     * like "exper_user" could be a name of the other role like "user".
     * @param roleName The given role name
     * @param roles The user roles as a string separated by comma
     * @return true if role name is included
     */
    public static boolean isIncludedByUrl(Long roleId, String url) {
    	boolean res = false;
//    	Logger.info("isIncludedByUrl() roleId: " + roleId + ",url: " + url);
    	try {
	    	if (StringUtils.isNotEmpty(url)) {
		    	List<Role> roles = User.findByUrl(url).roles;
//		    	Logger.info("roles.size: "+ roles.size());
		    	res = isIncluded(roleId, roles);
	    	}
    	} catch (Exception e) {
    		Logger.debug("User is not yet stored in database.");
    	}
    	return res;
    }
    
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
    			Logger.info("New created role is allowed.");
    			res = true;
    		}
    	}
//    	Logger.debug("role allowance check: " + role + ", user: " + user + ", res: " + res);
    	return res;
    }
    
	/**
	 * This method retrieves selected roles from user object.
	 * @param userUrl
	 * @return
	 */
	public static List<Role> convertUrlsToObjects(String urls) {
		List<Role> res = new ArrayList<Role>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
//		    	Logger.info("convertUrlsToObjects part: " + part);
	    		Role role = findByName(part);
	    		if (role != null && role.name != null && role.name.length() > 0) {
//			    	Logger.info("add role to the list: " + role.name);
	    			res.add(role);
	    		}
	    	}
    	}
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
	public static List<Role> setDefaultRoleByName(String roleName) {
		List<Role> res = new ArrayList<Role>();
   		Role role = findByName(roleName);
		if (role != null && role.name != null && role.name.length() > 0) {
			res.add(role);
		}
		return res;
	}       
 
   public String toString() {
        return "Role(" + name + ")" + ", id:" + id;
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

}