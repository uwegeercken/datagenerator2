package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractDefinitionTest
{
    protected abstract FieldType getFieldType();

    protected FieldProcessor buildProcessor(String fieldName, Map<String, Object> options) throws InvalidConfigurationException
    {
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(getFieldType());
        fieldConfiguration.setOptions(new HashMap<>(options));  // copy to mutable map
        FieldProcessor processor = new FieldProcessor(fieldConfiguration);
        processor.setDefaultOptions();
        return processor;
    }

    protected FieldProcessor buildProcessorWithDefaults(String fieldName) throws InvalidConfigurationException
    {
        return buildProcessor(fieldName, new HashMap<>());
    }

    protected void assertValidationFails(Map<String, Object> options, String... expectedMessageFragments)
    {
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class, () ->
                buildProcessor("testfield", options).validateConfiguration());
        for (String fragment : expectedMessageFragments)
        {
            assertTrue(ex.getMessage().contains(fragment),
                    "expected message to contain [" + fragment + "] but was: " + ex.getMessage());
        }
    }

    protected void assertValidationPasses(Map<String, Object> options)
    {
        assertDoesNotThrow(() ->
                buildProcessor("testfield", options).validateConfiguration());
    }

    protected void assertErrorCount(Map<String, Object> options, int expectedCount)
    {
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class, () ->
                buildProcessor("testfield", options).validateConfiguration());
        assertTrue(ex.getMessage().contains(expectedCount + " configuration error(s)"),
                "expected " + expectedCount + " error(s) but got: " + ex.getMessage());
    }
}