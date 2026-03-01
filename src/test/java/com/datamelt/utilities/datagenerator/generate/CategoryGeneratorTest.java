package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class CategoryGeneratorTest
{
    private RowBuilder getRowBuilder(FieldConfiguration fieldConfiguration) throws Exception
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    private FieldConfiguration categoryField(String name, List<FieldConfigurationValue> values)
    {
        FieldConfiguration fieldConfiguration = new FieldConfiguration(name);
        fieldConfiguration.setType(FieldType.CATEGORY);
        fieldConfiguration.setValues(new ArrayList<>(values));
        fieldConfiguration.setOptions(new HashMap<>());
        return fieldConfiguration;
    }

    @Test
    @DisplayName("throws when no values are defined")
    void throwsWhenNoValuesAreDefined()
    {
        FieldConfiguration fieldConfiguration = categoryField("testfield", List.of());
        assertThrows(InvalidConfigurationException.class, () -> getRowBuilder(fieldConfiguration));
    }

    @Test
    @DisplayName("throws when sum of weights exceeds 100")
    void throwsWhenSumOfWeightsExceeds100()
    {
        FieldConfiguration fieldConfiguration = categoryField("testfield", List.of(
                new FieldConfigurationValue("a", 60),
                new FieldConfigurationValue("b", 60)
        ));
        assertThrows(InvalidConfigurationException.class, () -> getRowBuilder(fieldConfiguration));
    }

    @Test
    @DisplayName("generated value is always from the defined value list")
    void generatedValueIsAlwaysFromValueList() throws Exception
    {
        List<FieldConfigurationValue> values = List.of(
                new FieldConfigurationValue("monday", 20),
                new FieldConfigurationValue("tuesday", 20),
                new FieldConfigurationValue("wednesday", 20),
                new FieldConfigurationValue("thursday", 20),
                new FieldConfigurationValue("friday", 20)
        );
        RowBuilder rowBuilder = getRowBuilder(categoryField("weekday", values));
        List<String> allowedValues = values.stream().map(FieldConfigurationValue::getValue).toList();

        for (int i = 0; i < 1000; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            String generated = (String) row.getResult().getFields().getFirst().getValue();
            assertTrue(allowedValues.contains(generated),
                    "generated value [" + generated + "] is not in the allowed value list");
        }
    }

    @Test
    @DisplayName("weighted distribution roughly matches configured weights over many samples")
    void weightedDistributionRoughlyMatchesConfiguredWeights() throws Exception
    {
        // "rare" has weight 10, "common" has weight 90
        // over 10000 samples, "rare" should appear roughly 10% of the time
        List<FieldConfigurationValue> values = List.of(
                new FieldConfigurationValue("rare", 10),
                new FieldConfigurationValue("common", 90)
        );
        RowBuilder rowBuilder = getRowBuilder(categoryField("testfield", values));

        int samples = 10000;
        long rareCount = LongStream.range(0, samples)
                .mapToObj(i -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(row -> (String) row.getResult().getFields().getFirst().getValue())
                .filter("rare"::equals)
                .count();

        double rarePercentage = (double) rareCount / samples * 100;
        // allow +/- 5% tolerance around the configured 10%
        assertTrue(rarePercentage >= 5.0 && rarePercentage <= 15.0,
                "expected ~10%% for 'rare', got " + rarePercentage + "%%");
    }

    @Test
    @DisplayName("equally weighted values all appear over many samples")
    void equalWeightsAllValuesAppear() throws Exception
    {
        List<FieldConfigurationValue> values = List.of(
                new FieldConfigurationValue("a", 25),
                new FieldConfigurationValue("b", 25),
                new FieldConfigurationValue("c", 25),
                new FieldConfigurationValue("d", 25)
        );
        RowBuilder rowBuilder = getRowBuilder(categoryField("testfield", values));

        int samples = 1000;
        Map<String, Long> counts = LongStream.range(0, samples)
                .mapToObj(i -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(row -> (String) row.getResult().getFields().getFirst().getValue())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        // every value should appear at least once in 1000 samples
        List.of("a", "b", "c", "d").forEach(value ->
                assertTrue(counts.getOrDefault(value, 0L) > 0,
                        "value [" + value + "] never appeared in " + samples + " samples"));
    }

    @Test
    @DisplayName("unweighted values are auto-distributed and all appear")
    void unweightedValuesAreAutoDistributed() throws Exception
    {
        // no weights set — processor should distribute equally
        List<FieldConfigurationValue> values = List.of(
                new FieldConfigurationValue("x"),
                new FieldConfigurationValue("y"),
                new FieldConfigurationValue("z")
        );
        RowBuilder rowBuilder = getRowBuilder(categoryField("testfield", values));

        int samples = 1000;
        Map<String, Long> counts = LongStream.range(0, samples)
                .mapToObj(i -> rowBuilder.generate())
                .filter(Try::isSuccess)
                .map(row -> (String) row.getResult().getFields().getFirst().getValue())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        List.of("x", "y", "z").forEach(value ->
                assertTrue(counts.getOrDefault(value, 0L) > 0,
                        "value [" + value + "] never appeared in " + samples + " samples"));
    }

    @Test
    @DisplayName("zero-weight values are removed and never generated")
    void zeroWeightValuesAreNeverGenerated() throws Exception
    {
        List<FieldConfigurationValue> values = List.of(
                new FieldConfigurationValue("active", 100),
                new FieldConfigurationValue("excluded", 0)
        );
        RowBuilder rowBuilder = getRowBuilder(categoryField("testfield", values));

        for (int i = 0; i < 500; i++)
        {
            Try<Row> row = rowBuilder.generate();
            String generated = (String) row.getResult().getFields().getFirst().getValue();
            assertNotEquals("excluded", generated, "zero-weight value should never be generated");
        }
    }
}