package com.ddfinv.core.service;

import com.ddfinv.core.entity.UserAccount;
import com.ddfinv.core.entity.enums.Role;

public class EntityPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(UserAccount userAccount, String permissionName, Object resourceObject) {
        if(userAccount.getRole() == Role.admin){
            return true; // admin has all permissions
        }

        if (resourceObject != null){
            // TODO: Implement object specific checks based on rules for app
        }
        
        // basic permission check to validate userAccount passed in as arg
        return userAccount.getPermissions().stream() // creates stream from permissions for this account
        .anyMatch(p->p.getName().equals(permissionName)); // check stream for any permission matching permissionName
    }

    // VERIFY that the USERACCOUNT attempting an action has valid access to required permission
}
