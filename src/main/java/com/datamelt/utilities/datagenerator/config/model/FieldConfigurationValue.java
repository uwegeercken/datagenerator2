package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class FieldConfigurationValue
{
    public static final int DEFAULT_WEIGHT = -1;
    private String value;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private int weight = -1;

    public FieldConfigurationValue()
    {

    }
    public FieldConfigurationValue(String value, int weight)
    {
        this.value = value;
        this.weight = weight;
    }

    public FieldConfigurationValue(String value)
    {
        this.value = value;
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

    @Override
    public boolean equals(Object object)
    {
        FieldConfigurationValue compareFieldConfigurationValue = (FieldConfigurationValue)object;
        return this.value.equals(compareFieldConfigurationValue.getValue());
    }
}
