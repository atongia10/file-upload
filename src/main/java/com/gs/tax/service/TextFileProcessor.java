package com.gs.tax.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.gs.tax.exception.ValidationException;
import com.gs.tax.validation.ValidationError;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TextFileProcessor implements FileProcessor {

    private static final String DELIMITER = "\\|"; // Pipe delimiter in regex
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public List<Map<String, Object>> processFile(MultipartFile file, JsonNode schema) throws Exception {
        List<Map<String, Object>> processedData = new ArrayList<>();
        List<ValidationError> validationErrors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine(); // Read the first line as header
            if (headerLine == null) {
                throw new IllegalArgumentException("The file is empty.");
            }

            int rowNumber = 2; // Start from the second line for data rows
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(DELIMITER, -1); // Split line into values
                Map<String, Object> rowData = new HashMap<>();
                List<ValidationError> rowErrors = new ArrayList<>();

                for (int i = 0; i < schema.size(); i++) {
                    JsonNode columnDefinition = schema.get(i);
                    String outputColumnName = columnDefinition.get("name").asText();
                    String type = columnDefinition.get("type").asText();
                    Object value = i < values.length ? convertValue(values[i], type, rowErrors, rowNumber, outputColumnName) : null; // Handle missing values

                    rowData.put(outputColumnName, value);
                }

                if (rowErrors.isEmpty()) {
                    processedData.add(rowData);
                } else {
                    validationErrors.addAll(rowErrors);
                }
                rowNumber++;
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation errors occurred while processing Text file", validationErrors);
        }

        return processedData;
    }

    private Object convertValue(String value, String type, List<ValidationError> rowErrors, int rowNumber, String columnName) {
        try {
            switch (type.toUpperCase()) {
                case "STRING":
                    return value;
                case "INTEGER":
                    return Integer.parseInt(value.trim());
                case "BOOLEAN":
                    return Boolean.parseBoolean(value.trim());
                case "DATE":
                    return DATE_FORMAT.parse(value.trim());
                default:
                    throw new IllegalArgumentException("Unsupported type: " + type);
            }
        } catch (ParseException e) {
            rowErrors.add(new ValidationError(rowNumber, columnName, "Date format is not valid"));
            return null;
        } catch (NumberFormatException e) {
            rowErrors.add(new ValidationError(rowNumber, columnName, "Number format is not valid"));
            return null;
        } catch (IllegalArgumentException e) {
            rowErrors.add(new ValidationError(rowNumber, columnName, "Type is not supported"));
            return null;
        }
    }
}


