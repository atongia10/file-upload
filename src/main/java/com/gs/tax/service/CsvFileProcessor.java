package com.gs.tax.service;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

public class CsvFileProcessor implements FileProcessor {

    @Override
    public List<Map<String, Object>> processFile(MultipartFile file, JsonNode schema) throws Exception {
        return null;
    }
}
