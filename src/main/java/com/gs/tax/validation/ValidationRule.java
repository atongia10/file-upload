package com.gs.tax.validation;

import com.fasterxml.jackson.databind.JsonNode;

public interface ValidationRule {
    boolean isValid(Object value, JsonNode schema);
}
