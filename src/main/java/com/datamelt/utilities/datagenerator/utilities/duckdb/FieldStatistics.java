package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

import java.util.HashMap;
import java.util.Map;

public class FieldStatistics
{
    private final String fieldName;
    private final FieldType fieldType;
    private long numberOfDistinctValues = 0;
    private Map<String, Double> fieldStatistics = new HashMap<>();

    public FieldStatistics(String fieldName, FieldType fieldType)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
    public void addValueCount(String fieldValue, double count)
    {
        fieldStatistics.put(fieldValue, count);
    }

    public long getNumberOfDistinctValues()
    {
        return numberOfDistinctValues;
    }

    public void setNumberOfDistinctValues(long numberOfDistinctValues)
    {
        this.numberOfDistinctValues = numberOfDistinctValues;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public Map<String, Double> getFieldStatistics()
    {
        return fieldStatistics;
    }
}
