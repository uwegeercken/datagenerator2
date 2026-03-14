package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomDateDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.RANDOMDATE; }

    @Test
    @DisplayName("maxYear as string throws with clear message")
    void validateMaxYearWrongType()
    {
        assertValidationFails(
                Map.of("minYear", 2020L, "maxYear", "gugu"),
                "maxYear", "long");
    }

    @Test
    @DisplayName("maxYear smaller than minYear throws")
    void validateMaxYearSmallerThanMinYear()
    {
        assertValidationFails(
                Map.of("minYear", 2030L, "maxYear", 2020L),
                "maxYear", "minYear");
    }

    @Test
    @DisplayName("invalid date format throws")
    void validateInvalidDateFormat()
    {
        assertValidationFails(
                Map.of("minYear", 2020L, "maxYear", 2030L, "dateFormat", "not-a-format-%%%"),
                "dateFormat");
    }

    @Test
    @DisplayName("negative minYear throws")
    void validateNegativeMinYear()
    {
        assertValidationFails(Map.of("minYear", -1L, "maxYear", 2030L), "minYear");
    }

    @Test
    @DisplayName("multiple invalid options reported together")
    void validateMultipleErrors()
    {
        assertErrorCount(Map.of("minYear", "wrong", "maxYear", "alsowrong"), 2);
    }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(
                Map.of("minYear", 2020L, "maxYear", 2030L, "dateFormat", "yyyy-MM-dd"));
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("date.fulldate");
        assertEquals(2020L, processor.getFieldConfiguration().getOptions().get("minYear"));
        assertEquals(2030L, processor.getFieldConfiguration().getOptions().get("maxYear"));
        assertEquals("yyyy-MM-dd", processor.getFieldConfiguration().getOptions().get("dateFormat"));
    }
}