package kz.aitu.digitalcontent.model;

public interface Validatable {

    void validate();

    default String getValidationMessage(String fieldName, String issue) {
        return String.format("Validation Error [%s]: %s", fieldName, issue);
    }

    static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    static boolean isPositive(double value) {
        return value > 0;
    }
}