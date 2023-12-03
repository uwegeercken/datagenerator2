package com.datamelt.utilities.datagenerator.config.model.options;

public class FieldOption
{
    private final OptionKey key;

    private final Object defaultValue;

    public FieldOption(OptionKey key, Object defaultValue)
    {
        this.key = key;
        this.defaultValue = defaultValue;
    }
    public OptionKey getOptionKey()
    {
        return key;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }
}
