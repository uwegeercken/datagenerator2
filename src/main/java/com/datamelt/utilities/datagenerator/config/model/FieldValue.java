package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class FieldValue<T>
{
    public static final int DEFAULT_WEIGHT = -1;
    private T value;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private int weight = -1;

    public FieldValue()
    {

    }
    public FieldValue(T value, int weight)
    {
        this.value = value;
        this.weight = weight;
    }

    public FieldValue(T value)
    {
        this.value = value;
    }

    public  T getValue()
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
        FieldValue compareFieldValue = (FieldValue)object;
        if(this.getValue() instanceof String)
        {
            return this.value.equals(compareFieldValue.getValue());
        }
        else
        {
            return this.value == compareFieldValue.getValue();
        }
    }
}
