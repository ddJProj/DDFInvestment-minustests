package com.ddfinv.core.service;
import java.lang.Object;
import org.springframework.security.crypto.bcrypt.BCryptPasswordE
public class AuthenticationService {

    private final PasswordEncoder pwEncoder;
    
    public String hashPassword(String password){
        // TODO: 
        // FIXME: ADD the hash method here for provided password

        String hashedPassword = password + "123";
        String validatedHashedPassword = validateString(hashedPassword);

        return validatedHashedPassword;
    }

    private String validateString(String passwordString){
        try{

        }catch(exception e){

        }

        return validatedString;
    }

    // FIXME: ADD a validation / sanitization step for a provided user password.
}
