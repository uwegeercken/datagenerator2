package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class DateReferenceDefinition
{
    private DateReferenceDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of(
            Transformations.TOQUARTER.getName(),
            Transformations.TOHALFYEAR.getName()
    );

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.REFERENCE, null,
                    OptionValidations.IS_NOT_EMPTY_STRING,
                    "the value can not be null or empty"),
            new FieldOption(OptionKey.DATE_FORMAT, null,
                    OptionValidations.IS_VALID_SIMPLE_DATE_FORMAT,
                    "the value can not be parsed as a date format"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        boolean monthValueTransformations =
                config.containsTransformation(Transformations.TOQUARTER) ||
                config.containsTransformation(Transformations.TOHALFYEAR);
        String dateFormat = (String) config.getOptions().get(OptionKey.DATE_FORMAT.getKey());
        if (dateFormat != null && !dateFormat.equals("MM") && monthValueTransformations)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.DATE_FORMAT.getKey() + "] - transformations [" + Transformations.TOQUARTER.getName() + ", " + Transformations.TOHALFYEAR.getName() + "] can only be used with dateFormat 'MM'");
        }
    }
}