package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

public enum RandomDoubleOptions
{
    MIN_VALUE("minValue",0L),
    MAX_VALUE("maxValue", 1000000L),
    OUTPUT_TYPE("outputType", DataTypeDuckDb.DOUBLE.name());
    private final String key;
    private final Object defaultValue;

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
