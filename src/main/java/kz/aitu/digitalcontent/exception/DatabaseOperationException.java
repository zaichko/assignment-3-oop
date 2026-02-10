package kz.aitu.digitalcontent.exception;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String operation, Throwable cause) {
        super(String.format("Database operation '%s' failed", operation), cause);
    }
}