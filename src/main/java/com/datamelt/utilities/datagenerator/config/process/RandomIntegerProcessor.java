package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomIntegerOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

public class RandomIntegerProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomIntegerProcessor.class);

    public RandomIntegerProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkOptionsTransform(fieldConfiguration);

    }

    private void checkOptionsTransform(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getOptions()!=null)
        {
            for(Map.Entry<String, Object> entry : fieldConfiguration.getOptions().entrySet())
            {
                if(entry.getKey().equals(CategoryOptions.TRANSFORM.getKey()))
                {
                    String value = (String) entry.getValue();
                    if(!Arrays.stream(Transformations.values()).anyMatch(v -> v.name().toLowerCase().equals(value.toLowerCase())))
                    {
                        throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + entry.getKey() + "] - must be one out of " + Transformations.getValues(Integer.class));
                    }
                }
            }
        }
    }
    @Override
    public void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        for(RandomIntegerOptions defaultOption : RandomIntegerOptions.values())
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
