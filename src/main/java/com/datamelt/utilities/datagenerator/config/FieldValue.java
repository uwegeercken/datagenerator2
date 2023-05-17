package com.datamelt.utilities.datagenerator.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldValue
{
    public static final int DEFAULT_WEIGHT = -1;
    private String value;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private int weight = DEFAULT_WEIGHT;

    public FieldValue()
    {

    }
    public FieldValue(String value, int weight)
    {
        this.value = value;
        this.weight = weight;
    }
    public String getValue()
    {
        return value;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }
    @Override
    public String toString()
    {
        return "[" + value + "," + weight + "]";
    }
}
