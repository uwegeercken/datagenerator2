package com.datamelt.utilities.datagenerator.application;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.export.*;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.lang.System.exit;

public class DataGenerator
{
    private static Logger logger;
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.5.0";
    private static final String versionDate = "2026-03-14";
    private static final String contactEmail = "uwe.geercken@web.de";
    private DataConfiguration dataConfiguration;
    private ProgramConfiguration programConfiguration;
    private DataStore dataStore;

    public static void main(String[] args)
    {
        CustomLog4jConfig.setupLog4j2Config(parseLoglevel(args));
        logger = LoggerFactory.getLogger(DataGenerator.class);
        logger.info(applicationName +  " application - version: " + version + " (" + versionDate + ")");

        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
        {
            help();
            exit(0);
        }
        try
        {
            ProgramArguments arguments = new ProgramArguments(args);

            DataGenerator dataGenerator = new DataGenerator(arguments.getProgramConfigurationFilename(), arguments.getDataConfigurationFilename());
            dataGenerator.programConfiguration.mergeArguments(arguments);

            logger.info("starting to generate total of [{}] rows", dataGenerator.programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate());
            long runtime = dataGenerator.generateRows();

            long totalRequested = dataGenerator.programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate();
            long totalFailed = dataGenerator.getAppendFailureCount();

            logger.info("total rows generated: [{}], failed: [{}]", totalRequested - totalFailed, totalFailed);            logger.info("total data generation time: [{}] seconds", runtime / 1000d);

            if(dataGenerator.programConfiguration.getGeneralConfiguration().getExportFilename() != null) {
                dataGenerator.exportToFile();
            }
            if(arguments.getGenerateStatistics())
            {
                dataGenerator.outputStatistics();
            }
        }
        catch (InvalidConfigurationException ice)
        {
            logger.error("error in configuration: {}", ice.getMessage());
        }
        catch (Exception ex)
        {
            logger.error("unable to generate data: {}", ex.getMessage());
        }
        logger.info("processing completed");
    }

    public DataGenerator(String programConfigurationFilename, String dataConfigurationFilename) throws IOException, SQLException,InvalidConfigurationException
    {
        logger.debug("processing program configuration file: [{}],", dataConfigurationFilename);
        loadProgramConfiguration(programConfigurationFilename);

        logger.debug("processing data configuration file: [{}],", dataConfigurationFilename);
        loadDataConfiguration(dataConfigurationFilename);

        validateDataConfiguration();
    }

    public long getAppendFailureCount()
    {
        return dataStore.getAppendFailureCount();
    }

    private static void help()
    {
        logger.info("program arguments:");
        Arrays.stream(Argument.values())
                .forEach(argument -> logger.info("argument: {} -> {}. mandatory: {}", argument.getAbbreviation(), argument.getExplanation(), argument.isMandatory()));

        logger.info("contact: {}", contactEmail);
    }

    private void validateDataConfiguration() throws InvalidConfigurationException
    {
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
    }

    private void loadDataConfiguration(String dataConfigurationFilename) throws IOException
    {
        try(InputStream stream = new FileInputStream(dataConfigurationFilename))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);        }
    }

    private void loadProgramConfiguration(String programConfigurationFilename) throws IOException
    {
        try(InputStream stream = new FileInputStream(programConfigurationFilename))
        {
            programConfiguration = ConfigurationLoader.load(stream.readAllBytes(), ProgramConfiguration.class);
        }
    }

    private void setupDataStore() throws SQLException
    {
        FileExporter fileExporter = switch (programConfiguration.getGeneralConfiguration().getExportType())
        {
            case JSON -> new JsonFileExporter(programConfiguration.getJsonExport());
            case PARQUET -> new ParquetFileExporter(programConfiguration.getParquetExport());
            case EXCEL -> new ExcelFileExporter(programConfiguration.getExcelExport());
            default -> new CsvFileExporter(programConfiguration.getCsvExport());
        };
        dataStore = new DataStore(programConfiguration, dataConfiguration, fileExporter);
    }

    private long generateRows() throws NoSuchMethodException, InvalidConfigurationException, SQLException
    {
        setupDataStore();
        long start = System.currentTimeMillis();

        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        AtomicLong appendCounter = new AtomicLong(0);
        LongStream.range(0, programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate())
                .mapToObj(value -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .forEach(row -> {
                    dataStore.insert(row);
                    logOutput("rows generated: [{}]", appendCounter.incrementAndGet());
                });

        dataStore.flush();
        long appendFailures = dataStore.getAppendFailureCount();
        if (appendFailures > 0)
        {
            logger.warn("total rows skipped due to append errors: [{}]", appendFailures);
        }
        return System.currentTimeMillis() - start;
    }

    private void logOutput(String message, long counter)
    {
        if(programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() > 0
                && programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate() > programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval()
                && counter % programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() == 0
                && counter > 0)
        {
            logger.info(message, counter);
        }
    }

    private void outputStatistics()
    {
        logger.info("collecting statistics for generated field values...");
        dataConfiguration.getFields().stream()
                .map(fieldConfiguration -> dataStore.getValueCounts(fieldConfiguration))
                .forEach(fieldStatistics -> logger.info("field: [{}], type: [{}], distinct values: [{}], values and percentages: [{}]", fieldStatistics.getFieldName(), fieldStatistics.getFieldType(), fieldStatistics.getNumberOfDistinctValues(), fieldStatistics.getNumberOfDistinctValues() <= 50 ? fieldStatistics.getFieldStatistics() : "no statistics generated - too many distinct values"));
    }

    private void exportToFile() throws Exception
    {
        dataStore.exportToFile(dataConfiguration.getTableName(), programConfiguration.getGeneralConfiguration().getExportFilename());
    }

    private static Level parseLoglevel(String[] args)
    {
        for (String arg : args)
        {
            if (arg.startsWith(Argument.LOGLEVEL.getAbbreviation()))
            {
                String logLevel = arg.substring(arg.indexOf("=") + 1).trim();
                try
                {
                    return Level.valueOf(logLevel.toUpperCase());
                }
                catch (Exception ex)
                {
                    return Level.INFO;
                }
            }
        }
        return Level.INFO;
    }
}
