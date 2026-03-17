package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class RandomDateDefinition
{
    private RandomDateDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of();

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.MIN_YEAR, 2020L,
                    OptionValidations.IS_NON_NEGATIVE_LONG,
                    "the value can not be smaller than zero"),
            new FieldOption(OptionKey.MAX_YEAR, 2030L,
                    OptionValidations.IS_NON_NEGATIVE_LONG,
                    "the value can not be smaller than zero"),
            new FieldOption(OptionKey.DATE_FORMAT, "yyyy-MM-dd",
                    OptionValidations.IS_VALID_SIMPLE_DATE_FORMAT,
                    "the value can not be parsed as a date format"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name()),
            new FieldOption(OptionKey.NULL_PROBABILITY, 0L,
                    OptionValidations.IS_PERCENTAGE,
                    "the value must be between 0 and 100")
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        Long minYear = (Long) config.getOptions().get(OptionKey.MIN_YEAR.getKey());
        Long maxYear = (Long) config.getOptions().get(OptionKey.MAX_YEAR.getKey());
        if (maxYear < minYear)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.MAX_YEAR.getKey() + "] - the value can not be smaller than option [" + OptionKey.MIN_YEAR.getKey() + "]");
        }
    }
}