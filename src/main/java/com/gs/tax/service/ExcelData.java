package com.gs.tax.service;

import java.util.List;
import java.util.Map;

public class ExcelData {
    private Map<String, String> headers;
    private List<Map<String, Object>> rowData;

    public ExcelData(Map<String, String> headers, List<Map<String, Object>> rowData) {
        this.headers = headers;
        this.rowData = rowData;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Map<String, Object>> getRowData() {
        return rowData;
    }

    public void setRowData(List<Map<String, Object>> rowData) {
        this.rowData = rowData;
    }
}
