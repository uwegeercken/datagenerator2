package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDoubleGeneratorTest
{
    private RowBuilder getRowBuilder(String fieldName, Map<String, Object> options) throws Exception
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(FieldType.RANDOMDOUBLE);
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
        options.put(OptionKey.MIN_VALUE.getKey(), 50L);
        options.put(OptionKey.MAX_VALUE.getKey(), 10L);

        assertThrows(InvalidConfigurationException.class,()->{
            RowBuilder rowBuilder = getRowBuilder("testfield", options);
        });
    }

    @Test
    @DisplayName("testing generated value is within min and max bounds")
    void validateGeneratedValueWithinBounds() throws Exception
    {
            long minValue = 10L;
            long maxValue = 20L;

            Map<String, Object> options = new HashMap<>();
            options.put(OptionKey.MIN_VALUE.getKey(), minValue);
            options.put(OptionKey.MAX_VALUE.getKey(), maxValue);

            RowBuilder rowBuilder = getRowBuilder("testfield", options);
            for (int i = 0; i < 1000; i++)
                {
                    Try<Row> row = rowBuilder.generate();
                    assertTrue(row.isSuccess());
                    double value = (Double) row.getResult().getFields().getFirst().getValue();
                    assertTrue(value >= minValue && value <= maxValue + 1,
                            "generated value " + value + " must be within [" + minValue + ", " + (maxValue + 1) + "]");
                }
        }

    @Test
    @DisplayName("testing equal min and max value produces values in single unit range")
    void validateEqualMinMaxValue() throws Exception
    {
            long minValue = 5L;
            long maxValue = 5L;

            Map<String, Object> options = new HashMap<>();
            options.put(OptionKey.MIN_VALUE.getKey(), minValue);
            options.put(OptionKey.MAX_VALUE.getKey(), maxValue);

            RowBuilder rowBuilder = getRowBuilder("testfield", options);
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            double value = (Double) row.getResult().getFields().getFirst().getValue();
            assertTrue(value >= minValue && value < maxValue + 1);
    }
}