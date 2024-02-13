package com.gs.tax.controller;

import com.gs.tax.service.FileProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final FileProcessingService fileProcessingService;

    @Autowired
    public FileUploadController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(){
        System.out.println("Hello");
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @Operation(summary = "Greets the user")
    @PostMapping(value = "/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("schema") MultipartFile schemaFile) {
        try {
            // The processFile method is supposed to handle file processing and validation
            Object result = fileProcessingService.processFile(file, schemaFile);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Exception handling can be more specific based on the types of exceptions your service might throw
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }
}
