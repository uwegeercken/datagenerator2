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

public class CategoryFileLoader
{
    private static Logger logger = LoggerFactory.getLogger(CategoryFileLoader.class);
    public static void loadCategoryFiles(MainConfiguration configuration)
    {
        for(Field field : configuration.getFields())
        {
            if(field.getValuesFile()!=null)
            {
                loadCategoryFile( field);
            }
        }
    }

    private static void loadCategoryFile(Field field)
    {
        File file = new File(field.getValuesFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));)
        {
            String value;
            while ((value = reader.readLine()) != null)
            {


                if (value != null && value.trim().length() > 0 && !value.trim().startsWith("#"))
                {
                    if (!field.containsFieldValue(value.trim()))
                    {
                        field.getValues().add(composeFieldValue(value));
                    }
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error processing category file [{}], error [{}]", field.getValuesFile(), ex .getMessage());
        }
    }

    private static FieldValue composeFieldValue(String value)
    {
        String[] valueParts = value.split("#");
        if(valueParts.length==1)
        {
            return new FieldValue(value, FieldValue.DEFAULT_WEIGHT);
        }
        else
        {
            return new FieldValue(valueParts[0], Integer.parseInt(valueParts[1]));
        }
    }
}
