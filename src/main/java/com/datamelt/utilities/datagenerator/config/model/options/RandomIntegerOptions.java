package com.datamelt.utilities.datagenerator.config.model.options;

import java.util.Arrays;
import java.util.List;

public enum RandomIntegerOptions
{
    TRANSFORM("transform", Transformations.UNCHANGED.name().toLowerCase()),
    MIN_VALUE("minValue",1),
    MAX_VALUE("maxValue", 1000000);

    private static final List<String> availableTransformations = Arrays.asList(new String[]{Transformations.NEGATE.getName()});

    private String key;
    private Object defaultValue;

    RandomIntegerOptions(String key, Object defaultValue)
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
