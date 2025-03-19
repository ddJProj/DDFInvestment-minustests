package com.ddfinv.backend.exception.security;

import com.ddfinv.backend.exception.ApplicationException;

public class PasswordHashException extends ApplicationException {
    public PasswordHashException(String message) {
        super(message);
    }
}