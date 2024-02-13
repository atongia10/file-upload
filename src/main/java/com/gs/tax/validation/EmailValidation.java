package com.gs.tax.validation;

import com.fasterxml.jackson.databind.JsonNode;

public class EmailValidation implements ValidationRule {
    @Override
    public boolean isValid(Object value, JsonNode schema) {
        if (value == null) return false;
        String email = value.toString();
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}
