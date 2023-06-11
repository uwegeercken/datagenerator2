package com.datamelt.utilities.datagenerator.config.model.options;

import java.util.Arrays;
import java.util.List;

public enum CategoryOptions
{
    TRANSFORM("transform", Transformations.UNCHANGED.name().toLowerCase());

    private String key;
    private Object defaultValue;

    private static final List<String> availableTransformations = Arrays.asList();

    CategoryOptions(String key, Object defaultValue)
    {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey()
    {
        return key;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public static List<String> getAvailableTransformations()
    {
        return availableTransformations;
    }
}
