package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

public class DataFieldsProcessor
{
    public void processAllFields(DataConfiguration configuration) throws InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            FieldProcessor processor = null;
            if (fieldConfiguration.getType() == FieldType.CATEGORY)
            {
                processor = new CategoryProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMSTRING)
            {
               processor = new RandomStringProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMINTEGER)
            {
                processor = new RandomLongProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMLONG)
            {
                processor = new RandomLongProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMDOUBLE)
            {
                processor = new RandomDoubleProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.REGULAREXPRESSION)
            {
               //processor = new RegularExpressionProcessor(fieldConfiguration);

            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMDATE)
            {
                processor = new RandomDateProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMTIMESTAMP)
            {
                processor = new RandomTimestampProcessor(fieldConfiguration);
            }
            else if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
            {
                processor = new DateReferenceProcessor(fieldConfiguration);
            }

            processor.setDefaultOptions();
            processor.validateTransformations();
            processor.validateOutputType();
            processor.validateConfiguration();
            processor.processConfiguration();
        }
    }
}
