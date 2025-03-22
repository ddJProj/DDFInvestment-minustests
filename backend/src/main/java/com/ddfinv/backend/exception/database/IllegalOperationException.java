package com.ddfinv.backend.exception.database;

import com.ddfinv.backend.exception.DatabaseException;


public class IllegalOperationException extends DatabaseException {
    public IllegalOperationException(String message) {
        super(message);
    }
}


