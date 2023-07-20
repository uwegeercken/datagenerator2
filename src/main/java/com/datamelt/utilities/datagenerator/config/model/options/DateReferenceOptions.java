package com.datamelt.utilities.datagenerator.config.model.options;

public enum DateReferenceOptions
{
    REFERENCE("reference",null),
    DATE_FORMAT("dateFormat", null);
    private String key;
    private Object defaultValue;

    DateReferenceOptions(String key, Object defaultValue)
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
