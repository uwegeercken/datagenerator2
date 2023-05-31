package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CsvExportConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    String delimiter = ",";
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    boolean includeHeader = true;

    public String getDelimiter() {
        return delimiter;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }
}
