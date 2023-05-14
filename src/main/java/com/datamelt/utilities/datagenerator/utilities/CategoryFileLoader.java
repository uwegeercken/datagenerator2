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
                    }
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error processing category file [{}], error [{}]", field.getValuesFile(), ex .getMessage());
        }
    }
}
