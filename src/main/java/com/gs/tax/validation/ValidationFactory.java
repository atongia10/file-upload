package com.gs.tax.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class ValidationFactory {
    public static List<ValidationRule> getValidations(JsonNode validations) {
        List<ValidationRule> validationRules = new ArrayList<>();

        // Handle both single validation rule and an array of rules
        if (validations.isTextual()) {
            // Single validation rule
            addValidationRule(validationRules, validations.asText());
        } else if (validations.isArray()) {
            // Array of validation rules
            for (JsonNode validationType : validations) {
                addValidationRule(validationRules, validationType.asText());
            }
        }

        return validationRules;
    }

    private static void addValidationRule(List<ValidationRule> validationRules, String validationType) {
        switch (validationType.toLowerCase()) {
            case "required":
                validationRules.add(new RequiredValidation());
                break;
            case "email":
                validationRules.add(new EmailValidation());
                break;
            // Add cases for other validation types
            default:
                // Optionally handle unknown validation types
                break;
        }
    }
}
