package com.datamelt.utilities.datagenerator.config.model.options;

import java.util.Arrays;
import java.util.List;

public enum RandomDoubleOptions
{
    MIN_VALUE("minValue",0L),
    MAX_VALUE("maxValue", 1000000L);

    private String key;
    private Object defaultValue;

    RandomDoubleOptions(String key, Object defaultValue)
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

}
