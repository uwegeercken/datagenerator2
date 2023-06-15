package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import com.datamelt.utilities.datagenerator.utilities.Constants;
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
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkTransformations(fieldConfiguration);

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

    private void checkTransformations(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getTransformations()!=null)
        {
            for(TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                try
                {
                    Transformations.valueOf(configuredTransformation.getName());
                }
                catch(IllegalArgumentException ex)
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is invalid - must be in list: " + Arrays.toString(availableTransformations.toArray()));
                }
            }
        }
    }
}
