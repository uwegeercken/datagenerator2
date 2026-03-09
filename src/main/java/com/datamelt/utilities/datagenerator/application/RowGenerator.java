package com.datamelt.utilities.datagenerator.application;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class RowGenerator
{
    private final DataConfiguration dataConfiguration;

    public RowGenerator(String dataConfigurationFilename) throws IOException, InvalidConfigurationException
    {
        dataConfiguration = loadDataConfiguration(dataConfigurationFilename);
        validateDataConfiguration();
    }

    /**
     * Returns a lazy infinite stream of rows. The caller controls termination
     * via limit(), takeWhile(), or any other stream operation.
     * Each element is wrapped in a Try — filter on Try::isSuccess to get successful rows only.
     */
    public Stream<Try<Row>> generateRows() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        return Stream.generate(rowBuilder::generate);
    }

    /**
     * Convenience method returning a bounded stream of exactly numberOfRows rows.
     */
    public Stream<Try<Row>> generateRows(long numberOfRows) throws InvalidConfigurationException
    {
        return generateRows().limit(numberOfRows);
    }

    /**
     * Generates a single row.
     */
    public Try<Row> generateRow() throws InvalidConfigurationException
    {
        return new RowBuilder(dataConfiguration).generate();
    }

    private DataConfiguration loadDataConfiguration(String dataConfigurationFilename) throws IOException
    {
        try (InputStream stream = new FileInputStream(dataConfigurationFilename))
        {
            return ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
    }

    private void validateDataConfiguration() throws InvalidConfigurationException
    {
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
    }
}