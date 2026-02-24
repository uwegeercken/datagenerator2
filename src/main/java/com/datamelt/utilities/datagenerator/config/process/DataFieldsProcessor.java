package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataFieldsProcessor
{
    public static void processAllFields(DataConfiguration configuration) throws InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            FieldProcessor processor = fieldConfiguration.getType().getFieldProcessorFunction().apply(fieldConfiguration);
            processor.setDefaultOptions();
            processor.validateTransformations();
            processor.validateOutputType();
            processor.validateConfiguration();
            processor.processConfiguration();
        }
    }
}
