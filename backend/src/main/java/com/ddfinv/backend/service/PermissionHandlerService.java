package com.ddfinv.backend.service;

import java.net.Authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;
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
     * @param permissionType
     * @return
     */
    public boolean thisUserHasPermission(Long userAccountId, Permissions permissionType){
        return thisUserHasPermission(userAccountId, permissionType, null);
    }

    /**
     * 
     * @param userAccountId
     * @param permissionType
     * @param resource
     * @return
     */
    public boolean thisUserHasPermission(Long userAccountId, Permissions permissionType, Object resource){
        UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);
        if (userAccount == null){
            return false;
        }
        return permissionEvaluator.hasPermission(userAccount, permissionType, resource);
    }

    /**
     * 
     * @param permissionType
     * @return
     */
    public boolean thisUserHasPermission(Permissions permissionType){
        
        return thisUserHasPermission(permissionType, null);
    }

    /**
     * 
     * @param permissionType
     * @param resource
     * @return
     */
    public boolean thisUserHasPermission(Permissions permissionType, Object resource){
        UserAccount thisUser = getThisUser();
        if (thisUser == null){
            return false;
        }
        return permissionEvaluator.hasPermission(thisUser, permissionType, resource);
    }



    public boolean currentUserHasPermission(Permissions permissionType) {
        return thisUserHasPermission(permissionType);
        
    }

}
