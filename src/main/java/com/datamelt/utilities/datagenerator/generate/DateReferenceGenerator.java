package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomTimestampOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateReferenceGenerator implements RandomValueGenerator<String>
{
    private final static Logger logger = LoggerFactory.getLogger(DateReferenceGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private FieldConfiguration fieldConfiguration;
    private List<TransformationMethod> transformationMethods;
    private String reference;
    private RowField<?> referenceRowField;
    private DateTimeFormatter dateTimeFormatter;
    public DateReferenceGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey()) instanceof String)
        {
            reference = ((String) fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey()));
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()) instanceof String)
        {
            dateTimeFormatter = DateTimeFormatter.ofPattern((String) fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey())).withZone(ZoneId.of("UTC"));
        }
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    public void addReferenceRowField(RowField<?> referenceRowField)
    {
        this.referenceRowField = referenceRowField;
    }

    @Override
    public String generateRandomValue()
    {
        RandomValueProvider<Long> referenceDateGenerator = (RandomValueProvider<Long>) referenceRowField.getGenerator();
        if(referenceDateGenerator.getGeneratedRandomValue()==1672527600000L)
        {
            System.out.println();
        }
        LocalDateTime referencedDateTime = Instant.ofEpochMilli(referenceDateGenerator.getGeneratedRandomValue())
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();

        String test = dateTimeFormatter.format(referencedDateTime);
        return  test;
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
