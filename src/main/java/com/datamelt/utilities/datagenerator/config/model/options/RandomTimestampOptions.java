package com.datamelt.utilities.datagenerator.config.model.options;

public enum RandomTimestampOptions
{
    MIN_YEAR("minYear",2020),
    MAX_YEAR("maxYear", 2030),
    DATE_FORMAT("dateFormat", "yyyy-MM-dd HH:mm:ss");
    private String key;
    private Object defaultValue;

    RandomTimestampOptions(String key, Object defaultValue)
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
