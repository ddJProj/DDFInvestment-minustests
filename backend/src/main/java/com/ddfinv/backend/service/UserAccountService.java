package com.ddfinv.backend.service;

import com.ddfinv.core.entity.UserAccount;
import com.ddfinv.core.entity.enums.Role;
import com.ddfinv.backend.service.auth.AuthenticationService;

public class UserAccountService {

    AuthenticationService authService;

    public UserAccount createNewUserAccount(String email, String password, String firstName, String lastName){
        UserAccount newAccount = new UserAccount();

        newAccount.setEmail(email);
        newAccount.setHashedPassword(authService.hashPassword(password));
        newAccount.setFirstName(firstName);
        newAccount.setLastName(lastName);
        
        return newAccount;
    }
    public UserAccount modifyUserAccountRole(UserAccount account, Role role){
        account.setRole(role);

        
        
        return account;
    }
}
