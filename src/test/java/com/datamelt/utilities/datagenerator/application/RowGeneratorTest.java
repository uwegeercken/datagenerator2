package com.datamelt.utilities.datagenerator.application;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RowGeneratorTest
{
    private static final String DATACONFIGURATION_TESTFILE_01 = "/datagenerator-test-01.yml";

    @Test
    void testGenerateRow() throws InvalidConfigurationException, IOException, URISyntaxException
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_01).toURI());
        RowGenerator rowGenerator = new RowGenerator(resourcePath.toString());
        List<Row> rows = rowGenerator.generateRows(100)
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .toList();

        assertTrue(() -> rows.size()==100);
    }

    @Test
    @DisplayName("test generate single row")
    void generateSingleRow() throws Exception
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_01).toURI());
        RowGenerator generator = new RowGenerator(resourcePath.toString());
        Try<Row> row =  generator.generateRow(resourcePath.toString());
        assertTrue(row::isSuccess);
    }
}