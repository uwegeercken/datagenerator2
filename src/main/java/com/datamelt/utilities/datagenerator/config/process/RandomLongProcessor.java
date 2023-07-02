package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
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

    public RandomLongProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    protected void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkOptions(fieldConfiguration);
        checkTransformations(fieldConfiguration);

    }

    private void checkOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if((Long)fieldConfiguration.getOptions().get(RandomLongOptions.MAX_VALUE.getKey()) < (Long)fieldConfiguration.getOptions().get(RandomLongOptions.MIN_VALUE.getKey()))
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomLongOptions.MAX_VALUE.getKey() + "] - the value can not be smaller than the option [" + RandomLongOptions.MIN_VALUE.getKey() + "]");
        }
    }

    @Override
    protected void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        for(RandomLongOptions defaultOption : RandomLongOptions.values())
        {
            if(!fieldConfiguration.getOptions().containsKey(defaultOption.getKey()))
            {
                fieldConfiguration.getOptions().put(defaultOption.getKey(), defaultOption.getDefaultValue());
            }
        }
    }

    @Override
    protected void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }

    private void checkTransformations(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getTransformations()!=null)
        {
            for(TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                if(!availableTransformations.contains(configuredTransformation.getName()))
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is not allowed - must be in list: " + Arrays.toString(availableTransformations.toArray()));
                }
            }
        }
    }
}
