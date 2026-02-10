package kz.aitu.digitalcontent.exception;

public class DuplicateResourceException extends InvalidInputException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, String identifier) {
        super(String.format("%s with identifier '%s' already exists", resourceType, identifier));
    }
}