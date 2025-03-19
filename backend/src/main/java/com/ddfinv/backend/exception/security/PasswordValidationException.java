package com.ddfinv.backend.exception.security;

import com.ddfinv.backend.exception.ApplicationException;

public class PasswordValidationException extends ApplicationException {
    public PasswordValidationException(String message) {
        super(message);
    }
}