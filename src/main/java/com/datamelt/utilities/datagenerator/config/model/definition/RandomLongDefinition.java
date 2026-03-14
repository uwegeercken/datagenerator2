package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class RandomLongDefinition
{
    private RandomLongDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of(
            Transformations.NEGATE.getName(),
            Transformations.TOBOOLEAN.getName()
    );

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.LONG,
            DataTypeDuckDb.BOOLEAN
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.MIN_VALUE, 0L,
                    OptionValidations.IS_LONG,
                    "the value must be of type long"),
            new FieldOption(OptionKey.MAX_VALUE, 1000000L,
                    OptionValidations.IS_NON_NEGATIVE_LONG,
                    "the value can not be smaller than zero"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.LONG.name())
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        Long minValue = (Long) config.getOptions().get(OptionKey.MIN_VALUE.getKey());
        Long maxValue = (Long) config.getOptions().get(OptionKey.MAX_VALUE.getKey());
        if (maxValue < minValue)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.MAX_VALUE.getKey() + "] - the value can not be smaller than option [" + OptionKey.MIN_VALUE.getKey() + "]");
        }
    }
}