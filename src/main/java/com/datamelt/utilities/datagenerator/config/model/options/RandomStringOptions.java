package com.datamelt.utilities.datagenerator.config.model.options;

public enum RandomStringOptions
{
    MIN_LENGTH("minLength",0),
    MAX_LENGTH("maxLength", 40),
    RANDOM_CHARACTERS("randomCharacters", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_");

    private String key;
    private Object defaultValue;

    RandomStringOptions(String key, Object defaultValue)
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
