package com.zaichko.digitalstore.exception;

public class DuplicateResourceException extends InvalidInputException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
