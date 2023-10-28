package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RandomLongProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomLongProcessor.class);

    private static List<String> availableTransformations = Arrays.asList(
            Transformations.NEGATE.getName()
    );

    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.LONG
    );

    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.MIN_VALUE, 0L),
            new FieldOption(OptionKey.MAX_VALUE,1000000L),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.LONG.name())
    );

    public RandomLongProcessor(FieldConfiguration fieldConfiguration)
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
            if ((Long) getFieldConfiguration().getOptions().get(RandomLongOptions.MAX_VALUE.getKey()) < (Long) getFieldConfiguration().getOptions().get(RandomLongOptions.MIN_VALUE.getKey()))
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomLongOptions.MAX_VALUE.getKey() + "] - the value can not be smaller than the option [" + RandomLongOptions.MIN_VALUE.getKey() + "]");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + RandomLongOptions.MIN_VALUE.getKey() + ", " + RandomLongOptions.MAX_VALUE.getKey() + "] - the values must be of type long");
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
