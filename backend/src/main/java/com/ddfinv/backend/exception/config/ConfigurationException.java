package com.ddfinv.backend.exception.config;

import com.ddfinv.backend.exception.ApplicationException;

public class ConfigurationException extends ApplicationException {
    public ConfigurationException(String message) {
        super(message);
    }
}