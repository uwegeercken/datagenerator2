package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NullProbabilityTest
{
    private RowBuilder buildRowBuilder(FieldType fieldType, int nullProbability) throws InvalidConfigurationException
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration("testfield");
        fieldConfiguration.setType(fieldType);
        fieldConfiguration.setNullProbability(nullProbability);
        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    @Test
    @DisplayName("nullProbability=100 always produces null")
    void validateAlwaysNull() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMSTRING, 100);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNull(row.getResult().getFields().get(0).getValue(),
                    "expected null value but got: " + row.getResult().getFields().get(0).getValue());
        }
    }

    @Test
    @DisplayName("nullProbability=0 never produces null")
    void validateNeverNull() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMSTRING, 0);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNotNull(row.getResult().getFields().get(0).getValue(),
                    "expected non-null value but got null");
        }
    }

    @Test
    @DisplayName("nullProbability=50 produces nulls within expected range over 1000 rows")
    void validateApproximateNullFrequency() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMSTRING, 50);
        long nullCount = 0;
        int totalRows = 1000;

        for (int i = 0; i < totalRows; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            if (row.getResult().getFields().get(0).getValue() == null)
            {
                nullCount++;
            }
        }

        double nullPercentage = (double) nullCount / totalRows * 100;
        assertTrue(nullPercentage >= 35 && nullPercentage <= 65,
                "expected null percentage between 35% and 65% but was: " + nullPercentage + "%");
    }

    @Test
    @DisplayName("nullProbability=100 on randomdate always produces null")
    void validateAlwaysNullForRandomDate() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMDATE, 100);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNull(row.getResult().getFields().get(0).getValue(),
                    "expected null value but got: " + row.getResult().getFields().get(0).getValue());
        }
    }

    @Test
    @DisplayName("nullProbability=100 on randomtimestamp always produces null")
    void validateAlwaysNullForRandomTimestamp() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMTIMESTAMP, 100);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNull(row.getResult().getFields().get(0).getValue(),
                    "expected null value but got: " + row.getResult().getFields().get(0).getValue());
        }
    }

    @Test
    @DisplayName("nullProbability=100 on regularexpression always produces null")
    void validateAlwaysNullForRegularExpression() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.REGULAREXPRESSION, 100);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNull(row.getResult().getFields().get(0).getValue(),
                    "expected null value but got: " + row.getResult().getFields().get(0).getValue());
        }
    }

    @Test
    @DisplayName("nullProbability=100 on randomuuid always produces null")
    void validateAlwaysNullForRandomUuid() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilder(FieldType.RANDOMUUID, 100);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> row = rowBuilder.generate();
            assertTrue(row.isSuccess());
            assertNull(row.getResult().getFields().get(0).getValue(),
                    "expected null value but got: " + row.getResult().getFields().get(0).getValue());
        }
    }
}