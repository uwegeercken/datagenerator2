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
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.lang.System.exit;

public class DataGenerator
{
    private static Logger logger; // = LoggerFactory.getLogger(DataGenerator.class);
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.3.2";
    private static final String versionDate = "2025-03-22";
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

            logger.info("starting to generate total of [{}] rows, number of threads [{}], rows per thread [{}]", programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate(), programConfiguration.getGeneralConfiguration().getNumberOfThreads(), programConfiguration.getGeneralConfiguration().getNumberOfRowsPerThread());
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
        Arrays.stream(Argument.values())
                .forEach(argument -> logger.info("argument: {} -> {}. mandatory: {}", argument.getAbbreviation(), argument.getExplanation(), argument.isMandatory()));

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

    private static long generateRows() throws NoSuchMethodException, InvalidConfigurationException, SQLException, ExecutionException, InterruptedException
    {
        try(ExecutorService executorService = Executors.newFixedThreadPool(programConfiguration.getGeneralConfiguration().getNumberOfThreads()))
        {
            CompletionService<List<Row>> completionService = new ExecutorCompletionService<>(executorService);

            long start = System.currentTimeMillis();
            RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

            List<Integer> partitons = getPartitions(programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate(), programConfiguration.getGeneralConfiguration().getNumberOfRowsPerThread());
            for (int i = 0; i < partitons.size(); i++)
            {
                int partitionBatchSize = partitons.get(i);
                completionService.submit(() -> generateRowsBatch(rowBuilder, partitionBatchSize));
            }

            long appendCounter = 0;
            for (int i = 0; i < partitons.size(); i++)
            {
                Future<List<Row>> futureRows = completionService.take();
                futureRows.get().forEach(row -> dataStore.insert(row));
                appendCounter += partitons.get(i);
                logOutput("rows generated: [{}]", appendCounter);
            }
            dataStore.flush();
            return System.currentTimeMillis() - start;
        }
    }

    private static List<Row> generateRowsBatch(RowBuilder rowBuilder, int partitionBatchSize)
    {
        return IntStream.range(0, partitionBatchSize)
                .mapToObj(rangeValue -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .toList();
    }

    private static List<Integer> getPartitions(long numberOfRows, int batchSize)
    {
        int numberOfBatches = (int) (numberOfRows / batchSize);
        List<Integer> partitions = new ArrayList<>();
        IntStream.range(0,numberOfBatches).forEach(i -> partitions.add(batchSize));

        int remainingNumberOfRows = (int) (programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate() - numberOfBatches * batchSize);
        if(remainingNumberOfRows>0)
        {
            partitions.add(remainingNumberOfRows);
        }
        return partitions;
    }

    private static void logOutput(String message, long counter)
    {
        if(programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() > 0
                && programConfiguration.getGeneralConfiguration().getNumberOfRowsToGenerate() > programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval()
                && counter % programConfiguration.getGeneralConfiguration().getGeneratedRowsLogInterval() == 0
                && counter > 0)
        {
            logger.debug(message, counter);
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
        dataConfiguration.getFields().stream()
                .map(fieldConfiguration -> dataStore.getValueCounts(fieldConfiguration))
                .forEach(fieldStatistics -> logger.info("field: [{}], type: [{}], distinct values: [{}], values and percentages: [{}]", fieldStatistics.getFieldName(), fieldStatistics.getFieldType(), fieldStatistics.getNumberOfDistinctValues(), fieldStatistics.getNumberOfDistinctValues() <= 50 ? fieldStatistics.getFieldStatistics() : "no statistics generated - too many distinct values"));
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
