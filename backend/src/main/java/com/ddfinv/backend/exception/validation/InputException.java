package com.ddfinv.backend.exception.validation;

import com.ddfinv.backend.exception.ApplicationException;

public class InputException extends ApplicationException {
    public InputException(String message) {
        super(message);
    }
}