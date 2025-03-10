package com.ddfinv.backend.service.auth;
import java.lang.Object;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public class AuthenticationService {

    //private final PasswordEncoder pwEncoder;i
    
    public String hashPassword(String password){
        // TODO: 
        // FIXME: ADD the hash method here for provided password

        String hashedPassword = password + "123";
        String validatedHashedPassword = validateString(hashedPassword);

        return validatedHashedPassword;
    }

    private String validateString(String passwordString){
  /*
        try{

        }catch(exception e){

        }

        return validatedString;
    }
*/
    return passwordString;
    // FIXME: ADD a validation / sanitization step for a provided user password.
}
}