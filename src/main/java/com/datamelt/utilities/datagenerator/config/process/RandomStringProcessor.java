package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class RandomStringProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringProcessor.class);

    private static List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.PREPEND.getName(),
            Transformations.APPEND.getName(),
            Transformations.REVERSE.getName()
    );

    public RandomStringProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    protected void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkOptions(fieldConfiguration);
        checkTransformations(fieldConfiguration);

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

    private void checkOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if((Long)fieldConfiguration.getOptions().get(RandomStringOptions.MIN_LENGTH.getKey()) < 0)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomStringOptions.MIN_LENGTH.getKey() + "] - the value can not be smaller zero");
        }
        if((Long)fieldConfiguration.getOptions().get(RandomStringOptions.MAX_LENGTH.getKey()) < (Long)fieldConfiguration.getOptions().get(RandomStringOptions.MIN_LENGTH.getKey()))
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomStringOptions.MAX_LENGTH.getKey() + "] - the value can not be smaller than the option [" + RandomStringOptions.MIN_LENGTH.getKey() + "]");
        }
    }

    @Override
    protected void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        for(RandomStringOptions defaultOption : RandomStringOptions.values())
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
}
