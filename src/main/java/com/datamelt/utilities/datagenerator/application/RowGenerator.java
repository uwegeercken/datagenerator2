package com.datamelt.utilities.datagenerator.application;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.export.*;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class RowGenerator
{
    private DataConfiguration dataConfiguration;

    public RowGenerator(String dataConfigurationFilename) throws IOException, InvalidConfigurationException
    {
        loadDataConfiguration(dataConfigurationFilename);
        validateDataConfiguration();
    }

    private void loadDataConfiguration(String dataConfigurationFilename) throws IOException
    {
        try(InputStream stream = new FileInputStream(dataConfigurationFilename))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);        }
    }

    private void validateDataConfiguration() throws InvalidConfigurationException
    {
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
    }

    public Stream<Try<Row>> generateRows(long numberOfRows)
    {
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        return LongStream.range(0, numberOfRows)
                .mapToObj(rangeValue -> rowBuilder.generate());
    }

    public Try<Row> generateRow(String dataConfigurationFilename)
    {
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        return rowBuilder.generate();
    }
}
