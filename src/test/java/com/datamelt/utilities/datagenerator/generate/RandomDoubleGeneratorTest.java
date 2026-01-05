package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDoubleOptions;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomDoubleGeneratorTest
{
    private RowBuilder getRowBuilder(String fieldName, Map<String, Object> options) throws Exception
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(FieldType.RANDOMLONG);
        fieldConfiguration.setOptions(options);
        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    @Test
    @DisplayName("testing exception when max value < min value")
    void validateErrorMaxValueSmallerMinValue()
    {
        Map<String, Object> options = new HashMap<>();
        options.put(RandomDoubleOptions.MIN_VALUE.getKey(), 50L);
        options.put(RandomDoubleOptions.MAX_VALUE.getKey(), 10L);

        assertThrows(InvalidConfigurationException.class,()->{
            RowBuilder rowBuilder = getRowBuilder("testfield", options);
        });
    }
}