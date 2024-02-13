package com.gs.tax.service;

import org.springframework.stereotype.Component;

@Component
public class FileProcessorFactory {
    public FileProcessor getFileProcessor(String fileType) {
        switch (fileType.toLowerCase()) {
            case "xlsx":
            case "xls":
                return new ExcelFileProcessor();
            case "csv":
                return new CsvFileProcessor();
            case "txt":
                return new TextFileProcessor();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}