package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomUuidDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.RANDOMUUID; }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(Map.of());
    }

    @Test
    @DisplayName("invalid output type throws")
    void validateInvalidOutputType()
    {
        assertThrows(InvalidConfigurationException.class, () ->
                buildProcessor("uuid.field", Map.of("outputType", "LONG")).validateOutputType());
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("uuid.field");
        assertEquals("VARCHAR", processor.getFieldConfiguration().getOptions().get("outputType"));
    }

    @Test
    @DisplayName("nullProbability=0 passes")
    void validateNullProbabilityZero()
    {
        assertValidationPasses(Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", 0L));
    }

    @Test
    @DisplayName("nullProbability=50 passes")
    void validateNullProbabilityFifty()
    {
        assertValidationPasses(Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", 50L));
    }

    @Test
    @DisplayName("nullProbability=100 passes")
    void validateNullProbabilityHundred()
    {
        assertValidationPasses(Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", 100L));
    }

    @Test
    @DisplayName("nullProbability greater than 100 throws")
    void validateNullProbabilityExceedsMax()
    {
        assertValidationFails(
                Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", 101L),
                "nullProbability", "0 and 100");
    }

    @Test
    @DisplayName("nullProbability negative throws")
    void validateNullProbabilityNegative()
    {
        assertValidationFails(
                Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", -1L),
                "nullProbability", "0 and 100");
    }

    @Test
    @DisplayName("nullProbability wrong type throws")
    void validateNullProbabilityWrongType()
    {
        assertValidationFails(
                Map.of("minLength", 1L, "maxLength", 40L, "nullProbability", "fifty"),
                "nullProbability", "long");
    }
}