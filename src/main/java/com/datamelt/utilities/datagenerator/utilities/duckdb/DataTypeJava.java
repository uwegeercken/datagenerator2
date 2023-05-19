package com.datamelt.utilities.datagenerator.utilities.duckdb;

public enum DataTypeJava
{
    LONG("long"),
    BOOLEAN("boolean"),
    DATE("date"),
    DOUBLE("double"),
    INTEGER("integer"),
    STRING("string");

    public final String value;

    private DataTypeJava(String value)
    {
        this.value = value;
    }
}
