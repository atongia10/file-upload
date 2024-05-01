/*
package com.gs.tax.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelExportController {

    @Autowired
    private DataService dataService;

    @GetMapping("/download")
    public ResponseEntity<?> downloadExcelFile(HttpServletResponse response) {
        try {
            List<Data> dataList = dataService.findAll();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            createHeaderRow(sheet);

            int rowCount = 1;
            for (Data data : dataList) {
                Row row = sheet.createRow(rowCount++);
                writeData(data, row);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred while generating Excel file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("ID");

        cell = headerRow.createCell(1);
        cell.setCellValue("Name");

        cell = headerRow.createCell(2);
        cell.setCellValue("Value");
    }

    private void writeData(Data data, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(data.getId());

        cell = row.createCell(1);
        cell.setCellValue(data.getName());

        cell = row.createCell(2);
        cell.setCellValue(data.getValue());
    }
}
*/
