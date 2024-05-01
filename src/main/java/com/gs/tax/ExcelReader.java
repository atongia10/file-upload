package com.gs.tax;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public List<Map<String, Object>> readExcel(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Map<String, Object>> rowDataList = new ArrayList<>();
        Row headerRow = rowIterator.next(); // Assuming the first row is the header
        List<String> headers = new ArrayList<>();

        // Reading header information
        headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

        // Iterating over each row and constructing the map
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, Object> rowMap = new HashMap<>();
            for (int cn=0; cn<row.getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null) {
                    Object value = getCellValue(cell);
                    rowMap.put(headers.get(cn), value);
                }
            }
            rowDataList.add(rowMap);
        }
        workbook.close();
        return rowDataList;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}
