package com.gs.tax.controller;

import com.gs.tax.service.ExcelData;
import com.gs.tax.service.ExcelService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            ExcelData data = excelService.processExcelFile(file);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not process file: " + e.getMessage());
        }
    }
}
