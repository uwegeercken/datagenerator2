package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomTimestampDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.RANDOMTIMESTAMP; }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(Map.of("minYear", 2020L, "maxYear", 2030L, "dateFormat", "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    @DisplayName("maxYear as string throws with clear message")
    void validateMaxYearWrongType()
    {
        assertValidationFails(
                Map.of("minYear", 2020L, "maxYear", "gugu"),
                "maxYear", "long");
    }

    @Test
    @DisplayName("minYear as string throws with clear message")
    void validateMinYearWrongType()
    {
        assertValidationFails(
                Map.of("minYear", "notanumber", "maxYear", 2030L),
                "minYear", "long");
    }

    @Test
    @DisplayName("negative minYear throws")
    void validateNegativeMinYear()
    {
        assertValidationFails(Map.of("minYear", -1L, "maxYear", 2030L), "minYear");
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
    @DisplayName("invalid timestamp format throws")
    void validateInvalidDateFormat()
    {
        assertValidationFails(
                Map.of("minYear", 2020L, "maxYear", 2030L, "dateFormat", "not-a-format-%%%"),
                "dateFormat");
    }

    @Test
    @DisplayName("multiple invalid options reported together")
    void validateMultipleErrors()
    {
        assertErrorCount(Map.of("minYear", "wrong", "maxYear", "alsowrong"), 2);
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("ts.field");
        assertEquals(2020L, processor.getFieldConfiguration().getOptions().get("minYear"));
        assertEquals(2030L, processor.getFieldConfiguration().getOptions().get("maxYear"));
        assertEquals("yyyy-MM-dd HH:mm:ss", processor.getFieldConfiguration().getOptions().get("dateFormat"));
    }
}