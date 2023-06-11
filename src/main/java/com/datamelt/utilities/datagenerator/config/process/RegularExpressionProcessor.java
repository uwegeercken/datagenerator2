package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;

import java.util.Arrays;
import java.util.Map;

public class RegularExpressionProcessor extends FieldProcessor
{
    public RegularExpressionProcessor(DataConfiguration configuration)
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
                        throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + entry.getKey() + "] - must be one out of " + Transformations.getValues(String.class));
                    }
                }
            }
        }
    }

    @Override
    public void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {

    }

    @Override
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
