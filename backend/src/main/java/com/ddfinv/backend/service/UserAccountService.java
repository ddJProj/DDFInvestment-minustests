package com.ddfinv.backend.service;

import com.ddfinv.core.entity.Permission;
import com.ddfinv.core.entity.UserAccount;
import com.ddfinv.core.entity.enums.Role;
import com.ddfinv.core.repository.PermissionRepository;
import com.ddfinv.core.repository.UserAccountRepository;
import com.ddfinv.core.service.RolePermissionService;

import jakarta.transaction.Transactional;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ddfinv.backend.service.auth.AuthenticationService;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionService rolePermissionService;
    private final AuthenticationService authService;

    @Autowired
    public UserAccountService (UserAccountRepository userAccountRepository, PermissionRepository permissionRepository, AuthenticationService authService){
        this.userAccountRepository = userAccountRepository;
        this.permissionRepository = permissionRepository;
        this.authService = authService;
        this.rolePermissionService = new RolePermissionService();
    }

    
    @Transactional
    public UserAccount createNewUserAccount(String email, String password, String firstName, String lastName){
        UserAccount newAccount = new UserAccount();

        newAccount.setEmail(email);
        newAccount.setHashedPassword(authService.hashPassword(password));
        newAccount.setFirstName(firstName);
        newAccount.setLastName(lastName);
        
        userAccountRepository.save(newAccount);
        assignUserRole(newAccount, Role.guest);

        return newAccount;
    }

    @Transactional
    public void assignUserRole(UserAccount userAccount, Role role){
        userAccount.setRole(role);

        // get the full possible list of permissions
        Set<Permission> completePermissions = Set.copyOf(permissionRepository.findAll());
        
        // get the default set of permissions for the specific role
        Set<Permission> basePermisions = rolePermissionService.getBasePermissionForRole(role, completePermissions);

        // set the list of permissions for the account to base for it's role
        userAccount.setPermissions(basePermisions);
    }

    @Transactional
    public UserAccount modifyUserAccountRole(UserAccount userAccount, Role role){
        assignUserRole(userAccount, role);
        return userAccount;
    }

    public UserAccount findByEmail(String email){
        return userAccountRepository.findByEmail(email)
            .orElse(null);
    }

    public UserAccount saveUserAccount(UserAccount userAccount){
        return userAccountRepository.save(userAccount);
    }
}
