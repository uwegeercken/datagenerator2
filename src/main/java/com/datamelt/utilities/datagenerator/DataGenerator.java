package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.ProgramArguments;
import com.datamelt.utilities.datagenerator.config.model.ProgramConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.YamlFieldProcessor;
import com.datamelt.utilities.datagenerator.export.CsvFileExporter;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private DataConfiguration dataConfiguration;
    private ProgramConfiguration programConfiguration;
    private DataStore dataStore;

    private static ProgramArguments arguments;

    public DataGenerator(ProgramArguments arguments) throws Exception
    {
        programConfiguration = loadProgramConfiguration(arguments.getProgramConfigurationFilename());
        programConfiguration.mergeArguments(arguments);
        dataConfiguration = loadDataConfiguration(arguments.getDataConfigurationFilename());
        processConfiguration();
        setupDataStore();
    }
    public static void main(String[] args) throws Exception
    {
        try
        {
            arguments = new ProgramArguments(args);
            DataGenerator generator = new DataGenerator(arguments);
            generator.generateRows();
            if(generator.programConfiguration.getOutputFilename() != null) {
                generator.exportToFile(generator.dataConfiguration.getTableName(), generator.programConfiguration.getOutputFilename());
            }
        }
        catch (Exception ex)
        {
            logger.error("unable to generate data: {}", ex.getMessage());
        }
    }

    private DataConfiguration loadDataConfiguration(String dataConfigurationFilename) throws Exception
    {
        logger.debug("processing datagenerator configuration file: [{}],", dataConfigurationFilename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        DataConfiguration dataConfiguration = mapper.readValue(new File(dataConfigurationFilename), DataConfiguration.class);
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        return dataConfiguration;
    }

    private ProgramConfiguration loadProgramConfiguration(String programConfigurationFilename) throws Exception
    {
        logger.debug("processing program configuration file: [{}],", programConfigurationFilename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return mapper.readValue(new File(programConfigurationFilename), ProgramConfiguration.class);
    }

    private void processConfiguration() throws InvalidConfigurationException
    {
        YamlFieldProcessor allFieldsProcessor = new YamlFieldProcessor(dataConfiguration);
        allFieldsProcessor.processAllFields();
    }

    private void setupDataStore() throws Exception
    {
        dataStore = new DataStore(dataConfiguration, new CsvFileExporter(",",true));
    }

    private void generateRows() throws Exception
    {
        logger.debug("generating rows: [{}],", programConfiguration.getNumberOfRowsToGenerate());
        Row row;
        long counter = 0;
        long start = System.currentTimeMillis();
        for(long i=0;i < programConfiguration.getNumberOfRowsToGenerate();i++)
        {
            counter++;
            if(counter % 25000 == 0)
            {
                logger.debug("rows generated: [{}],", counter);
            }
            row = RowBuilder.generate(dataConfiguration);
            dataStore.insert(row);
        }
        dataStore.flush();
        long end = System.currentTimeMillis();
        logger.debug("total rows generated: [{}],", counter);
        logger.info("total data generation time: [{}] seconds", (end - start) / 1000);
    }

    private void exportToFile(String tablename, String outputFilename) throws Exception
    {
        logger.debug("output of generated data to: [{}],", outputFilename);
        dataStore.exportToFile(tablename, outputFilename);
    }
}
