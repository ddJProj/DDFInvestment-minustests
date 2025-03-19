package com.ddfinv.backend.exception.database;

import com.ddfinv.backend.exception.DatabaseException;

public class TransactionException extends DatabaseException {
    public TransactionException(String message) {
        super(message);
    }
}