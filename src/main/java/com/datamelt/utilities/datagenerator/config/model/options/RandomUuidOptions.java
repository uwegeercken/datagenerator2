package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

public enum RandomUuidOptions
{
    OUTPUT_TYPE("outputType", DataTypeDuckDb.VARCHAR.name());

    private final String key;
    private final Object defaultValue;

    RandomUuidOptions(String key, Object defaultValue)
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
