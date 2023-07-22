package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldType;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
    private String name;
    private List<StructField> fields = new ArrayList<>();

    public Struct(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StructField> getFields()
    {
        return fields;
    }

    public void setFields(List<StructField> fields)
    {
        this.fields = fields;
    }

    public void addField(String name, FieldType fieldType)
    {
        fields.add(new StructField(name, fieldType));
    }
}
