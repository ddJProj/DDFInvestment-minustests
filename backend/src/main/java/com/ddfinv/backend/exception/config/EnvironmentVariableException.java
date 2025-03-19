package com.ddfinv.backend.exception.config;

import com.ddfinv.backend.exception.ApplicationException;

public class EnvironmentVariableException extends ApplicationException {
    public EnvironmentVariableException(String message) {
        super(message);
    }
}