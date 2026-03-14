package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.FieldOption;
import com.datamelt.utilities.datagenerator.config.model.options.GenericOptions;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FieldProcessor
{
    private final FieldConfiguration fieldConfiguration;

    public FieldProcessor(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
    }

    public void setDefaultOptions()
    {
        for (FieldOption availableOption : fieldConfiguration.getType().getAvailableOptions())
        {
            if (!fieldConfiguration.getOptions().containsKey(availableOption.getOptionKey().getKey()))
            {
                if (availableOption.getDefaultValue() != null)
                {
                    fieldConfiguration.getOptions().put(availableOption.getOptionKey().getKey(), availableOption.getDefaultValue());
                }
            }
        }
    }

    public void validateTransformations() throws InvalidConfigurationException
    {
        if (fieldConfiguration.getTransformations() != null)
        {
            for (TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                if (!fieldConfiguration.getType().getAvailableTransformationNames().contains(configuredTransformation.getName()))
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is not available - must be one of: " + Arrays.toString(fieldConfiguration.getType().getAvailableTransformationNames().toArray()));
                }
            }
        }
    }

    public void validateOutputType() throws InvalidConfigurationException
    {
        Object outputTypeValue = fieldConfiguration.getOptions().get(GenericOptions.OUTPUT_TYPE.getKey());
        if (outputTypeValue != null)
        {
            DataTypeDuckDb dataTypeDuckDb = DataTypeDuckDb.getDataType((String) outputTypeValue);
            if (dataTypeDuckDb == null)
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + GenericOptions.OUTPUT_TYPE.getKey() + "] - the provided value [" + outputTypeValue + "] is invalid");
            }
            if (!fieldConfiguration.getType().getAvailableOutputTypes().contains(dataTypeDuckDb))
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], output type [" + dataTypeDuckDb + "] is not available - must be one of: " + Arrays.toString(fieldConfiguration.getType().getAvailableOutputTypes().toArray()));
            }
        }
    }

    public void validateConfiguration() throws InvalidConfigurationException
    {
        // collect all per-option validation failures
        List<String> failures = fieldConfiguration.getType().getAvailableOptions().stream()
                .map(option -> {
                    Object value = fieldConfiguration.getOptions().get(option.getOptionKey().getKey());
                    return Try.of(() -> {
                        option.validate(fieldConfiguration.getName(), value);
                        return null;
                    });
                })
                .filter(Try::isFailure)
                .map(t -> t.getError().getMessage())
                .collect(Collectors.toList());

        if (!failures.isEmpty())
        {
            throw new InvalidConfigurationException(
                    "field [" + fieldConfiguration.getName() + "] has " + failures.size() + " configuration error(s):\n" +
                            String.join("\n", failures));
        }

        // cross-option validation runs only when all individual options passed
        if (fieldConfiguration.getType().getCrossOptionValidator() != null)
        {
            try
            {
                fieldConfiguration.getType().getCrossOptionValidator().validate(fieldConfiguration);
            }
            catch (ClassCastException cce)
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - invalid option type: " + cce.getMessage());
            }
        }
    }

    public void processConfiguration() throws InvalidConfigurationException
    {
        if (fieldConfiguration.getType().getPostProcessor() != null)
        {
            fieldConfiguration.getType().getPostProcessor().process(fieldConfiguration);
        }
    }

    public FieldOption getFieldOption(OptionKey optionKey)
    {
        for (FieldOption fieldOption : fieldConfiguration.getType().getAvailableOptions())
        {
            if (fieldOption.getOptionKey() == optionKey)
            {
                return fieldOption;
            }
        }
        return null;
    }

    public FieldConfiguration getFieldConfiguration()
    {
        return fieldConfiguration;
    }
}