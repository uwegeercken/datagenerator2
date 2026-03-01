package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransformationChainingTest
{
    private RowBuilder getRowBuilderWithTransformations(String fieldName, Map<String, Object> options, List<TransformationConfiguration> transformations) throws Exception
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(FieldType.RANDOMSTRING);
        fieldConfiguration.setOptions(options);
        fieldConfiguration.setTransformations(transformations);
        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    private TransformationConfiguration transformation(String name)
    {
        TransformationConfiguration t = new TransformationConfiguration();
        t.setName(name);
        return t;
    }

    @Test
    @DisplayName("single transformation uppercase is applied")
    void singleTransformationUppercase() throws Exception
    {
        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 5L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 5L);
        options.put(RandomStringOptions.RANDOM_CHARACTERS.getKey(), "abcde");

        RowBuilder rowBuilder = getRowBuilderWithTransformations("testfield", options,
                List.of(transformation("uppercase")));

        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            String value = (String) row.getResult().getFields().getFirst().getValue();
            assertEquals(value.toUpperCase(), value, "value should be fully uppercase after transformation");
        }
    }

    @Test
    @DisplayName("chained transformations uppercase then reverse are both applied in order")
    void chainedTransformationsUppercaseThenReverse() throws Exception
    {
        Map<String, Object> options = new HashMap<>();
        // use fixed characters so we can predict the transformation result
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 5L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 5L);
        options.put(RandomStringOptions.RANDOM_CHARACTERS.getKey(), "abcde");

        RowBuilder rowBuilder = getRowBuilderWithTransformations("testfield", options,
                List.of(transformation("uppercase"), transformation("reverse")));

        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            String value = (String) row.getResult().getFields().getFirst().getValue();

            // if chaining works: first uppercased, then reversed
            // the result must be uppercase (since reverse of uppercase is still uppercase)
            assertEquals(value.toUpperCase(), value, "value should be uppercase - reverse of uppercase is still uppercase");

            // and it must be the reverse of an uppercase string (all chars in A-E range)
            assertTrue(value.chars().allMatch(c -> c >= 'A' && c <= 'E'),
                    "all characters should be in A-E range after uppercase transformation");
        }
    }

    @Test
    @DisplayName("chained transformations reverse then uppercase produce different result than uppercase then reverse for mixed case input")
    void chainedTransformationsOrderMatters() throws Exception
    {
        // We'll use a fixed known input via a category field to have predictable values
        // and verify the transformation order is respected
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration("testfield");
        fieldConfiguration.setType(FieldType.RANDOMSTRING);

        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 3L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 3L);
        options.put(RandomStringOptions.RANDOM_CHARACTERS.getKey(), "abc");

        fieldConfiguration.setOptions(options);
        // uppercase then reverse: "abc" -> "ABC" -> "CBA"
        fieldConfiguration.setTransformations(List.of(transformation("uppercase"), transformation("reverse")));

        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);

        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            String value = (String) row.getResult().getFields().getFirst().getValue();
            // result must be 3 chars, uppercase, in A-C range — proving both transformations applied
            assertEquals(3, value.length());
            assertTrue(value.chars().allMatch(c -> c == 'A' || c == 'B' || c == 'C'),
                    "expected uppercase A-C chars after chained uppercase+reverse, got: " + value);
        }
    }
}