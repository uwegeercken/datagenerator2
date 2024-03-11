package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.FieldOption;
import com.datamelt.utilities.datagenerator.config.model.options.GenericOptions;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.Arrays;
import java.util.List;

public abstract class FieldProcessor
{
    private final FieldConfiguration fieldConfiguration;

    public FieldProcessor(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
    }
    protected void validateTransformations() throws InvalidConfigurationException
    {
        if (fieldConfiguration.getTransformations() != null)
        {
            for (TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                if (!getAvailableTransformations().contains(configuredTransformation.getName()))
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is not available - must be one of: " + Arrays.toString(getAvailableTransformations().toArray()));
                }
            }
        }
    }

    protected void checkOutputType() throws InvalidConfigurationException
    {
        DataTypeDuckDb dataTypeDuckDb = fieldConfiguration.getOutputType();
        if (dataTypeDuckDb == null)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + GenericOptions.OUTPUT_TYPE.getKey() + "] - the provided value [" + fieldConfiguration.getOptions().get(GenericOptions.OUTPUT_TYPE.getKey()) + "] is invalid");
        }
    }

    protected void validateOutputType() throws InvalidConfigurationException
    {
        if(fieldConfiguration.getOutputType()!=null)
        {
            if(!getAvailableOutputTypes().contains(fieldConfiguration.getOutputType()))
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], output type [" + fieldConfiguration.getOutputType() + "] is not available - must be one of: " + Arrays.toString(getAvailableOutputTypes().toArray()));
            }
        }
        else
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + GenericOptions.OUTPUT_TYPE.getKey() + "] - the provided value [" + getFieldConfiguration().getOptions().get(GenericOptions.OUTPUT_TYPE.getKey()) + "] is invalid");
        }
    }

    protected void setDefaultOptions()
    {
        for(FieldOption availableOption : getAvailableOptions())
        {
            if(!getFieldConfiguration().getOptions().containsKey(availableOption.getOptionKey().getKey()))
            {
                getFieldConfiguration().getOptions().put(availableOption.getOptionKey().getKey(), availableOption.getDefaultValue());
            }
        }
    }

    protected abstract void validateConfiguration() throws InvalidConfigurationException;

    protected abstract void processConfiguration() throws InvalidConfigurationException;
    protected abstract List<String> getAvailableTransformations();
    protected abstract List<DataTypeDuckDb> getAvailableOutputTypes();
    protected abstract List<FieldOption> getAvailableOptions();
    protected FieldConfiguration getFieldConfiguration() { return fieldConfiguration; }

    public FieldOption getFieldOption(OptionKey optionKey)
    {
        for(FieldOption fieldOption : getAvailableOptions())
        {
            if(fieldOption.getOptionKey() == optionKey)
            {
                return fieldOption;
            }
        }
        return null;
    }
}
