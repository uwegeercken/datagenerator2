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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static java.lang.System.exit;

public class DataGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.2.6";
    private static final String versionDate = "2024-03-11";
    private static final String contactEmail = "uwe.geercken@web.de";
    private static DataConfiguration dataConfiguration;
    private static ProgramConfiguration programConfiguration;
    private static DataStore dataStore;
    private static ProgramArguments arguments;

    private static void processDataConfiguration(String dataConfigurationFilename) throws IOException, InvalidConfigurationException
    {
        loadDataConfiguration(dataConfigurationFilename);
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
    }

    public static void main(String[] args)
    {
        logger.info(applicationName +  " application - version: " + version + " (" + versionDate + ")");
        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
        {
            help();
            exit(0);
        }
        try
        {
            arguments = new ProgramArguments(args);
            loadProgramConfiguration(arguments.getProgramConfigurationFilename());
            programConfiguration.mergeArguments(arguments);

            processDataConfiguration(arguments.getDataConfigurationFilename());
            setupDataStore();
            generateRows();

            if(programConfiguration.getGeneral().getExportFilename() != null) {
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

    private static void loadDataConfiguration(String dataConfigurationFilename) throws IOException
    {
        logger.debug("processing data configuration file: [{}],", dataConfigurationFilename);
        InputStream stream = new FileInputStream(dataConfigurationFilename);
        dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        stream.close();
    }

    private static void loadProgramConfiguration(String programConfigurationFilename) throws IOException
    {
        logger.debug("processing program configuration file: [{}],", programConfigurationFilename);
        InputStream stream = new FileInputStream(programConfigurationFilename);
        programConfiguration = ConfigurationLoader.load(stream.readAllBytes(), ProgramConfiguration.class);
        stream.close();
    }

    private static void setupDataStore() throws Exception
    {
        FileExporter fileExporter = switch (programConfiguration.getGeneral().getExportType())
        {
            case JSON -> new JsonFileExporter(programConfiguration.getJsonExport());
            case PARQUET -> new ParquetFileExporter(programConfiguration.getParquetExport());
            case EXCEL -> new ExcelFileExporter(programConfiguration.getExcelExport());
            default -> new CsvFileExporter(programConfiguration.getCsvExport());
        };
        dataStore = new DataStore(programConfiguration, dataConfiguration, fileExporter);
    }

    private static void generateRows() throws NoSuchMethodException, InvalidConfigurationException, SQLException
    {
        logger.info("starting to generate total of [{}] rows", programConfiguration.getGeneral().getNumberOfRowsToGenerate());
        long start = System.currentTimeMillis();
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        LongStream.range(0, programConfiguration.getGeneral().getNumberOfRowsToGenerate())
                .peek(DataGenerator::logProcessedRows)
                .mapToObj(rangeValue -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .forEach(rowTry -> dataStore.insert(rowTry.getResult()));

        dataStore.flush();
        long end = System.currentTimeMillis();
        logger.info("total rows generated: [{}]", programConfiguration.getGeneral().getNumberOfRowsToGenerate());
        logger.info("total data generation time: [{}] seconds", (end - start) / 1000);
    }

    private static void logProcessedRows(long counter)
    {
        if(programConfiguration.getGeneral().getGeneratedRowsLogInterval() > 0
                && programConfiguration.getGeneral().getNumberOfRowsToGenerate() > programConfiguration.getGeneral().getGeneratedRowsLogInterval()
                && counter % programConfiguration.getGeneral().getGeneratedRowsLogInterval() == 0
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
                .map(rowTry -> rowTry.getResult())
                .toList();
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
        dataStore.exportToFile(dataConfiguration.getTableName(), programConfiguration.getGeneral().getExportFilename());
    }
}
