package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DateReferenceGenerator implements RandomValueGenerator
{
    private final static Logger logger = LoggerFactory.getLogger(DateReferenceGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;
    private final List<TransformationMethod> transformationMethods;
    private String reference;
    private RowField referenceRowField;
    private DateTimeFormatter dateTimeFormatter;
    private final long minDaysOffset;
    private final long maxDaysOffset;
    private final String adjustTo;

    public DateReferenceGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
        if (fieldConfiguration.getOptions().get(OptionKey.REFERENCE.getKey()) instanceof String)
        {
            reference = ((String) fieldConfiguration.getOptions().get(OptionKey.REFERENCE.getKey()));
        }
        if (fieldConfiguration.getOptions().get(OptionKey.DATE_FORMAT.getKey()) instanceof String)
        {
            dateTimeFormatter = DateTimeFormatter.ofPattern((String) fieldConfiguration.getOptions().get(OptionKey.DATE_FORMAT.getKey()));
        }

        Object minOffset = fieldConfiguration.getOptions().get(OptionKey.MIN_DAYS_OFFSET.getKey());
        Object maxOffset = fieldConfiguration.getOptions().get(OptionKey.MAX_DAYS_OFFSET.getKey());
        this.minDaysOffset = minOffset instanceof Long ? (Long) minOffset : 0L;
        this.maxDaysOffset = maxOffset instanceof Long ? (Long) maxOffset : 0L;

        Object adjust = fieldConfiguration.getOptions().get(OptionKey.ADJUST_TO.getKey());
        this.adjustTo = adjust instanceof String ? (String) adjust : null;

        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    public void addReferenceRowField(RowField referenceRowField)
    {
        this.referenceRowField = referenceRowField;
    }

    @Override
    public String generateRandomValue()
    {
        RandomValueProvider<Long> referenceDateGenerator = (RandomValueProvider<Long>) referenceRowField.getGenerator();
        LocalDateTime referencedDateTime = Instant.ofEpochMilli(referenceDateGenerator.getGeneratedRandomValue())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        referencedDateTime = applyDaysOffset(referencedDateTime);
        referencedDateTime = applyAdjustTo(referencedDateTime);

        return dateTimeFormatter.format(referencedDateTime);
    }

    private LocalDateTime applyDaysOffset(LocalDateTime dateTime)
    {
        if (minDaysOffset == 0 && maxDaysOffset == 0)
        {
            return dateTime;
        }
        long offset = minDaysOffset == maxDaysOffset
                ? minDaysOffset
                : ThreadLocalRandom.current().nextLong(minDaysOffset, maxDaysOffset + 1);
        return dateTime.plusDays(offset);
    }

    private LocalDateTime applyAdjustTo(LocalDateTime dateTime)
    {
        if (adjustTo == null)
        {
            return dateTime;
        }
        return switch (adjustTo)
        {
            case "startOfMonth" -> dateTime.withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case "endOfMonth"   -> dateTime.withDayOfMonth(YearMonth.of(dateTime.getYear(), dateTime.getMonth()).lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59).withNano(0);
            case "startOfYear"  -> dateTime.withDayOfYear(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case "endOfYear"    -> dateTime.withMonth(12).withDayOfMonth(31)
                    .withHour(23).withMinute(59).withSecond(59).withNano(0);
            default             -> dateTime;
        };
    }

    @Override
    public Object transformRandomValue(Object value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}