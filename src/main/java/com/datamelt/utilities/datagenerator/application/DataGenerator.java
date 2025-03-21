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
import com.datamelt.utilities.datagenerator.utilities.duckdb.FieldStatistics;
import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.LongStream;

import static java.lang.System.exit;

public class DataGenerator
{
    private static Logger logger; // = LoggerFactory.getLogger(DataGenerator.class);
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.3.1";
    private static final String versionDate = "2025-03-21";
    private static final String contactEmail = "uwe.geercken@web.de";
    private static DataConfiguration dataConfiguration;
    private static ProgramConfiguration programConfiguration;
    private static DataStore dataStore;
    private static ProgramArguments arguments;

    public static void main(String[] args)
    {
        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
        {
            help();
            exit(0);
        }
        try
        {
            CustomLog4jConfig.setupLog4j2Config(parseLoglevel(args));
            logger = LoggerFactory.getLogger(DataGenerator.class);
            logger.info(applicationName +  " application - version: " + version + " (" + versionDate + ")");

            arguments = new ProgramArguments(args);
            //logger.debug("processing program configuration file: [{}],", programConfigurationFilename);
            loadProgramConfiguration(arguments.getProgramConfigurationFilename());
            programConfiguration.mergeArguments(arguments);

            logger.debug("processing data configuration file: [{}],", arguments.getDataConfigurationFilename());
            processDataConfiguration(arguments.getDataConfigurationFilename());
            setupDataStore();

            logger.info("starting to generate total of [{}] rows", programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate());
            long runtime = generateRows();
            logger.info("total rows generated: [{}]", programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate());
            logger.info("total data generation time: [{}] seconds", runtime / 1000d);


            if(programConfiguration.getGeneralConfiguration().getExportFilename() != null) {
                exportToFile();
            }
            if(arguments.getGenerateStatistics())
            {
                outputStatistics();
            }
        }
        catch (InvalidConfigurationException ice)
        {
            logger.error("error in configuration: {}", ice.getMessage());
        }
        catch (NoSuchMethodException nsm)
        {
            logger.error("error in transformation: {}", nsm.getMessage());
        }
        catch (Exception ex)
        {
            logger.error("unable to generate data: {}", ex.getMessage());
        }
        logger.info("processing completed");
    }

    private static void help()
    {
        logger.info("program arguments:");
        for(Argument argument : Argument.values())
        {
            logger.info("argument: {} -> {}. mandatory: {}", argument.getAbbreviation(), argument.getExplanation(), argument.isMandatory());
        }
        logger.info("contact: {}", contactEmail);
    }

    private static void processDataConfiguration(String dataConfigurationFilename) throws IOException, InvalidConfigurationException
    {
        loadDataConfiguration(dataConfigurationFilename);
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
    }

    private static void loadDataConfiguration(String dataConfigurationFilename) throws IOException
    {
        try(InputStream stream = new FileInputStream(dataConfigurationFilename))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
    }

    private static void loadProgramConfiguration(String programConfigurationFilename) throws IOException
    {
        try(InputStream stream = new FileInputStream(programConfigurationFilename))
        {
            programConfiguration = ConfigurationLoader.load(stream.readAllBytes(), ProgramConfiguration.class);
        }
    }

    private static void setupDataStore() throws Exception
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

    private static long generateRows() throws NoSuchMethodException, InvalidConfigurationException, SQLException
    {

        long start = System.currentTimeMillis();
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        LongStream.range(0, programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate())
                .peek(DataGenerator::logProcessedRows)
                .mapToObj(rangeValue -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .forEach(rowTry -> dataStore.insert(rowTry.getResult()));

        dataStore.flush();
        return System.currentTimeMillis() - start;
    }

    private static void logProcessedRows(long counter)
    {
        if(programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() > 0
                && programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate() > programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval()
                && counter % programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() == 0
                && counter > 0)
        {
            logger.debug("rows generated: [{}]", counter);
        }
    }

    public static List<Row> generateRows(String dataConfigurationFilename, long numberOfRows) throws IOException, InvalidConfigurationException, NoSuchMethodException
    {
        processDataConfiguration(dataConfigurationFilename);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        return LongStream.range(0, numberOfRows)
                .mapToObj(rangeValue -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .toList();
    }

    public static Try<Row> generateRow(String dataConfigurationFilename) throws IOException, InvalidConfigurationException, NoSuchMethodException
    {
        processDataConfiguration(dataConfigurationFilename);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        return rowBuilder.generate();
    }

    private static void outputStatistics()
    {
        logger.info("collecting statistics for generated field values...");
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            FieldStatistics fieldStatistics = dataStore.getValueCounts(fieldConfiguration);
            if(fieldStatistics.getNumberOfDistinctValues()<= 50)
            {
                logger.info("field: [{}], type: [{}], distinct values: [{}], values and percentages: [{}]", fieldConfiguration.getName(), fieldConfiguration.getType(), fieldStatistics.getNumberOfDistinctValues(), fieldStatistics.getFieldStatistics());
            }
            else
            {
                logger.info("field: [{}], type: [{}], distinct values: [{}], values and percentages: [{}]", fieldConfiguration.getName(), fieldConfiguration.getType(), fieldStatistics.getNumberOfDistinctValues(), "no statistics generated - too many distinct values");
            }
        }
    }

    private static void exportToFile() throws Exception
    {
        dataStore.exportToFile(dataConfiguration.getTableName(), programConfiguration.getGeneralConfiguration().getExportFilename());
    }

    private static Level parseLoglevel(String[] args)
    {
        for(int i=0;i<args.length;i++)
        {
            if (args[i].startsWith(Argument.LOGLEVEL.getAbbreviation()))
            {
                String logLevel = args[i].substring(args[i].indexOf("=") + 1).trim();
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
