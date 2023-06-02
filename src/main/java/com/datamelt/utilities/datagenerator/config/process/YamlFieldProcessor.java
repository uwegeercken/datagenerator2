package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;

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
            if (field.getType().equals("category"))
            {
                processor = new CategoryProcessor();

            }
            else if (field.getType().equals("randomstring"))
            {
               processor = new RandomStringProcessor();

            }
            else if (field.getType().equals("regex"))
            {
               processor = new RegularExpressionProcessor();

            }

            processor.validateConfiguration(field);
            processor.processConfiguration(field);
        }
    }
}
