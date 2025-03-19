package com.ddfinv.backend.exception.database;

import com.ddfinv.backend.exception.DatabaseException;

public class EntityNotFoundException extends DatabaseException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}