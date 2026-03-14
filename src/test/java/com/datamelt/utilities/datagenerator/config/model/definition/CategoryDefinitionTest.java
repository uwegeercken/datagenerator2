package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.CATEGORY; }

    private FieldProcessor buildProcessorWithValues(String fieldName, List<FieldConfigurationValue> values) throws InvalidConfigurationException
    {
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(FieldType.CATEGORY);
        fieldConfiguration.setOptions(new HashMap<>());
        fieldConfiguration.setValues(values);
        FieldProcessor processor = new FieldProcessor(fieldConfiguration);
        processor.setDefaultOptions();
        return processor;
    }

    private List<FieldConfigurationValue> valuesWithWeights(Object... pairs)
    {
        List<FieldConfigurationValue> values = new ArrayList<>();
        for (int i = 0; i < pairs.length; i += 2)
        {
            values.add(new FieldConfigurationValue((String) pairs[i], (int) pairs[1 + i]));
        }
        return values;
    }

    @Test
    @DisplayName("valid configuration with weights summing to 100 passes")
    void validateValidConfiguration() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = valuesWithWeights(
                "monday", 20, "tuesday", 20, "wednesday", 20,
                "thursday", 20, "friday", 20);
        assertDoesNotThrow(() -> buildProcessorWithValues("weekday", values).validateConfiguration());
    }

    @Test
    @DisplayName("empty values list throws")
    void validateEmptyValues() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithValues("weekday", new ArrayList<>());
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class, () ->
                processor.validateConfiguration());
        assertTrue(ex.getMessage().contains("number of values can not be zero"));
    }

    @Test
    @DisplayName("sum of weights greater than 100 throws")
    void validateWeightSumExceeds100() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = valuesWithWeights(
                "monday", 60, "tuesday", 60);
        FieldProcessor processor = buildProcessorWithValues("weekday", values);
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class, () ->
                processor.validateConfiguration());
        assertTrue(ex.getMessage().contains("sum of weights"));
    }

    @Test
    @DisplayName("weight smaller than -1 throws")
    void validateWeightSmallerThanMinusOne() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = valuesWithWeights("monday", -2);
        FieldProcessor processor = buildProcessorWithValues("weekday", values);
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class, () ->
                processor.validateConfiguration());
        assertTrue(ex.getMessage().contains("weight cannot be smaller than -1"));
    }

    @Test
    @DisplayName("weights distributed correctly when some have no weight defined")
    void validateWeightDistribution() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = new ArrayList<>();
        values.add(new FieldConfigurationValue("saturday", 10));
        values.add(new FieldConfigurationValue("sunday", 10));
        values.add(new FieldConfigurationValue("monday", FieldConfigurationValue.DEFAULT_WEIGHT));
        values.add(new FieldConfigurationValue("tuesday", FieldConfigurationValue.DEFAULT_WEIGHT));
        values.add(new FieldConfigurationValue("wednesday", FieldConfigurationValue.DEFAULT_WEIGHT));
        values.add(new FieldConfigurationValue("thursday", FieldConfigurationValue.DEFAULT_WEIGHT));
        values.add(new FieldConfigurationValue("friday", FieldConfigurationValue.DEFAULT_WEIGHT));

        FieldProcessor processor = buildProcessorWithValues("weekday", values);
        processor.validateConfiguration();
        processor.processConfiguration();

        int totalWeight = processor.getFieldConfiguration().getValues().stream()
                .mapToInt(FieldConfigurationValue::getWeight)
                .sum();
        assertEquals(100, totalWeight);
    }

    @Test
    @DisplayName("zero weight values removed during processing")
    void validateZeroWeightValuesRemoved() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = valuesWithWeights(
                "monday", 50, "tuesday", 50, "wednesday", 0);
        FieldProcessor processor = buildProcessorWithValues("weekday", values);
        processor.validateConfiguration();
        processor.processConfiguration();

        assertEquals(2, processor.getFieldConfiguration().getValues().size());
        assertTrue(processor.getFieldConfiguration().getValues().stream()
                .noneMatch(v -> v.getValue().equals("wednesday")));
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        List<FieldConfigurationValue> values = valuesWithWeights("monday", 100);
        FieldProcessor processor = buildProcessorWithValues("weekday", values);
        assertEquals(",", processor.getFieldConfiguration().getOptions().get("categoryFileSeparator"));
    }
}