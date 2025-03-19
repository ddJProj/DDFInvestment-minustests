package com.ddfinv.backend.exception.security;

import com.ddfinv.backend.exception.ApplicationException;

public class AuthenticationException extends ApplicationException {
    public AuthenticationException(String message) {
        super(message);
    }
}