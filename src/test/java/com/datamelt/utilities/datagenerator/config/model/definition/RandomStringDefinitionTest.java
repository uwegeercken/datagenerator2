package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomStringDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.RANDOMSTRING; }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(Map.of("minLength", 1L, "maxLength", 40L));
    }

    @Test
    @DisplayName("minLength as string throws with clear message")
    void validateMinLengthWrongType()
    {
        assertValidationFails(
                Map.of("minLength", "notanumber", "maxLength", 40L),
                "minLength", "long");
    }

    @Test
    @DisplayName("maxLength as string throws with clear message")
    void validateMaxLengthWrongType()
    {
        assertValidationFails(
                Map.of("minLength", 1L, "maxLength", "notanumber"),
                "maxLength", "long");
    }

    @Test
    @DisplayName("minLength zero or less throws")
    void validateMinLengthZero()
    {
        assertValidationFails(Map.of("minLength", 0L, "maxLength", 40L), "minLength");
    }

    @Test
    @DisplayName("maxLength smaller than minLength throws")
    void validateMaxLengthSmallerThanMinLength()
    {
        assertValidationFails(
                Map.of("minLength", 20L, "maxLength", 10L),
                "maxLength", "minLength");
    }

    @Test
    @DisplayName("equal minLength and maxLength produces fixed length string")
    void validateEqualMinMaxLength()
    {
        assertValidationPasses(Map.of("minLength", 10L, "maxLength", 10L));
    }

    @Test
    @DisplayName("multiple invalid options reported together")
    void validateMultipleErrors()
    {
        assertErrorCount(Map.of("minLength", "wrong", "maxLength", "alsowrong"), 2);
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("str.field");
        assertEquals(1L, processor.getFieldConfiguration().getOptions().get("minLength"));
        assertEquals(40L, processor.getFieldConfiguration().getOptions().get("maxLength"));
        assertNotNull(processor.getFieldConfiguration().getOptions().get("randomCharacters"));
    }
}