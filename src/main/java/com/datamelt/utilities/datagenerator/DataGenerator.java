package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.FieldValue;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    private MainConfiguration configuration;
    public static void main(String[] args)
    {
        DataGenerator generator = new DataGenerator();
        try
        {
            generator.loadConfiguration(args[0]);
            for(Field field : generator.getConfiguration().getFields())
            {
                if(field.getValuesFile()!=null)
                {
                    generator.loadCategoryFile( field);
                }
            }

        }
        catch(Exception ex)
        {
            logger.error("exception processing configuration file: [{}], error: [{}]", args[0], ex.getMessage());
        }

        System.out.println();
    }

    private void loadConfiguration(String configurationFilename) throws Exception
    {
        logger.debug("processing configuration file: [{}],", configurationFilename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        this.configuration = mapper.readValue(new File(configurationFilename), MainConfiguration.class);
    }

    private void loadCategoryFile(Field field)
    {
        File file = new File(field.getValuesFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));)
        {
            String value;
            while ((value = reader.readLine()) != null)
            {
                if (value != null && value.trim().length() > 0 && !value.trim().startsWith("#"))
                {
         xxxx           if (!field.getValues().contains(value.trim()))
                    {
                        field.getValues().add(new FieldValue(value.trim(), FieldValue.DEFAULT_WEIGHT));
                    }
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error processing category file [{}], error [{}]", field.getValuesFile(), ex .getMessage());
        }
    }

    public MainConfiguration getConfiguration()
    {
        return configuration;
    }
}
