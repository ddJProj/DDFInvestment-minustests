package com.ddfinv.backend.service.auth;

import java.lang.Object;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCrypt.html
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.UserAccountDTO;
import com.ddfinv.backend.dto.auth.AuthenticationRequest;
import com.ddfinv.backend.dto.auth.AuthenticationResponse;
import com.ddfinv.backend.dto.auth.RegisterAuthRequest;
import com.ddfinv.backend.exception.security.InvalidPasswordException;
import com.ddfinv.backend.exception.validation.EmailValidationException;
import com.ddfinv.backend.service.UserDetailService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;


/*
 * 
 * Source(s) : 
 * https://medium.com/@th.chousiadas/spring-security-architecture-of-jwt-authentication-a7967a8ee309
 * 
 */
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    
    
    private final PasswordEncoder pwEncoder;
    // regex pattern for input found at :
    // https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    private static final Pattern VALID_PATTERN =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");



    public AuthenticationService(PasswordEncoder pwEncoder, UserAccountRepository userAccountRepository, UserAccountService userAccountService, JwtService jwtService, AuthenticationManager authenticationManager, UserDetailService userDetailService){
        this.userAccountRepository = userAccountRepository;
        this.userAccountService = userAccountService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        
        this.pwEncoder = pwEncoder;
    }


    /**
     *
     * @param passwordString
     * @return
     */
    private String validateString(String passwordString) throws InvalidPasswordException {
        if(passwordString == null){
            throw new InvalidPasswordException
                ("Password entry must not be blank!");
        }
        if (passwordString.length() < 8){
            throw new InvalidPasswordException
                ("Password must be greater than 8 characters in length.");
        }
        if (!VALID_PATTERN.matcher(passwordString).matches()){
            throw new InvalidPasswordException
                ("Password must contain no spaces, at least one number, one special character, one uppercase, and one lowercase letter.");
        }

        return passwordString;
    }


    /**
     *
     * @param request
     * @return
     * @throws EmailValidationException
     * @throws InvalidPasswordException
     */
    public AuthenticationResponse register(RegisterAuthRequest request)
            throws EmailValidationException, InvalidPasswordException {
        
        if (userAccountRepository.existsByEmail(request.getEmail())){
            throw new EmailValidationException("The email provided is linked to an existing account.");
        }
        
        validateString(request.getPassword());
        UserAccountDTO userAccountDTO = new UserAccountDTO();

        userAccountDTO.setFirstName(request.getFirstName());
        userAccountDTO.setLastName(request.getLastName());
        userAccountDTO.setEmail(request.getEmail());
        userAccountDTO.setPassword(request.getPassword());
        userAccountDTO.setRole(Role.guest);

        UserAccountDTO newUser = userAccountService.createUserAcount(userAccountDTO);

        UserDetails userDetails = userDetailService.loadUserByUsername(newUser.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);

        // return the contructed token
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    /**
     *
     * @param request
     * @return
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());
        String jwtTaken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
        .token(jwtTaken)
        .build();

    }



}
//TODO : finish documentation