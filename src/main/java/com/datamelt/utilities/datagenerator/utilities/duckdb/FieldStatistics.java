package com.datamelt.utilities.datagenerator.utilities.duckdb;

import java.util.HashMap;
import java.util.Map;

public class FieldStatistics
{
    private String fieldName;
    private long numberOfDistinctValues = 0;
    private Map<String, Double> fieldStatistics = new HashMap<>();

    public FieldStatistics(String fieldName)
    {
        this.fieldName = fieldName;
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

    public Map<String, Double> getFieldStatistics()
    {
        return fieldStatistics;
    }
}
