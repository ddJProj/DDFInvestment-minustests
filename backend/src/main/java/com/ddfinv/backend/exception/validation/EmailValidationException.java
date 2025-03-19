package com.ddfinv.backend.exception.validation;

import com.ddfinv.backend.exception.ApplicationException;

public class EmailValidationException extends ApplicationException {
    public EmailValidationException(String message) {
        super(message);
    }
}