package com.datamelt.utilities.datagenerator.config.model;

import java.util.ArrayList;
import java.util.List;

public class TransformationConfiguration
{
    private String name;
    private List<Object> parameters = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }
}
