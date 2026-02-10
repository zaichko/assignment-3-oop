package kz.aitu.digitalcontent.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, int id) {
        super(String.format("%s with ID %d not found", resourceType, id));
    }
}