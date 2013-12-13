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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;

@Entity
public class Role extends Model
{
    @Id
    public Long id;

    public String name;

    @Column(columnDefinition = "TEXT")
    public String permissions;
    
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
    
    public List<? extends Permission> getPermissions()
    {
    	List<Permission> res = new ArrayList<Permission>();
		List<String> resList = Arrays.asList(permissions.split(Const.COMMA));
		Iterator<String> itr = resList.iterator();
		while (itr.hasNext()) {
			res.add(Permission.findByName(itr.next()));
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