package com.gs.tax.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ExcelData processExcelFile(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        List<Map<String, Object>> rowData = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();

        // Read header row to get header names
        Row headerRow = sheet.getRow(0);

        // Use the second row to determine data types
        Row typeRow = sheet.getRow(1);
        if (typeRow == null) {
            throw new IllegalArgumentException("The second row is required for data type determination");
        }

        for (int cn = 0; cn < headerRow.getLastCellNum(); cn++) {
            Cell headerCell = headerRow.getCell(cn);
            Cell typeCell = typeRow.getCell(cn);
            if (headerCell != null && typeCell != null) {
                headers.put(headerCell.getStringCellValue(), getJavaType(typeCell, evaluator));
            }
        }

        // Process all rows for data
        for (int rn = 1; rn <= sheet.getLastRowNum(); rn++) {
            Row row = sheet.getRow(rn);
            Map<String, Object> rowMap = new HashMap<>();
            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn);
                if (headerRow.getCell(cn) != null) {
                    rowMap.put(headerRow.getCell(cn).getStringCellValue(), getCellValue(cell, evaluator));
                }
            }
            rowData.add(rowMap);
        }

        workbook.close();
        return new ExcelData(headers, rowData);
    }

    private String getJavaType(Cell cell, FormulaEvaluator evaluator) {
        Cell evaluatedCell = evaluator.evaluateInCell(cell);
        if (evaluatedCell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return "Date";
            } else {
                // Check if the number is an integer (no fractional part)
                double value = evaluatedCell.getNumericCellValue();
                return (value % 1 == 0) ? "Integer" : "Double";
            }
        }
        return switch (evaluatedCell.getCellType()) {
            case BOOLEAN -> "Boolean";
            case STRING -> "String";
            case FORMULA -> "Formula";  // Further distinction might be needed based on the formula result type
            default -> "Object";
        };
    }

    private Object getCellValue(Cell cell, FormulaEvaluator evaluator) {
        evaluator.evaluate(cell);  // Make sure the cell is evaluated
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());  // Format date
                } else {
                    double value = cell.getNumericCellValue();
                    return (value % 1 == 0) ? (int) value : value;  // Cast to int if no fractional part
                }
            default:
                return null;  // Return null for undefined or error types
        }
    }
}
