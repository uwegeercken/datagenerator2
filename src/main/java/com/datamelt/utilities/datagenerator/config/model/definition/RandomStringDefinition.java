package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class RandomStringDefinition
{
    private RandomStringDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.PREPEND.getName(),
            Transformations.APPEND.getName(),
            Transformations.REVERSE.getName(),
            Transformations.TRIM.getName(),
            Transformations.MASKLEADING.getName(),
            Transformations.MASKTRAILING.getName(),
            Transformations.REPLACEALL.getName(),
            Transformations.REMOVE.getName()
    );

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.MIN_LENGTH, 1L,
                    OptionValidations.IS_POSITIVE_LONG,
                    "the value must be greater than 0"),
            new FieldOption(OptionKey.MAX_LENGTH, 40L,
                    OptionValidations.IS_POSITIVE_LONG,
                    "the value must be greater than 0"),
            new FieldOption(OptionKey.RANDOM_CHARACTERS,
                    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        Long minLength = (Long) config.getOptions().get(OptionKey.MIN_LENGTH.getKey());
        Long maxLength = (Long) config.getOptions().get(OptionKey.MAX_LENGTH.getKey());
        if (maxLength < minLength)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.MAX_LENGTH.getKey() + "] - the value can not be smaller than option [" + OptionKey.MIN_LENGTH.getKey() + "]");
        }
    }
}