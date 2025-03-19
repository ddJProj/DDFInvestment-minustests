package com.ddfinv.core.service;


import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;

public class EntityPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(UserAccount userAccount, Permissions permissionType, Object resourceObject) {
        if(userAccount.getRole() == Role.admin){
            return true; // admin has all permissions
        }

        if (resourceObject != null){
            // TODO: Implement object specific checks based on rules for app
        }
        
        // basic permission check to validate userAccount passed in as arg
        return userAccount.getPermissions().stream() // creates stream from permissions for this account
        .anyMatch(p->p.getPermissionType() == permissionType); // check stream for any permission matching permissionName
    }

    // VERIFY that the USERACCOUNT attempting an action has valid access to required permission
}
