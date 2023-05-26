package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.FieldValue;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.model.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.datamelt.utilities.datagenerator.utilities.Constants.CATEGORY_FILE_VALUE_WEIGHT_SEPARATOR;

public class CategoryFileLoader
{
    private static Logger logger = LoggerFactory.getLogger(CategoryFileLoader.class);
    public static void loadCategoryFiles(MainConfiguration configuration) throws Exception
    {
        for(Field field : configuration.getFields())
        {
            if(field.getValuesFile()!=null)
            {
                if(field.getSumOfWeights()<100)
                {
                    logger.debug("field [{}] - processing values from the category file [{}]", field.getName(), field.getValuesFile());
                    loadCategoryFile(field);
                }
                else
                {
                    logger.warn("field [{}] - total sum of weight values in the configuration is 100. the configured category file [{}] is ignored.", field.getName(), field.getValuesFile());
                }
            }
        }
    }

    private static void loadCategoryFile(Field field) throws InvalidConfigurationException {
        DataTypeJava dataType = DataTypeJava.valueOf(field.getDataType().toUpperCase());
        File file = new File(field.getValuesFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));)
        {
            String value;
            int counter = 0;
            while ((value = reader.readLine()) != null)
            {
                if (value != null && value.trim().length() > 0 && !value.trim().startsWith("#"))
                {
                    String[] valueParts = value.split(CATEGORY_FILE_VALUE_WEIGHT_SEPARATOR);
                    FieldValue fieldValue;
                    if(valueParts.length==1)
                    {
                        fieldValue = getFieldValue(valueParts[0], dataType, FieldValue.DEFAULT_WEIGHT);
                    }
                    else
                    {
                        fieldValue = getFieldValue(valueParts[0], dataType, Integer.parseInt(valueParts[1]));
                    }

                    if (!field.containsFieldValue(fieldValue))
                    {
                        field.getValues().add(fieldValue);
                        counter++;
                    }
                    else
                    {
                        logger.debug("field [{}] - value [{}] already defined in config. weight from the category file is ignored.", field.getName(), valueParts[0]);
                    }
                }
            }
            logger.debug("field [{}] - number of values added from category file: [{}]", field.getName(), counter);
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + field.getName() + "] - error processing category file: " +  ex.getMessage());        }
    }

    private static FieldValue getFieldValue(String value, DataTypeJava dataType, int weight)
    {
        switch(dataType)
        {
            case INTEGER:
                return new FieldValue(Integer.parseInt(value), weight);
            default:
                return new FieldValue(value, weight);
        }
    }
}
