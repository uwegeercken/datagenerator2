package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public class DataFieldsProcessor
{
    public static void processAllFields(DataConfiguration configuration) throws InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            processField(fieldConfiguration);
        }
    }

    private static void processField(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        FieldProcessor processor = new FieldProcessor(fieldConfiguration);
        processor.setDefaultOptions();
        processor.validateTransformations();
        processor.validateOutputType();
        processor.validateConfiguration();
        processor.processConfiguration();
    }
}