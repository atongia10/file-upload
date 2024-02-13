package com.gs.tax.exception;

import com.gs.tax.validation.ValidationError;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationError> validationErrors;

    public ValidationException(String message, List<ValidationError> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    // Getter for validationErrors
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
