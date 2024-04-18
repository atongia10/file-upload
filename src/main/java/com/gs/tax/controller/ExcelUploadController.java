package com.gs.tax.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Format;
import java.util.*;

@RestController
public class ExcelUploadController {

    @PostMapping(value = "/upload1",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Assuming first row is headers
            List<String> headers = new ArrayList<>();
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            }

            // Assuming second row defines cell types
            Map<String, String> schema = new LinkedHashMap<>();
            if (rowIterator.hasNext()) {
                Row typeRow = rowIterator.next();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = typeRow.getCell(i);
                    schema.put(headers.get(i), mapCellTypeToSchemaType(cell));
                }
            }

            workbook.close();
            return ResponseEntity.ok(schema);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file");
        }
    }

    private String mapCellTypeToSchemaType(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return "String";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Retrieve the cell's date format
                    DataFormatter formatter = new DataFormatter();
                    Format dateFormat = formatter.createFormat(cell);
                    return "Date (" + dateFormat.format(cell.getDateCellValue()) + ")";
                } else {
                    // Check if the numeric value is an integer or has a decimal point
                    double value = cell.getNumericCellValue();
                    if (value % 1 == 0) {
                        return "Integer";
                    } else {
                        return "Double";
                    }
                }
            case BOOLEAN:
                return "Boolean";
            case FORMULA:
                // This is simplified; consider evaluating the formula and determining the data type of its result
                return "Formula";
            default:
                return "Unknown";
        }
    }



}


