package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Field
{
    String name;
    String valuesFile;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    String type = "category";
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    String dataType = "string";
    List<FieldValue> values = new ArrayList<>();

    int numberOfDefaultWeights=0;

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

    public int getNumberOfFieldValues()
    {
        return values.size();
    }

    public boolean containsFieldValue(FieldValue fieldValue)
    {
        if(fieldValue != null)
        {
            return values.contains(fieldValue);
        }
        return false;
    }

    public String getValuesAndWeights()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        int sum = 0;
        for(FieldValue value : values)
        {
            counter++;
            sum = sum + value.getWeight();
            buffer.append( value.toString() + "(" + sum + ")");
            if(counter< values.size())
            {
                buffer.append( " - ");
            }
        }
        return buffer.toString();
    }

    public String getValuesAsString()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        int sum = 0;
        for(FieldValue value : values)
        {
            counter++;
            buffer.append("'" + value.getValue().toString() + "'");
            if(counter< values.size())
            {
                buffer.append( ",");
            }
        }
        return buffer.toString();
    }
    public int getSumOfWeights()
    {
        int sumOfWeights = 0;
        for(FieldValue value : values)
        {
            if(value.getWeight() != FieldValue.DEFAULT_WEIGHT)
            {
                sumOfWeights = sumOfWeights + value.getWeight();
            }
        }
        return sumOfWeights;
    }

    public void calculateNumberOfDefaultWeights()
    {
        int numberOfDefaultWeights = 0;
        for(FieldValue value : values)
        {
            if(value.getWeight() == FieldValue.DEFAULT_WEIGHT)
            {
                numberOfDefaultWeights++;
            }
        }
        this.numberOfDefaultWeights = numberOfDefaultWeights;
    }

    public void setValues(List<FieldValue> values)
    {
        this.values = values;
    }

    public String getValuesFile()
    {
        return valuesFile;
    }

    public int getNumberOfDefaultWeights()
    {
        return numberOfDefaultWeights;
    }

    public String getDataType()
    {
        return dataType;
    }

    public String getType() {
        return type;
    }
}
