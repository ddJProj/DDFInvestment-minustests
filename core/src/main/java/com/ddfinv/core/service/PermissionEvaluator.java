package com.ddfinv.core.service;

import com.ddfinv.core.entity.UserAccount;

public interface PermissionEvaluator {
    // to check if the required permission to work with a resource is held by a UserAccount
    boolean hasPermission(UserAccount userAccount, String permissionName, Object resourceObject);
}
