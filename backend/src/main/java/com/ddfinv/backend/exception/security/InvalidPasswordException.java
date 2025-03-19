package com.ddfinv.backend.exception.security;

import com.ddfinv.backend.exception.ApplicationException;

public class InvalidPasswordException extends ApplicationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}