package com.datamelt.utilities.datagenerator.config;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.datamelt.utilities.datagenerator.utilities.Constants.CATEGORY_FILE_VALUE_WEIGHT_SEPARATOR;

public class CategoryFileLoader
{
    private static Logger logger = LoggerFactory.getLogger(CategoryFileLoader.class);
    public static void loadCategoryFiles(DataConfiguration configuration) throws Exception
    {
        for(FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            if(fieldConfiguration.getValuesFile()!=null)
            {
                if(fieldConfiguration.getSumOfWeights()<100)
                {
                    logger.debug("field [{}] - processing values from the category file [{}]", fieldConfiguration.getName(), fieldConfiguration.getValuesFile());
                    loadCategoryFile(fieldConfiguration);
                }
                else
                {
                    logger.warn("field [{}] - total sum of weight values in the configuration is 100. the configured category file [{}] is ignored.", fieldConfiguration.getName(), fieldConfiguration.getValuesFile());
                }
            }
        }
    }

    private static void loadCategoryFile(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {
        File file = new File(fieldConfiguration.getValuesFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));)
        {
            String value;
            int counter = 0;
            while ((value = reader.readLine()) != null)
            {
                if (value != null && value.trim().length() > 0 && !value.trim().startsWith("#"))
                {
                    String[] valueParts = value.split(CATEGORY_FILE_VALUE_WEIGHT_SEPARATOR);
                    FieldConfigurationValue fieldConfigurationValue;
                    if(valueParts.length==1)
                    {
                        fieldConfigurationValue = new FieldConfigurationValue(valueParts[0], FieldConfigurationValue.DEFAULT_WEIGHT);
                    }
                    else
                    {
                        fieldConfigurationValue = new FieldConfigurationValue(valueParts[0], Integer.parseInt(valueParts[1]));
                    }
                    if (!fieldConfiguration.containsFieldValue(fieldConfigurationValue))
                    {
                        fieldConfiguration.getValues().add(fieldConfigurationValue);
                        counter++;
                    }
                    else
                    {
                        logger.debug("field [{}] - value [{}] already defined in config. weight from the category file is ignored.", fieldConfiguration.getName(), valueParts[0]);
                    }
                }
            }
            logger.debug("field [{}] - number of values added from category file: [{}]", fieldConfiguration.getName(), counter);
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - error processing category file: " +  ex.getMessage());
        }
    }
}
