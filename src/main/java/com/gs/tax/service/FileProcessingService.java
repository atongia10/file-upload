package com.gs.tax.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.tax.exception.ValidationException;
import com.gs.tax.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    private final ObjectMapper objectMapper;
    private final FileProcessorFactory fileProcessorFactory;

    @Autowired
    public FileProcessingService(ObjectMapper objectMapper, FileProcessorFactory fileProcessorFactory) {
        this.objectMapper = objectMapper;
        this.fileProcessorFactory = fileProcessorFactory;
    }

    public ResponseEntity<?> processFile(MultipartFile file, MultipartFile schemaFile) {
        try {
            JsonNode schema = objectMapper.readTree(schemaFile.getInputStream());
            String fileType = getFileExtension(file.getOriginalFilename());
            FileProcessor processor = fileProcessorFactory.getFileProcessor(fileType);

            List<Map<String, Object>> processedData = processor.processFile(file, schema);
            if (processedData.isEmpty()) {
                // No data processed could mean an entirely invalid file based on schema validations
                return ResponseEntity.badRequest().body("No valid data could be processed from the file based on the provided schema.");
            }

            return ResponseEntity.ok(processedData);
        } catch (ValidationException ve) {
            // Assuming ValidationException contains details about validation errors
            List<String> errorMessages = ve.getValidationErrors().stream()
                    .map(ValidationError::toString)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        } catch (Exception e) {
            // Generic error handling
            return ResponseEntity.badRequest().body("An error occurred while processing the file: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        } else {
            throw new IllegalArgumentException("File does not have a valid extension: " + filename);
        }
    }
}


