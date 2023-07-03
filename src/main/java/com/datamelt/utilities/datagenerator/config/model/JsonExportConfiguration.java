package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class JsonExportConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    CsvDelimiterType delimiter = CsvDelimiterType.COMMA;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    boolean includeHeader = true;

    public CsvDelimiterType getDelimiter() {
        return delimiter;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setDelimiter(CsvDelimiterType delimiter) {
        this.delimiter = delimiter;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }
}
