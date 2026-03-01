package com.datamelt.utilities.datagenerator.config.model;

import java.util.ArrayList;
import java.util.List;

public class TransformationConfiguration
{
    private String name;
    private final List<Object> parameters = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }
}
