package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DateReferenceGeneratorTest
{
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private RowBuilder buildRowBuilderWithOffset(long minDaysOffset, long maxDaysOffset) throws InvalidConfigurationException
    {
        return buildRowBuilder(minDaysOffset, maxDaysOffset, null);
    }

    private RowBuilder buildRowBuilderWithAdjustTo(String adjustTo) throws InvalidConfigurationException
    {
        return buildRowBuilder(0, 0, adjustTo);
    }

    private RowBuilder buildRowBuilder(long minDaysOffset, long maxDaysOffset, String adjustTo) throws InvalidConfigurationException
    {
        DataConfiguration dataConfiguration = new DataConfiguration();

        FieldConfiguration orderDate = new FieldConfiguration("order_date");
        orderDate.setType(FieldType.RANDOMDATE);

        FieldConfiguration derivedDate = new FieldConfiguration("derived_date");
        derivedDate.setType(FieldType.DATEREFERENCE);
        derivedDate.getOptions().put("reference", "order_date");
        derivedDate.getOptions().put("dateFormat", DATE_FORMAT);
        derivedDate.getOptions().put("minDaysOffset", minDaysOffset);
        derivedDate.getOptions().put("maxDaysOffset", maxDaysOffset);
        if (adjustTo != null)
        {
            derivedDate.getOptions().put("adjustTo", adjustTo);
        }

        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(orderDate);
        fields.add(derivedDate);
        dataConfiguration.setFields(fields);

        DataFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    @Test
    @DisplayName("zero offset produces same date as reference")
    void validateZeroOffsetProducesSameDate() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithOffset(0, 0);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            String orderDate = (String) result.getResult().getFields().get(0).getValue();
            String derivedDate = (String) result.getResult().getFields().get(1).getValue();
            assertEquals(orderDate, derivedDate,
                    "derived date should equal order date when offset is zero");
        }
    }

    @Test
    @DisplayName("fixed positive offset produces date exactly n days later")
    void validateFixedPositiveOffset() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithOffset(7, 7);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate orderDate = LocalDate.parse((String) result.getResult().getFields().get(0).getValue(), FORMATTER);
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(orderDate.plusDays(7), derivedDate,
                    "derived date should be exactly 7 days after order date");
        }
    }

    @Test
    @DisplayName("fixed negative offset produces date exactly n days earlier")
    void validateFixedNegativeOffset() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithOffset(-14, -14);
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate orderDate = LocalDate.parse((String) result.getResult().getFields().get(0).getValue(), FORMATTER);
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(orderDate.minusDays(14), derivedDate,
                    "derived date should be exactly 14 days before order date");
        }
    }

    @Test
    @DisplayName("range offset produces date within expected bounds over 1000 rows")
    void validateRangeOffset() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithOffset(1, 30);
        for (int i = 0; i < 1000; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate orderDate = LocalDate.parse((String) result.getResult().getFields().get(0).getValue(), FORMATTER);
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            long daysBetween = ChronoUnit.DAYS.between(orderDate, derivedDate);
            assertTrue(daysBetween >= 1 && daysBetween <= 30,
                    "derived date should be between 1 and 30 days after order date but was " + daysBetween + " days");
        }
    }

    @Test
    @DisplayName("range offset produces varying values over 1000 rows")
    void validateRangeOffsetProducesVariation() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithOffset(1, 30);
        long minObserved = Long.MAX_VALUE;
        long maxObserved = Long.MIN_VALUE;
        for (int i = 0; i < 1000; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate orderDate = LocalDate.parse((String) result.getResult().getFields().get(0).getValue(), FORMATTER);
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            long daysBetween = ChronoUnit.DAYS.between(orderDate, derivedDate);
            minObserved = Math.min(minObserved, daysBetween);
            maxObserved = Math.max(maxObserved, daysBetween);
        }
        assertTrue(maxObserved > minObserved,
                "expected variation in offset over 1000 rows but all offsets were " + minObserved);
    }

    @Test
    @DisplayName("adjustTo startOfMonth always produces day 1")
    void validateAdjustToStartOfMonth() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithAdjustTo("startOfMonth");
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(1, derivedDate.getDayOfMonth(),
                    "startOfMonth should always produce day 1 but was " + derivedDate.getDayOfMonth());
        }
    }

    @Test
    @DisplayName("adjustTo endOfMonth always produces last day of month")
    void validateAdjustToEndOfMonth() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithAdjustTo("endOfMonth");
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(derivedDate.lengthOfMonth(), derivedDate.getDayOfMonth(),
                    "endOfMonth should always produce last day of month but was " + derivedDate.getDayOfMonth());
        }
    }

    @Test
    @DisplayName("adjustTo startOfYear always produces January 1st")
    void validateAdjustToStartOfYear() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithAdjustTo("startOfYear");
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(1, derivedDate.getDayOfYear(),
                    "startOfYear should always produce January 1st but was " + derivedDate);
        }
    }

    @Test
    @DisplayName("adjustTo endOfYear always produces December 31st")
    void validateAdjustToEndOfYear() throws InvalidConfigurationException
    {
        RowBuilder rowBuilder = buildRowBuilderWithAdjustTo("endOfYear");
        for (int i = 0; i < 100; i++)
        {
            Try<Row> result = rowBuilder.generate();
            assertTrue(result.isSuccess());
            LocalDate derivedDate = LocalDate.parse((String) result.getResult().getFields().get(1).getValue(), FORMATTER);
            assertEquals(12, derivedDate.getMonthValue(),
                    "endOfYear should always produce month 12 but was " + derivedDate.getMonthValue());
            assertEquals(31, derivedDate.getDayOfMonth(),
                    "endOfYear should always produce day 31 but was " + derivedDate.getDayOfMonth());
        }
    }
}