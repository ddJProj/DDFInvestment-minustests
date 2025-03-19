package com.ddfinv.core.service;

import com.ddfinv.core.domain.Permission;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.PermissionRepository;

import java.util.HashSet;
import java.util.Set;


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
                addPermission(permissions, completePermissions, Permissions.VIEW_ACCOUNT);
                addPermission(permissions, completePermissions, Permissions.EDIT_MY_DETAILS);
                addPermission(permissions, completePermissions, Permissions.CREATE_USER);

                addPermission(permissions, completePermissions, Permissions.CREATE_CLIENT);
                addPermission(permissions, completePermissions, Permissions.EDIT_CLIENT);
                addPermission(permissions, completePermissions, Permissions.VIEW_CLIENT);
                addPermission(permissions, completePermissions, Permissions.VIEW_CLIENTS);
                addPermission(permissions, completePermissions, Permissions.ASSIGN_CLIENT);
                addPermission(permissions, completePermissions, Permissions.CREATE_INVESTMENT);
                addPermission(permissions, completePermissions, Permissions.EDIT_INVESTMENT);
                break;

            case client:
                addPermission(permissions, completePermissions, Permissions.VIEW_ACCOUNT);
                addPermission(permissions, completePermissions, Permissions.EDIT_MY_DETAILS);
                addPermission(permissions, completePermissions, Permissions.CREATE_USER);
                addPermission(permissions, completePermissions, Permissions.VIEW_INVESTMENT);
                break;

            case guest:
                addPermission(permissions, completePermissions, Permissions.VIEW_ACCOUNT);
                addPermission(permissions, completePermissions, Permissions.EDIT_MY_DETAILS);
                addPermission(permissions, completePermissions, Permissions.CREATE_USER);
                addPermission(permissions, completePermissions, Permissions.REQUEST_CLIENT_ACCOUNT);
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
    private void addPermission(Set<Permission> targetPermissions, Set<Permission> completePermissions, Permissions permissionType){
        // create stream from complete list of permissions
        completePermissions.stream()
        .filter(p -> p.getPermissionType() == permissionType) // search stream for permission matching nameOfPermission
        .findFirst() // get first match if exists
        .ifPresent(targetPermissions::add); //add to target set if found
    }

}
