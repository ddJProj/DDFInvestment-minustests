package com.ddfinv.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.auth.AuthenticationRequest;
import com.ddfinv.backend.dto.auth.AuthenticationResponse;
import com.ddfinv.backend.dto.auth.RegisterAuthRequest;
import com.ddfinv.backend.exception.ApplicationException;
import com.ddfinv.backend.exception.validation.EmailValidationException;
import com.ddfinv.backend.service.auth.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    /**
     * 
     * @param request
     * @return
     * @throws EmailValidationException 
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterAuthRequest request) throws ApplicationException {
        return ResponseEntity.ok(authenticationService.register(request));
        
    }

    /**
     * 
     * @param request
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
        
    }
    
}
