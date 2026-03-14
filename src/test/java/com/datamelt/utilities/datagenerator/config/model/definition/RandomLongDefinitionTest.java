package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomLongDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.RANDOMLONG; }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(Map.of("minValue", 0L, "maxValue", 1000L));
    }

    @Test
    @DisplayName("minValue as string throws with clear message")
    void validateMinValueWrongType()
    {
        assertValidationFails(
                Map.of("minValue", "notanumber", "maxValue", 1000L),
                "minValue", "long");
    }

    @Test
    @DisplayName("maxValue as string throws with clear message")
    void validateMaxValueWrongType()
    {
        assertValidationFails(
                Map.of("minValue", 0L, "maxValue", "notanumber"),
                "maxValue", "long");
    }

    @Test
    @DisplayName("maxValue smaller than minValue throws")
    void validateMaxValueSmallerThanMinValue()
    {
        assertValidationFails(
                Map.of("minValue", 1000L, "maxValue", 0L),
                "maxValue", "minValue");
    }

    @Test
    @DisplayName("equal minValue and maxValue passes")
    void validateEqualMinMaxValue()
    {
        assertValidationPasses(Map.of("minValue", 100L, "maxValue", 100L));
    }

    @Test
    @DisplayName("negative minValue passes")
    void validateNegativeMinValue()
    {
        assertValidationPasses(Map.of("minValue", -100L, "maxValue", 100L));
    }

    @Test
    @DisplayName("multiple invalid options reported together")
    void validateMultipleErrors()
    {
        assertErrorCount(Map.of("minValue", "wrong", "maxValue", "alsowrong"), 2);
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("long.field");
        assertEquals(0L, processor.getFieldConfiguration().getOptions().get("minValue"));
        assertEquals(1000000L, processor.getFieldConfiguration().getOptions().get("maxValue"));
    }
}