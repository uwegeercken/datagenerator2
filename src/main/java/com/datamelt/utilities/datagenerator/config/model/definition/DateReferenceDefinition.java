package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.text.SimpleDateFormat;
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
            new FieldOption(OptionKey.REFERENCE, null),
            new FieldOption(OptionKey.DATE_FORMAT, null),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name()),
            new FieldOption(OptionKey.NULL_PROBABILITY, 0L,
                    OptionValidations.IS_PERCENTAGE,
                    "the value must be between 0 and 100"),
            new FieldOption(OptionKey.MIN_DAYS_OFFSET, 0L,
                    OptionValidations.IS_LONG,
                    "the value must be of type long"),
            new FieldOption(OptionKey.MAX_DAYS_OFFSET, 0L,
                    OptionValidations.IS_LONG,
                    "the value must be of type long"),
            new FieldOption(OptionKey.ADJUST_TO, null)
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        // reference is mandatory
        String reference = (String) config.getOptions().get(OptionKey.REFERENCE.getKey());
        if (reference == null || reference.isBlank())
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.REFERENCE.getKey() + "] - the value can not be null or empty");
        }

        // dateFormat must be a valid date format if provided
        String dateFormat = (String) config.getOptions().get(OptionKey.DATE_FORMAT.getKey());
        if (dateFormat != null)
        {
            try { new SimpleDateFormat(dateFormat); }
            catch (Exception e)
            {
                throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.DATE_FORMAT.getKey() + "] - the value can not be parsed as a date format");
            }
        }

        // adjustTo must be one of the valid values if provided
        String adjustTo = (String) config.getOptions().get(OptionKey.ADJUST_TO.getKey());
        if (adjustTo != null && !OptionValidations.VALID_ADJUST_TO_VALUES.contains(adjustTo))
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.ADJUST_TO.getKey() + "] - the value must be one of: " + OptionValidations.VALID_ADJUST_TO_VALUES);
        }

        // toQuarter and toHalfYear require dateFormat MM
        boolean monthValueTransformations =
                config.containsTransformation(Transformations.TOQUARTER) ||
                        config.containsTransformation(Transformations.TOHALFYEAR);
        if (dateFormat != null && !dateFormat.equals("MM") && monthValueTransformations)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.DATE_FORMAT.getKey() + "] - transformations [" + Transformations.TOQUARTER.getName() + ", " + Transformations.TOHALFYEAR.getName() + "] can only be used with dateFormat 'MM'");
        }

        // maxDaysOffset must be >= minDaysOffset
        Long minDaysOffset = (Long) config.getOptions().get(OptionKey.MIN_DAYS_OFFSET.getKey());
        Long maxDaysOffset = (Long) config.getOptions().get(OptionKey.MAX_DAYS_OFFSET.getKey());
        if (minDaysOffset != null && maxDaysOffset != null && maxDaysOffset < minDaysOffset)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "], option [" + OptionKey.MAX_DAYS_OFFSET.getKey() + "] - the value can not be smaller than option [" + OptionKey.MIN_DAYS_OFFSET.getKey() + "]");
        }

        // adjustTo and daysOffset are mutually exclusive
        boolean hasAdjustTo = adjustTo != null && !adjustTo.isBlank();
        boolean hasDaysOffset = (minDaysOffset != null && minDaysOffset != 0) ||
                (maxDaysOffset != null && maxDaysOffset != 0);
        if (hasAdjustTo && hasDaysOffset)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "] - options [" + OptionKey.ADJUST_TO.getKey() + "] and [" + OptionKey.MIN_DAYS_OFFSET.getKey() + "/" + OptionKey.MAX_DAYS_OFFSET.getKey() + "] are mutually exclusive");
        }
    }
}