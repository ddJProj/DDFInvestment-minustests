package com.ddfinv.backend.service;

import java.net.Authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ddfinv.core.entity.UserAccount;
import com.ddfinv.core.repository.UserAccountRepository;
import com.ddfinv.core.service.PermissionEvaluator;

public class PermissionHandlerService {

    private final PermissionEvaluator permissionEvaluator;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public PermissionHandlerService(PermissionEvaluator permissionEvaluator, UserAccountRepository userAccountRepository){
        this.permissionEvaluator = permissionEvaluator;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * 
     * @param permissionName
     * @return
     */
    public boolean thisUserHasPermission(String permissionName){
        
        return thisUserHasPermission(permissionName, null);
    }

    /**
     * 
     * @param permissionName
     * @param resource
     * @return
     */
    public boolean thisUserHasPermission(String permissionName, Object resource){
        UserAccount thisUser = getThisUser();
        if (thisUser == null){
            return false;
        }
        return permissionEvaluator.hasPermission(thisUser, permissionName, resource);
    }

    /**
     * 
     * @return
     */
    public UserAccount getThisUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            return null;
        }
        String loginName = authentication.getName();
        return userAccountRepository.findByEmail(loginName).orElse(null);
    }

    /**
     * 
     * @param userAccountId
     * @param permissionName
     * @return
     */
    public boolean thisUserHasPermission(Long userAccountId, String permissionName){
        return thisUserHasPermission(userAccountId, permissionName, null);
    }

    /**
     * 
     * @param userAccountId
     * @param permissionName
     * @param resource
     * @return
     */
    public boolean thisUserHasPermission(Long userAccountId, String permissionName, Object resource){
        UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);
        if (userAccount == null){
            return false;
        }
        return permissionEvaluator.hasPermission(userAccount, permissionName, resource);
    }

}
