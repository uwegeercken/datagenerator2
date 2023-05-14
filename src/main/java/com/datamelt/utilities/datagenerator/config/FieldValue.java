package com.datamelt.utilities.datagenerator.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldValue
{
    public static final int DEFAULT_WEIGHT = -1;
    private String name;
    private int weight = DEFAULT_WEIGHT;

    public FieldValue(@JsonProperty("name") String name, @JsonProperty("weight") int weight)
    {
        this.name = name;
        this.weight = weight;
    }
    public String getName()
    {
        return name;
    }

    public int getWeight()
    {
        return weight;
    }
}
