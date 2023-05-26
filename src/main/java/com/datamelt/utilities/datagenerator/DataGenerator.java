package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.model.MainConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.YamlFieldProcessor;
import com.datamelt.utilities.datagenerator.generate.CategoryGenerator;
import com.datamelt.utilities.datagenerator.generate.RandomValueGenerator;
import com.datamelt.utilities.datagenerator.utilities.*;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private MainConfiguration configuration;
    private long numberOfRowsToGenerate=0;
    private DataStore dataStore;

    public DataGenerator(long numberOfRowsToGenerate)
    {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;

    }
    public static void main(String[] args) throws Exception
    {
        if(args!=null && args.length>1)
        {
            long start = System.currentTimeMillis();
            DataGenerator generator = new DataGenerator(Long.parseLong(args[1]));
            generator.loadConfiguration(args[0]);
            generator.processConfiguration();
            generator.dataStore = new DataStore(generator.configuration);

            logger.debug("generating rows: [{}],", generator.numberOfRowsToGenerate);
            long counter = 0;
            RandomValueGenerator valueGenerator = new CategoryGenerator();
            for(long i=0;i < generator.numberOfRowsToGenerate;i++)
            {
                counter++;
                if(counter % 25000 == 0)
                {
                    logger.debug("rows generated: [{}],", counter);
                }
                Row row = valueGenerator.generateRandomValues(generator.configuration);
                generator.dataStore.insert(row);
            }
            generator.dataStore.flush();

            logger.debug("total rows generated: [{}],", counter);
            long end = System.currentTimeMillis();
            logger.info("total processing time: [{}] seconds", (end-start)/1000);
        }
        else
        {
            logger.error("the path and name of the configuration yaml file needs to be specified");
        }
    }

    private void loadConfiguration(String configurationFilename) throws Exception
    {
        logger.debug("processing configuration file: [{}],", configurationFilename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        configuration = mapper.readValue(new File(configurationFilename), MainConfiguration.class);
        CategoryFileLoader.loadCategoryFiles(configuration);
    }

    private void processConfiguration() throws InvalidConfigurationException
    {
        YamlFieldProcessor allFieldsProcessor = new YamlFieldProcessor(configuration);
        allFieldsProcessor.processAllFields();
    }
}
