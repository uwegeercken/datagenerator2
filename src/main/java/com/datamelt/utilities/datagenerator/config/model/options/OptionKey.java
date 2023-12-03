package com.datamelt.utilities.datagenerator.config.model.options;

public enum OptionKey
{
    MIN_VALUE("minValue"),
    MAX_VALUE("maxValue"),
    CATEGORY_FILE_SEPARATOR("categoryFileSeparator"),
    REFERENCE("reference"),
    DATE_FORMAT("dateFormat"),
    MIN_YEAR("minYear"),
    MAX_YEAR("maxYear"),
    MIN_LENGTH("minLength"),
    MAX_LENGTH("maxLength"),
    RANDOM_CHARACTERS("randomCharacters"),

    OUTPUT_TYPE("outputType");

    private final String key;

    OptionKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
