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
}