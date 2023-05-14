package com.datamelt.utilities.datagenerator.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Field
{
    String name;
    String valuesFile;
    String valueFileDataType;
    List<FieldValue> values = new ArrayList<>();

    public Field(@JsonProperty("name") String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<FieldValue> getValues()
    {
        return values;
    }

    public void setValues(List<FieldValue> values)
    {
        this.values = values;
    }

    public String getValuesFile()
    {
        return valuesFile;
    }

    public String getValueFileDataType()
    {
        return valueFileDataType;
    }
}
