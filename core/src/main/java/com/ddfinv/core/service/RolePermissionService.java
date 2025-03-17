package com.ddfinv.core.service;

import com.ddfinv.core.entity.enums.Role;
import com.ddfinv.core.entity.UserAccount;
import com.ddfinv.core.repository.PermissionRepository;

import java.util.HashSet;
import java.util.Set;

import com.ddfinv.core.entity.Permission;


/**
 * Defines the relationship between each UserAccount role and it's associated permissions
 * as they are used in the core application logic
 * 
 */
 public class RolePermissionService {

    /**
     * 
     * 
     * @param role - the role to retrieve permissions for
     * @param completePermissions - the full list of possible permissions
     * @return - Set<Permission> permissions - the list of permissions for target role
     */
    public Set<Permission> getBasePermissionForRole(Role role, Set<Permission> completePermissions){
        Set<Permission> permissions = new HashSet<>();

        switch (role) {
            case admin:
                return new HashSet<>(completePermissions);

            case employee:
                addPermissionByName(permissions, completePermissions, "CREATE_CLIENT");
                addPermissionByName(permissions, completePermissions, "EDIT_CLIENT");
                addPermissionByName(permissions, completePermissions, "VIEW_CLIENT");
                addPermissionByName(permissions, completePermissions, "VIEW_CLIENTS");
                addPermissionByName(permissions, completePermissions, "ASSIGN_CLIENT");
                addPermissionByName(permissions, completePermissions, "VIEW_INVESTMENT");
                addPermissionByName(permissions, completePermissions, "CREATE_INVESTMENT");
                break;

            case client:
                addPermissionByName(permissions, completePermissions, "VIEW_CLIENT");
                addPermissionByName(permissions, completePermissions, "CREATE_INVESTMENT");
                addPermissionByName(permissions, completePermissions, "CREATE_INVESTMENT");
                break;
            
            case guest:
                addPermissionByName(permissions, completePermissions, "REQUEST_CLIENT_ACCOUNT");
                break;

            default:
                break;

        }
            return permissions;
    }

    /**
     * 
     * 
     * @param targetPermissions - set of permissions constructed for a specific role
     * @param completePermissions - full list of permissions
     * @param nameOfPermission - name of the permission to add to target set for a role
     */
    private void addPermissionByName(Set<Permission> targetPermissions, Set<Permission> completePermissions, String nameOfPermission){
        // create stream from complete list of permissions
        completePermissions.stream()
        .filter(p -> p.getName().equals(nameOfPermission)) // search stream for permission matching nameOfPermission
        .findFirst() // get first match if exists
        .ifPresent(targetPermissions::add); //add to target set if found
    }

}
