package com.ddfinv.core.service;

import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;

public interface PermissionEvaluator {

    /**
     *  check if the required permission to work with a resource is held by a UserAccount
     *  
     * @param userAccount
     * @param permissionName
     * @param resourceObject
     * @return boolean true if permission is held, else false
     */
    boolean hasPermission(UserAccount userAccount, Permissions permissionType, Object resourceObject);
}
