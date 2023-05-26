package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.MainConfiguration;

public class YamlFieldProcessor
{
    private MainConfiguration configuration;

    public YamlFieldProcessor(MainConfiguration configuration)
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
                processor = new CategoryFieldProcessor();

            }
            else if (field.getType().equals("random"))
            {
               processor = new RandomFieldProcessor();

            }
            else if (field.getType().equals("regex"))
            {
               processor = new RegularExpressionFieldProcessor();

            }

            processor.validateConfiguration(field);
            processor.processConfiguration(field);
        }
    }
}
