package com.ddfinv.backend.exception.database;

import com.ddfinv.backend.exception.DatabaseException;

public class QueryException extends DatabaseException {
    public QueryException(String message) {
        super(message);
    }
}