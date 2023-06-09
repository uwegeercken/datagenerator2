package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.Argument;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.ProgramArguments;
import com.datamelt.utilities.datagenerator.config.model.ProgramConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
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

import static java.lang.System.exit;

public class DataGenerator
{
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.0.6";
    private static final String versionDate = "2023-06-09";
    private static final String contactEmail = "uwe.geercken@web.de";
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
        logger.info(applicationName +  " application - version: " + version + " (" + versionDate + ")");
        DataGenerator generator = null;
        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
        {
            help();
            exit(0);
        }
        try
        {
            arguments = new ProgramArguments(args);
            generator = new DataGenerator(arguments);
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

    private static void help()
    {
        logger.info("program arguments:");
        for(Argument argument : Argument.values())
        {
            logger.info("argument: {} -> {}", argument.getAbbreviation(), argument.getExplanation());
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
        DataFieldsProcessor allFieldsProcessor = new DataFieldsProcessor(dataConfiguration);
        allFieldsProcessor.processAllFields();
    }

    private void setupDataStore() throws Exception
    {
        dataStore = new DataStore(dataConfiguration, new CsvFileExporter(",",true));
    }

    private void generateRows() throws Exception
    {
        logger.info("generating rows: [{}]", programConfiguration.getNumberOfRowsToGenerate());
        Row row;
        long counter = 0;
        long start = System.currentTimeMillis();
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        for(long i=0;i < programConfiguration.getNumberOfRowsToGenerate();i++)
        {
            counter++;
            if(programConfiguration.getGeneratedRowsLogInterval() > 0
                    && programConfiguration.getNumberOfRowsToGenerate() > programConfiguration.getGeneratedRowsLogInterval()
                    && counter % programConfiguration.getGeneratedRowsLogInterval() == 0)
            {
                logger.debug("rows generated: [{}]", counter);
            }
            row = rowBuilder.generate(dataConfiguration);
            dataStore.insert(row);
        }
        dataStore.flush();
        long end = System.currentTimeMillis();
        logger.info("total rows generated: [{}]", counter);
        logger.info("total data generation time: [{}] seconds", (end - start) / 1000);
    }

    private void exportToFile(String tablename, String outputFilename) throws Exception
    {
        logger.info("output of generated data to: [{}],", outputFilename);
        dataStore.exportToFile(tablename, outputFilename);
    }
}
