package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.MainConfiguration;
import com.datamelt.utilities.datagenerator.config.model.ProgramArguments;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.YamlFieldProcessor;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.*;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.List;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private MainConfiguration configuration;
    private long numberOfRowsToGenerate=0;
    private String configurationFilename;
    private DataStore dataStore;
    private static ProgramArguments arguments;

    public DataGenerator(String configurationFilename, long numberOfRowsToGenerate)
    {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;
        this.configurationFilename = configurationFilename;

    }
    public static void main(String[] args) throws Exception
    {
        arguments = new ProgramArguments(args);
        if(args!=null && args.length>1)
        {
            try
            {
                DataGenerator generator = new DataGenerator(arguments.getConfigurationFilename(), arguments.getNumberOfRowsToGenerate());
                generator.loadConfiguration();
                generator.processConfiguration();
                generator.dataStore = new DataStore(generator.configuration);
                generator.generateRows(generator.configuration.getFields(), arguments.getNumberOfRowsToGenerate());
                generator.exportToFile(generator.configuration.getTableName(), arguments.getOutputFilename());
            }
            catch (Exception ex)
            {
                logger.error("unable to generate data: {}", ex.getMessage());
            }
        }
        else
        {
            logger.error("the path and name of the configuration yaml file needs to be specified");
        }
    }

    private void loadConfiguration() throws Exception
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

    private void generateRows(List<Field> fields, long numberOfRowsToGenerate) throws Exception
    {
        logger.debug("generating rows: [{}],", numberOfRowsToGenerate);
        Row row;
        long counter = 0;
        long start = System.currentTimeMillis();
        for(long i=0;i < numberOfRowsToGenerate;i++)
        {
            counter++;
            if(counter % 25000 == 0)
            {
                logger.debug("rows generated: [{}],", counter);
            }
            row = RowBuilder.generate(fields);
            dataStore.insert(row);
        }
        dataStore.flush();
        long end = System.currentTimeMillis();
        logger.debug("total rows generated: [{}],", counter);
        logger.info("total data generation time: [{}] seconds", (end - start) / 1000);
    }

    private void exportToFile(String tablename, String outputFilename) throws Exception
    {
        logger.debug("output generated data to csv: [{}],", arguments.getOutputFilename());
        dataStore.exportToCsv(tablename, outputFilename);
    }
}
