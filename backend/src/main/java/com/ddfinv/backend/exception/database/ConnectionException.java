package com.ddfinv.backend.exception.database;

import com.ddfinv.backend.exception.DatabaseException;

public class ConnectionException extends DatabaseException {
    public ConnectionException(String message) {
        super(message);
    }
}