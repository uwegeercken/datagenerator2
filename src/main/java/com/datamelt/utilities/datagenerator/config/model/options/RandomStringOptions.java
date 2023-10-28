package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.Arrays;
import java.util.List;

public enum RandomStringOptions
{
    MIN_LENGTH("minLength",0),
    MAX_LENGTH("maxLength", 40),
    RANDOM_CHARACTERS("randomCharacters", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_"),
    OUTPUT_TYPE("outputType", DataTypeDuckDb.VARCHAR.name());

    private final String key;
    private final Object defaultValue;

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
