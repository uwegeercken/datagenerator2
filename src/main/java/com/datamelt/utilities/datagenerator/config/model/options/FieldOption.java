package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.function.Predicate;

public class FieldOption
{
    private final OptionKey key;
    private final Object defaultValue;
    private final Predicate<Object> validator;
    private final String validationErrorMessage;

    public FieldOption(OptionKey key, Object defaultValue, Predicate<Object> validator, String validationErrorMessage)
    {
        this.key = key;
        this.defaultValue = defaultValue;
        this.validator = validator;
        this.validationErrorMessage = validationErrorMessage;
    }

    public FieldOption(OptionKey key, Object defaultValue)
    {
        this(key, defaultValue, value -> true, "");
    }


    public void validate(String fieldName, Object value) throws InvalidConfigurationException
    {
        if (value == null)
        {
            return;
        }
        try
        {
            if (!validator.test(value))
            {
                throw new InvalidConfigurationException("field [" + fieldName + "], option [" + key.getKey() + "] - " + validationErrorMessage);
            }
        }
        catch (ClassCastException cce)
        {
            String expectedType = defaultValue != null
                    ? defaultValue.getClass().getSimpleName().toLowerCase()
                    : "string";
            throw new InvalidConfigurationException("field [" + fieldName + "], option [" + key.getKey() + "] - the value must be of type " + expectedType);
        }
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