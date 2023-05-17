package com.datamelt.utilities.datagenerator.utilities;

public class RowField {
    private String name;
    private Object value;

    public RowField(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
