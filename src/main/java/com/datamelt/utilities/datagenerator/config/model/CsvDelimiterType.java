package com.datamelt.utilities.datagenerator.config.model;

public enum CsvDelimiterType
{
    COMMA(","),
    COLON(":"),
    SEMICOLON(";"),
    TAB("\t"),
    VERTICALBAR("|"),
    ATSIGN("@");

    private String value;

    CsvDelimiterType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
