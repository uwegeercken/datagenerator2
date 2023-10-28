package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

public enum RandomDateOptions
{
    MIN_YEAR("minYear",2020),
    MAX_YEAR("maxYear", 2030),
    DATE_FORMAT("dateFormat", "yyyy-MM-dd"),
    OUTPUT_TYPE("outputType", DataTypeDuckDb.VARCHAR.name());
    private final String key;
    private final Object defaultValue;

    RandomDateOptions(String key, Object defaultValue)
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
