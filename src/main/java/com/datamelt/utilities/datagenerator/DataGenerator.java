package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.utilities.YamlFileProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

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

            System.out.println();

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
        MainConfiguration configuration = mapper.readValue(new File(configurationFilename), MainConfiguration.class);

        CategoryFileLoader.loadCategoryFiles(configuration);
        try
        {
            YamlFileProcessor.isValidConfiguration(configuration);
        }
        catch (InvalidConfigurationException ex)
        {
            logger.error("error processing configuration: {}", ex.getMessage());
        }
        YamlFileProcessor.distributeWeight(configuration.getFields().get(0));
    }


    public MainConfiguration getConfiguration()
    {
        return configuration;
    }
}
