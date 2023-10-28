package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public  class RandomDateProcessor extends FieldProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(RandomDateProcessor.class);
    private static final List<String> availableTransformations = Arrays.asList(
    );
    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG
    );
    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.MIN_YEAR, 2020),
            new FieldOption(OptionKey.MAX_YEAR,2030),
            new FieldOption(OptionKey.DATE_FORMAT,"yyyy-MM-dd"),
            new FieldOption(OptionKey.OUTPUT_TYPE,DataTypeDuckDb.VARCHAR.name())
    );

    public RandomDateProcessor(FieldConfiguration fieldConfiguration)
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
            if ((Long) getFieldConfiguration().getOptions().get(RandomDateOptions.MIN_YEAR.getKey()) < 0)
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomDateOptions.MIN_YEAR.getKey() + "] - the value can not be smaller zero");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomDateOptions.MIN_YEAR.getKey() + "] - the value must be of type long");
        }

        try
        {
            if ((Long) getFieldConfiguration().getOptions().get(RandomDateOptions.MAX_YEAR.getKey()) < (Long) getFieldConfiguration().getOptions().get(RandomDateOptions.MIN_YEAR.getKey()))
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomDateOptions.MAX_YEAR.getKey() + "] - the value can not be smaller than the option [" + RandomDateOptions.MIN_YEAR.getKey() + "]");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomDateOptions.MAX_YEAR.getKey() + "] - the value must be of type long");
        }

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat((String)getFieldConfiguration().getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()));
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomDateOptions.DATE_FORMAT.getKey() + "] - the value can not be parsed as a SimpleDateFormat");
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
    protected List<FieldOption> getAvailableOptions() { return availableOptions; }
}
