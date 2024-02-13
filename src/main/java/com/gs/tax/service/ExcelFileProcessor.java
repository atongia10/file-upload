package com.gs.tax.service;

import com.gs.tax.exception.ValidationException;
import com.gs.tax.validation.ValidationError;
import com.gs.tax.validation.ValidationFactory;
import com.gs.tax.validation.ValidationRule;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelFileProcessor implements FileProcessor {

    @Override
    public List<Map<String, Object>> processFile(MultipartFile file, JsonNode schema) throws Exception {
        List<Map<String, Object>> processedData = new ArrayList<>();
        List<ValidationError> validationErrors = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                List<ValidationError> rowErrors = processRow(row, schema, row.getRowNum() + 1);
                if (rowErrors.isEmpty()) {
                    Map<String, Object> rowData = extractRowData(row, schema);
                    processedData.add(rowData);
                } else {
                    validationErrors.addAll(rowErrors);
                }
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation errors occurred while processing Excel file", validationErrors);
        }

        return processedData;
    }

    private Map<String, Object> extractRowData(Row row, JsonNode schema) {
        Map<String, Object> rowData = new HashMap<>();
        for (int i = 0; i < schema.size(); i++) {
            JsonNode columnDefinition = schema.get(i);
            String columnName = columnDefinition.get("name").asText();
            int columnIndex = columnLetterToIndex(columnDefinition.get("excelColumn").asText());
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String type = columnDefinition.get("type").asText();
            Object value = convertCellValue(cell, type);
            rowData.put(columnName, value);
        }
        return rowData;
    }

    private List<ValidationError> processRow(Row row, JsonNode schema, int rowNumber) {
        List<ValidationError> rowErrors = new ArrayList<>();

        for (int i = 0; i < schema.size(); i++) {
            JsonNode columnDefinition = schema.get(i);
            String outputColumnName = columnDefinition.get("name").asText();
            int columnIndex = columnLetterToIndex(columnDefinition.get("excelColumn").asText());
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String type = columnDefinition.get("type").asText();
            Object value = convertCellValue(cell, type);

            JsonNode validationNode = columnDefinition.path("validation");
            List<ValidationRule> validations = ValidationFactory.getValidations(validationNode);
            for (ValidationRule validation : validations) {
                if (!validation.isValid(value, columnDefinition)) {
                    rowErrors.add(new ValidationError(rowNumber, outputColumnName, "Invalid value for " + outputColumnName));
                }
            }
        }

        return rowErrors;
    }

    private int columnLetterToIndex(String columnLetter) {
        // Apache POI utility to convert Excel column letter to 0-based column index
        return CellReference.convertColStringToIndex(columnLetter);
    }

    private Object convertCellValue(Cell cell, String type) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null; // Return null for blank cells
        }

        DataFormatter formatter = new DataFormatter();
        CellType cellType = cell.getCellType();

        switch (type.toUpperCase()) {
            case "STRING":
                return formatter.formatCellValue(cell); // Use DataFormatter to handle cell as a String

            case "INTEGER":
                try {
                    return cellType == CellType.NUMERIC ? (int) cell.getNumericCellValue() : Integer.parseInt(formatter.formatCellValue(cell));
                } catch (NumberFormatException e) {
                    return null; // Return null if the cell value cannot be parsed as Integer
                }

            case "BOOLEAN":
                return cellType == CellType.BOOLEAN ? cell.getBooleanCellValue() : Boolean.parseBoolean(formatter.formatCellValue(cell));

            case "DOUBLE":
                try {
                    return cellType == CellType.NUMERIC ? cell.getNumericCellValue() : Double.parseDouble(formatter.formatCellValue(cell));
                } catch (NumberFormatException e) {
                    return null; // Return null if the cell value cannot be parsed as Double
                }

            case "FLOAT":
                try {
                    return cellType == CellType.NUMERIC ? (float) cell.getNumericCellValue() : Float.parseFloat(formatter.formatCellValue(cell));
                } catch (NumberFormatException e) {
                    return null; // Return null if the cell value cannot be parsed as Float
                }

            case "DATE":
                if (cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    try {
                        return parseDateFromString(formatter.formatCellValue(cell)); // Your method to parse date from String
                    } catch (Exception e) {
                        return null; // Return null or handle the exception if date cannot be parsed
                    }
                }

            case "DATETIMESTAMP":
                if (cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    return convertExcelDateToLocalDateTime(cell.getNumericCellValue()); // Your method to convert Excel date to LocalDateTime
                } else {
                    try {
                        // Optionally, parse string to LocalDateTime here or throw an exception
                        return null; // Placeholder for string to LocalDateTime conversion
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Unsupported or non-numeric date format for DATETIMESTAMP type");
                    }
                }

            default:
                return formatter.formatCellValue(cell);
        }
    }


    private Date parseDateFromString(String dateStr) {
        // Implement parsing logic based on expected date formats or use a library like DateUtils from Apache Commons Lang
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy"); // Adjust this to match your expected format
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateStr, e);
        }
    }
    private LocalDateTime convertExcelDateToLocalDateTime(double excelDate) {
        // Convert Excel date (numeric value) to Java LocalDateTime
        LocalDate date = DateUtil.getJavaDate(excelDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) excelDate;
        double timeFraction = excelDate - days;
        LocalTime time = LocalTime.ofSecondOfDay((long) (timeFraction * 86400));
        return LocalDateTime.of(date, time);
    }
}

