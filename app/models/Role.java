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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;

@Entity
public class Role extends Model
{
    @Id
    public Long id;

    @Column(columnDefinition = "TEXT")
    public String name;

    @Column(columnDefinition = "TEXT")
    public String url;

    @Column(columnDefinition = "TEXT")
    public String permissions;

    @Column(columnDefinition = "TEXT")
    public String description;
    
    @Column(columnDefinition = "TEXT")
    public String revision; 
    
    @Version
    public Timestamp lastUpdate;

    public static final Finder<Long, Role> find = new Finder<Long, Role>(Long.class, Role.class);

    public String getName()
    {
        return name;
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
    	if (permissionName != null && permissionName.length() > 0 
    			&& permissions.contains(permissionName)) {
    		res = true;
    	}
    	return res;
    }
    
    /**
     * This method returns permissions assigned to this role.
     * @return list of Permission objects
     */
    public List<Permission> getPermissions()
    {
    	List<Permission> res = new ArrayList<Permission>();
    	if (permissions != null && permissions.length() > 0) {
			List<String> resList = Arrays.asList(permissions.split(Const.COMMA + " "));
			Iterator<String> itr = resList.iterator();
			while (itr.hasNext()) {
				res.add(Permission.findByName(itr.next()));
			}
    	}
        return res;
    }
    
    /**
     * This method returns permissions that are not assigned to this role.
     * @return list of Permission objects
     */
    public static List<Permission> getNotAssignedPermissions(String permissionsStr)
    {
    	List<Permission> allPermissionList = Permission.findAll();
//    	Logger.info("Permissions count: " + allPermissionList.size());
        List<Permission> res = new ArrayList<Permission>();
    	if (permissionsStr != null && permissionsStr.length() > 0) {
			List<String> assignedList = Arrays.asList(permissionsStr.split(Const.COMMA + " "));
//			Logger.info("original permissions: " + permissionsStr);
//			Logger.info("assignedList: " + assignedList);
			Iterator<Permission> itrAllPermissions = allPermissionList.iterator();
			while (itrAllPermissions.hasNext()) {
				Permission curPermission = itrAllPermissions.next();
//		    	Logger.info("curPermission: " + curPermission.name);
				if (!assignedList.contains(curPermission.name)) {
					res.add(curPermission);
				}
			}
    	}
        return res;
    }
    
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
    public static boolean isIncluded(String roleName, String roles) {
    	boolean res = false;
    	if (roleName != null && roleName.length() > 0 && roles != null && roles.length() > 0 ) {
    		if (roles.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(roles.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			String currentRoleName = itr.next();
        			currentRoleName = currentRoleName.replaceAll(" ", "");
        			if (currentRoleName.equals(roleName)) {
        				res = true;
        				break;
        			}
    			}
    		} else {
    			if (roles.equals(roleName)) {
    				res = true;
    			}
    		}
    	}
    	return res;
    }
    
    public String toString() {
        return "Role(" + name + ")" + ", id:" + id;
    }
}