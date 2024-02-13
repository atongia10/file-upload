package com.gs.tax.validation;

public class ValidationError {
    private final int rowNumber;
    private final String columnName;
    private final String message;

    public ValidationError(int rowNumber, String columnName, String message) {
        this.rowNumber = rowNumber;
        this.columnName = columnName;
        this.message = message;
    }

    // Getters
    public int getRowNumber() {
        return rowNumber;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "rowNumber=" + rowNumber +
                ", columnName='" + columnName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

