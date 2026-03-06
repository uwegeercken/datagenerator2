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

    private RowGenerator getRowGenerator() throws InvalidConfigurationException, IOException, URISyntaxException
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_01).toURI());
        return new RowGenerator(resourcePath.toString());
    }

    @Test
    @DisplayName("test generate 100 rows returns exactly 100 successful rows")
    void testGenerateRows() throws InvalidConfigurationException, IOException, URISyntaxException
    {
        List<Row> rows = getRowGenerator().generateRows(100)
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .toList();

        assertEquals(100, rows.size());
    }

    @Test
    @DisplayName("test generate single row is successful")
    void generateSingleRow() throws Exception
    {
        Try<Row> row = getRowGenerator().generateRow();
        assertTrue(row.isSuccess());  // fixed: was assertTrue(row::isSuccess) which is always true
    }

    @Test
    @DisplayName("test generated row has fields")
    void generatedRowHasFields() throws Exception
    {
        Try<Row> row = getRowGenerator().generateRow();
        assertTrue(row.isSuccess());
        assertFalse(row.getResult().getFields().isEmpty());
    }

    @Test
    @DisplayName("test generate zero rows returns empty list")
    void testGenerateZeroRows() throws InvalidConfigurationException, IOException, URISyntaxException
    {
        List<Row> rows = getRowGenerator().generateRows(0)
                .filter(Try::isSuccess)
                .map(Try::getResult)
                .toList();

        assertEquals(0, rows.size());
    }
}
