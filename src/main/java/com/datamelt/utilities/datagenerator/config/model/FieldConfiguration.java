package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldConfiguration
{
    String name;
    String valuesFile;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    FieldType type = FieldType.CATEGORY;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    FieldDataType dataType = FieldDataType.STRING;

    Map<String, Object> options = new HashMap<>();

    List<TransformationConfiguration> transformations = new ArrayList<>();

    List<FieldConfigurationValue> values = new ArrayList<>();

    int numberOfDefaultWeights = 0;

    public FieldConfiguration(@JsonProperty("name") String name)
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

    public List<FieldConfigurationValue> getValues()
    {
        return values;
    }

    public int getNumberOfFieldValues()
    {
        return values.size();
    }

    public boolean containsFieldValue(FieldConfigurationValue fieldConfigurationValue)
    {
        if(fieldConfigurationValue != null)
        {
            return values.contains(fieldConfigurationValue);
        }
        return false;
    }

    public String getValuesAndWeights()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        int sum = 0;
        for(FieldConfigurationValue value : values)
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
        for(FieldConfigurationValue value : values)
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
        for(FieldConfigurationValue value : values)
        {
            if(value.getWeight() != FieldConfigurationValue.DEFAULT_WEIGHT)
            {
                sumOfWeights = sumOfWeights + value.getWeight();
            }
        }
        return sumOfWeights;
    }

    public void calculateNumberOfDefaultWeights()
    {
        int numberOfDefaultWeights = 0;
        for(FieldConfigurationValue value : values)
        {
            if(value.getWeight() == FieldConfigurationValue.DEFAULT_WEIGHT)
            {
                numberOfDefaultWeights++;
            }
        }
        this.numberOfDefaultWeights = numberOfDefaultWeights;
    }

    public void setValues(List<FieldConfigurationValue> values)
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

    public FieldDataType getDataType()
    {
        return dataType;
    }

    public FieldType getType() {
        return type;
    }

    public Map<String, Object> getOptions()
    {
        return options;
    }

    public List<TransformationConfiguration> getTransformations()
    {
        return transformations;
    }
}
