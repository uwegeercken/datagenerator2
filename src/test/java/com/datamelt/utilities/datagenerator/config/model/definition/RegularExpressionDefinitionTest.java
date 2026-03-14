package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.REGULAREXPRESSION; }

    @Test
    @DisplayName("valid pattern passes")
    void validateValidPattern()
    {
        assertValidationPasses(Map.of("pattern", "[A-Za-z0-9]{1,10}"));
    }

    @Test
    @DisplayName("valid pattern with fixed multiplier passes")
    void validateValidPatternFixedMultiplier()
    {
        assertValidationPasses(Map.of("pattern", "[A-Z]{5}"));
    }

    @Test
    @DisplayName("empty pattern throws")
    void validateEmptyPattern()
    {
        assertValidationFails(Map.of("pattern", ""), "pattern");
    }

    @Test
    @DisplayName("empty multiplier throws")
    void validateEmptyMultiplier()
    {
        assertValidationFails(Map.of("pattern", "[A-Z]{}"), "pattern");
    }

    @Test
    @DisplayName("multiplier min zero throws")
    void validateMultiplierMinZero()
    {
        assertValidationFails(Map.of("pattern", "[A-Z]{0,5}"), "pattern");
    }

    @Test
    @DisplayName("multiplier max not greater than min throws")
    void validateMultiplierMaxNotGreaterThanMin()
    {
        assertValidationFails(Map.of("pattern", "[A-Z]{5,3}"), "pattern");
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("regex.field");
        assertEquals("[A-Za-z0-9]{1,10}", processor.getFieldConfiguration().getOptions().get("pattern"));
    }
}