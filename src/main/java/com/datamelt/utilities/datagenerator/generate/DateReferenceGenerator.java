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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateReferenceGenerator implements RandomValueGenerator
{
    private final static Logger logger = LoggerFactory.getLogger(DateReferenceGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;
    private final List<TransformationMethod> transformationMethods;
    private String reference;
    private RowField referenceRowField;
    private DateTimeFormatter dateTimeFormatter;
    public DateReferenceGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(OptionKey.REFERENCE.getKey()) instanceof String)
        {
            reference = ((String) fieldConfiguration.getOptions().get(OptionKey.REFERENCE.getKey()));
        }
        if(fieldConfiguration.getOptions().get(OptionKey.DATE_FORMAT.getKey()) instanceof String)
        {
            dateTimeFormatter = DateTimeFormatter.ofPattern((String) fieldConfiguration.getOptions().get(OptionKey.DATE_FORMAT.getKey()));
        }
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

        return  dateTimeFormatter.format(referencedDateTime);
    }

    @Override
    public Object transformRandomValue(Object value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
