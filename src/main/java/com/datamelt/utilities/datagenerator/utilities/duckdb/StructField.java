package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldType;

public class StructField
{
    private String name;
    private FieldType fieldType;

    public StructField(String name, FieldType fieldType)
    {
        this.name = name;
        this.fieldType = fieldType;
    }

    public String getName()
    {
        return name;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }
}
