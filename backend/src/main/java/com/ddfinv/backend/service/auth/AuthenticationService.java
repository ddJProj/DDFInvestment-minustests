package com.ddfinv.backend.service.auth;

import java.lang.Object;
import java.util.regex.Pattern;


// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCrypt.html
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.exception.InvalidPasswordException;

public class AuthenticationService {

    private final PasswordEncoder pwEncoder;
    // https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    private static final Pattern VALID_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    public AuthenticationService(){
        this.pwEncoder = new BCryptPasswordEncoder();
    }
    /**
     * 
     * @param password
     * @return
     */
    public String hashPassword(String password){
        // TODO: 

        String validatedPassword = validateString(password);

        return pwEncoder.encode(validatedPassword);
    }

    /**
     * 
     * @param passwordString
     * @return
     */
    private String validateString(String passwordString){
        if(passwordString == null){
            throw new InvalidPasswordException("Password entry must not be blank!");
        }
        if (passwordString.length() < 8){
            throw new InvalidPasswordException("Password must be greater than 8 characters in length.");
        }
        if (!VALID_PATTERN.matcher(passwordString).matches()){
            throw new InvalidPasswordException("Password must contain no spaces, at least one number, one special character, one uppercase, and one lowercase letter.");
        }

        return passwordString;
    }

    /**
     * 
     * @param password
     * @param hashedPassword
     * @return
     */
    public boolean authenticatePassword(String password, String hashedPassword){
        return pwEncoder.matches(password, hashedPassword);
    }
}
//TODO : finish documentation