package com.datamelt.utilities.datagenerator.config.model.options;

public enum RandomDateOptions
{
    MIN_YEAR("minYear",2020),
    MAX_YEAR("maxYear", 2030),
    DATE_FORMAT("dateFormat", "yyyy-MM-dd");
    private String key;
    private Object defaultValue;

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
