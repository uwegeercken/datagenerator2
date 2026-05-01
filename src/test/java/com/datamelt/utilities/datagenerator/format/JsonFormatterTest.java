package com.datamelt.utilities.datagenerator.format;

import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JsonFormatterTest
{
    private static final String DATACONFIGURATION_TESTFILE_01 = "/datagenerator-test-01.yml";
    private static final String DATACONFIGURATION_TESTFILE_02 = "/datagenerator-test-02.yml";
    private static final String DATACONFIGURATION_TESTFILE_03 = "/datagenerator-test-03.yml";

    @Test
    @DisplayName("validate simple dataconfiguration file conversion to json")
    void testSimpleDataConfigurationConversionToJson() throws Exception
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_01).toURI());
        File dataConfigurationFile = new File(resourcePath.toString());
        DataConfiguration dataConfiguration;
        try (InputStream stream = new FileInputStream(dataConfigurationFile))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        Try<Row> row = rowBuilder.generate();

        assertTrue(row.isSuccess(), "invalid configuration file");
        JsonNode result = JsonFormatter.convertToJsonNode(row.getResult());

        assertTrue(result.has("gender"));
        assertTrue(result.has("weekday"));
        assertTrue(result.has("season"));
        assertTrue(result.has("number"));
    }

    @Test
    @DisplayName("validate single level nested dataconfiguration file conversion to json")
    void testSingleLevelNestedDataConfigurationConversionToJson() throws Exception
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_02).toURI());
        File dataConfigurationFile = new File(resourcePath.toString());
        DataConfiguration dataConfiguration;
        try (InputStream stream = new FileInputStream(dataConfigurationFile))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        Try<Row> row = rowBuilder.generate();

        assertTrue(row.isSuccess(), "invalid configuration file");
        JsonNode result = JsonFormatter.convertToJsonNode(row.getResult());


        assertTrue(result.has("address"));
        JsonNode addressNode = result.get("address");

        assertTrue(addressNode.has("street"));
        assertTrue(addressNode.has("housenumber"));
    }

    @Test
    @DisplayName("validate two level nested dataconfiguration file conversion to json")
    void testTwoLevelNestedDataConfigurationConversionToJson() throws Exception
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_03).toURI());
        File dataConfigurationFile = new File(resourcePath.toString());
        DataConfiguration dataConfiguration;
        try (InputStream stream = new FileInputStream(dataConfigurationFile))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        Try<Row> row = rowBuilder.generate();

        assertTrue(row.isSuccess(), "invalid configuration file");
        JsonNode result = JsonFormatter.convertToJsonNode(row.getResult());

        assertTrue(result.has("person"));
        JsonNode personNode = result.get("person");
        assertTrue(personNode.has("first_name"));

        JsonNode addressNode = personNode.get("address");

        assertTrue(addressNode.has("street"));
        assertTrue(addressNode.has("housenumber"));
    }

}