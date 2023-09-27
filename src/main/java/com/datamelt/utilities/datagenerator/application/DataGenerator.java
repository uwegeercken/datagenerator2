package com.datamelt.utilities.datagenerator.application;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.export.*;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import com.datamelt.utilities.datagenerator.utilities.duckdb.FieldStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static java.lang.System.exit;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private static final String applicationName = "datagenerator2";
    private static final String version = "0.1.1";
    private static final String versionDate = "2023-09-27";
    private static final String contactEmail = "uwe.geercken@web.de";
    private DataConfiguration dataConfiguration;
    private ProgramConfiguration programConfiguration;
    private DataStore dataStore;

    private static ProgramArguments arguments;

    public DataGenerator(ProgramArguments arguments) throws Exception
    {
        programConfiguration = loadProgramConfiguration(arguments.getProgramConfigurationFilename());
        programConfiguration.mergeArguments(arguments);
        dataConfiguration = loadDataConfiguration(arguments.getDataConfigurationFilename());
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
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

            if(generator.programConfiguration.getGeneral().getExportFilename() != null) {
                generator.exportToFile();
            }

            if(arguments.getGenerateStatistics())
            {
                generator.outputStatistics();
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

    private DataConfiguration loadDataConfiguration(String dataConfigurationFilename) throws Exception
    {
        logger.debug("processing data configuration file: [{}],", dataConfigurationFilename);
        InputStream stream = new FileInputStream(new File(dataConfigurationFilename));
        DataConfiguration dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        stream.close();
        return dataConfiguration;
    }

    private ProgramConfiguration loadProgramConfiguration(String programConfigurationFilename) throws Exception
    {
        logger.debug("processing program configuration file: [{}],", programConfigurationFilename);
        InputStream stream = new FileInputStream(new File(programConfigurationFilename));
        ProgramConfiguration programConfiguration = ConfigurationLoader.load(stream.readAllBytes(), ProgramConfiguration.class);
        stream.close();
        return programConfiguration;
    }

    private void processConfiguration() throws InvalidConfigurationException
    {
        DataFieldsProcessor allFieldsProcessor = new DataFieldsProcessor(dataConfiguration);
        allFieldsProcessor.processAllFields();
    }

    private void setupDataStore() throws Exception
    {
        FileExporter fileExporter;
        switch(programConfiguration.getGeneral().getExportType())
        {
            case JSON:
                fileExporter = new JsonFileExporter(programConfiguration.getJsonExport());
                break;
            case PARQUET:
                fileExporter = new ParquetFileExporter(programConfiguration.getParquetExport());
                break;
            case EXCEL:
                fileExporter = new ExcelFileExporter(programConfiguration.getExcelExport());
                break;
            default:
                fileExporter = new CsvFileExporter(programConfiguration.getCsvExport());
                break;
        }
        dataStore = new DataStore(programConfiguration, dataConfiguration, fileExporter);
    }

    private void generateRows() throws Exception
    {
        logger.info("generating rows: [{}]", programConfiguration.getGeneral().getNumberOfRowsToGenerate());
        Row row;
        long counter = 0;
        long start = System.currentTimeMillis();
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        for(long i=0;i < programConfiguration.getGeneral().getNumberOfRowsToGenerate();i++)
        {
            counter++;
            if(programConfiguration.getGeneral().getGeneratedRowsLogInterval() > 0
                    && programConfiguration.getGeneral().getNumberOfRowsToGenerate() > programConfiguration.getGeneral().getGeneratedRowsLogInterval()
                    && counter % programConfiguration.getGeneral().getGeneratedRowsLogInterval() == 0)
            {
                logger.debug("rows generated: [{}]", counter);
            }
            row = rowBuilder.generate();
            dataStore.insert(row, counter);
        }
        dataStore.flush();
        long end = System.currentTimeMillis();
        logger.info("total rows generated: [{}]", counter);
        logger.info("total data generation time: [{}] seconds", (end - start) / 1000);
    }

    private void outputStatistics()
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

    private void exportToFile() throws Exception
    {
        dataStore.exportToFile(dataConfiguration.getTableName(), programConfiguration.getGeneral().getExportFilename());
    }
}
