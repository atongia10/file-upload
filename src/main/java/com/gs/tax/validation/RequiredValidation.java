package com.gs.tax.validation;

import com.fasterxml.jackson.databind.JsonNode;

public class RequiredValidation implements ValidationRule {
    @Override
    public boolean isValid(Object value, JsonNode schema) {
        return value != null && !value.toString().isEmpty();
    }
}
