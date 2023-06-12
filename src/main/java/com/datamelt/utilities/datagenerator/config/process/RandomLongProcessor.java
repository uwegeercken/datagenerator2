package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import com.datamelt.utilities.datagenerator.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RandomLongProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomLongProcessor.class);

    public RandomLongProcessor(DataConfiguration configuration)
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
                    String[] transformations = ((String) entry.getValue()).split(Constants.OPTION_TRANSFORM_DIVIDER);
                    for(String transformation : transformations)
                    {
                        if(!RandomLongOptions.getAvailableTransformations().contains(transformation.trim()) && !transformation.equals(Transformations.UNCHANGED.getName()))
                        {
                            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + entry.getKey() + "], value [" + transformation.trim() + "] is invalid - must be in list " + RandomLongOptions.getAvailableTransformations());
                        }
                    }
                }
            }
        }
    }
    @Override
    public void setDefaultOptions(FieldConfiguration fieldConfiguration)
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
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
