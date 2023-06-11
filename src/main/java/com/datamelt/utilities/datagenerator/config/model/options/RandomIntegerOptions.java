package com.datamelt.utilities.datagenerator.config.model.options;

public enum RandomIntegerOptions
{
    TRANSFORM("transform", Transformations.UNCHANGED.name().toLowerCase()),
    MIN_VALUE("minValue",1),
    MAX_VALUE("maxValue", 1000000);

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
}
