package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomTimestampOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class RandomTimestampProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(RandomTimestampProcessor.class);

    private static List<String> availableTransformations = Arrays.asList(
    );

    public RandomTimestampProcessor(DataConfiguration configuration)
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
        try
        {
            if ((Long) fieldConfiguration.getOptions().get(RandomTimestampOptions.MIN_YEAR.getKey()) < 0)
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomTimestampOptions.MIN_YEAR.getKey() + "] - the value can not be smaller zero");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomTimestampOptions.MIN_YEAR.getKey() + "] - the value must be of type long");
        }

        try
        {
            if ((Long) fieldConfiguration.getOptions().get(RandomTimestampOptions.MAX_YEAR.getKey()) < (Long) fieldConfiguration.getOptions().get(RandomTimestampOptions.MIN_YEAR.getKey()))
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomTimestampOptions.MAX_YEAR.getKey() + "] - the value can not be smaller than the option [" + RandomTimestampOptions.MIN_YEAR.getKey() + "]");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomTimestampOptions.MAX_YEAR.getKey() + "] - the value must be of type long");
        }

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat((String)fieldConfiguration.getOptions().get(RandomTimestampOptions.DATE_FORMAT.getKey()));
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + RandomDateOptions.DATE_FORMAT.getKey() + "] - the value can not be parsed as a SimpleDateFormat");
        }

    }

    @Override
    protected void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        for(RandomTimestampOptions defaultOption : RandomTimestampOptions.values())
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
