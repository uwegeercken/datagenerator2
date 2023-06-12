package com.datamelt.utilities.datagenerator.config.model.options;

import java.util.Arrays;
import java.util.List;

public enum RandomStringOptions
{
    TRANSFORM("transform", Transformations.UNCHANGED.name().toLowerCase()),
    MIN_LENGTH("minLength",0),
    MAX_LENGTH("maxLength", 40),
    RANDOM_CHARACTERS("randomCharacters", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_");

    private static final List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.REVERSE.getName()
    );

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

    public static List<String> getAvailableTransformations()
    {
        return availableTransformations;
    }

}
