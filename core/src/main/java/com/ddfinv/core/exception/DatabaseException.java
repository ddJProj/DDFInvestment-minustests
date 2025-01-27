package com.ddfinv.core.exception;

// DatabaseException.java
public class DatabaseException extends ApplicationException {
    public static class ConnectionException extends DatabaseException {
        public ConnectionException(String message) {
            super(message);
        }
    }

    public static class QueryException extends DatabaseException {
        public QueryException(String message) {
            super(message);
        }
    }

    public static class TransactionException extends DatabaseException {
        public TransactionException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends DatabaseException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
