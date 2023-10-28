package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.Arrays;
import java.util.List;

public enum RandomLongOptions
{
    MIN_VALUE("minValue",0L),
    MAX_VALUE("maxValue", 1000000L),
    OUTPUT_TYPE("outputType", DataTypeDuckDb.LONG.name());

    private final String key;
    private final Object defaultValue;

    RandomLongOptions(String key, Object defaultValue)
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
