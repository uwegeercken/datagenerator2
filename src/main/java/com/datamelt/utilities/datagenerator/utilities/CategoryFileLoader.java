package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.DataGenerator;
import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.FieldValue;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.datamelt.utilities.datagenerator.utilities.Constants.CATEGORY_FILE_VALUE_WEIGHT_SEPARATOR;

public class CategoryFileLoader
{
    private static Logger logger = LoggerFactory.getLogger(CategoryFileLoader.class);
    public static void loadCategoryFiles(MainConfiguration configuration)
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

    private static void loadCategoryFile(Field field)
    {
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
                    if (!field.containsFieldValue(valueParts[0].trim()))
                    {
                        if(valueParts.length==1)
                        {
                            field.getValues().add(new FieldValue(valueParts[0], FieldValue.DEFAULT_WEIGHT));
                        }
                        else
                        {
                            field.getValues().add(new FieldValue(valueParts[0], Integer.parseInt(valueParts[0])));
                        }
                        counter++;
                    }
                    else
                    {
                        logger.debug("field [{}] - value [{}] already defined in config. value and weight from the category file is ignored.", field.getName(), valueParts[0]);
                    }
                }
            }
            logger.debug("field [{}] - number of values added from category file: [{}]", field.getName(), counter);
        }
        catch(Exception ex)
        {
            logger.error("field [{}] - error processing category file [{}], error {}", field.getName(), field.getValuesFile(), ex .getMessage());
        }
    }
}
