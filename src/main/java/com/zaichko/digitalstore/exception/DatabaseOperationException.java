package com.zaichko.digitalstore.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable e) {
        super(message, e);
    }
}