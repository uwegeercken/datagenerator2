package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

public class DataFieldsProcessor
{
    private DataConfiguration configuration;

    public DataFieldsProcessor(DataConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public void processAllFields() throws InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            FieldProcessor processor = null;
            if (fieldConfiguration.getType() == FieldType.CATEGORY)
            {
                processor = new CategoryProcessor(configuration);

            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMSTRING)
            {
               processor = new RandomStringProcessor(configuration);

            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMINTEGER)
            {
                processor = new RandomIntegerProcessor(configuration);

            }
            else if (fieldConfiguration.getType() == FieldType.REGULAREXPRESSION)
            {
               processor = new RegularExpressionProcessor(configuration);

            }

            processor.setDefaultOptions(fieldConfiguration);
            processor.validateConfiguration(fieldConfiguration);
            processor.processConfiguration(fieldConfiguration);
        }
    }
}
