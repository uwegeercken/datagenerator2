package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class RandomStringProcessor extends FieldProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(RandomStringProcessor.class);

    private static final List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.PREPEND.getName(),
            Transformations.APPEND.getName(),
            Transformations.REVERSE.getName(),
            Transformations.TRIM.getName(),
            Transformations.MASKLEADING.getName(),
            Transformations.MASKTRAILING.getName(),
            Transformations.REPLACEALL.getName()
    );

    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR
    );

    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.MIN_LENGTH, 0),
            new FieldOption(OptionKey.MAX_LENGTH,40),
            new FieldOption(OptionKey.RANDOM_CHARACTERS, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public RandomStringProcessor(FieldConfiguration fieldConfiguration)
    {
        super(fieldConfiguration);
    }

    @Override
    protected void validateConfiguration() throws InvalidConfigurationException
    {
        checkOptions();
    }

    private void checkOptions() throws InvalidConfigurationException
    {
        try
        {
            if ((Long) getFieldConfiguration().getOptions().get(RandomStringOptions.MIN_LENGTH.getKey()) <= 0)
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomStringOptions.MIN_LENGTH.getKey() + "] - the value can not be smaller zero");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomStringOptions.MIN_LENGTH.getKey() + "] - the value must be of type long");
        }

        try
        {
            if ((Long) getFieldConfiguration().getOptions().get(RandomStringOptions.MAX_LENGTH.getKey()) < (Long) getFieldConfiguration().getOptions().get(RandomStringOptions.MIN_LENGTH.getKey()))
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomStringOptions.MAX_LENGTH.getKey() + "] - the value can not be smaller than the option [" + RandomStringOptions.MIN_LENGTH.getKey() + "]");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomStringOptions.MAX_LENGTH.getKey() + "] - the value must be of type long");
        }
    }

    @Override
    protected void processConfiguration() throws InvalidConfigurationException {

    }

    @Override
    protected List<String> getAvailableTransformations()
    {
        return availableTransformations;
    }

    @Override
    protected List<DataTypeDuckDb> getAvailableOutputTypes()
    {
        return availableOutputTypes;
    }

    @Override
    protected List<FieldOption> getAvailableOptions()
    {
        return availableOptions;
    }
}
