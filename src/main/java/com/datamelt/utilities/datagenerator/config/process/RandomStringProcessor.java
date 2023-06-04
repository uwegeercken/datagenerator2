package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomStringProcessor implements FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringProcessor.class);

    private DataConfiguration configuration;
    public RandomStringProcessor(DataConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {


    }

    @Override
    public void validateOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
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
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
