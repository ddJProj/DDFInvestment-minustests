package com.ddfinv.backend.exception;

/**
 * basic custom exception for invalid password
 */
public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String message){
        super(message);
    }
}
