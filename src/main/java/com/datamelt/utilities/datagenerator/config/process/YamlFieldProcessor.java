package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

public class YamlFieldProcessor
{
    private DataConfiguration configuration;

    public YamlFieldProcessor(DataConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public void processAllFields() throws InvalidConfigurationException
    {
        for (Field field : configuration.getFields())
        {
            FieldProcessor processor = null;
            if (field.getType() == FieldType.CATEGORY)
            {
                processor = new CategoryProcessor();

            }
            else if (field.getType() == FieldType.RANDOMSTRING)
            {
               processor = new RandomStringProcessor();

            }
            else if (field.getType() == FieldType.REGULAREXPRESSION)
            {
               processor = new RegularExpressionProcessor();

            }

            processor.validateConfiguration(field);
            processor.processConfiguration(field);
        }
    }
}
