package com.datamelt.utilities.datagenerator.config.model.options;

public enum CategoryOptions
{
    TRANSFORM("transform", Transformation.UNCHANGED.name());

    private String key;
    private Object defaultValue;

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


}
