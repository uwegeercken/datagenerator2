package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateGenerator implements RandomValueGenerator<String>, RandomValueProvider<Long>
{
    private static final Logger logger = LoggerFactory.getLogger(RandomDateGenerator.class);

    private static final Class<Double> BASE_DATATYPE = Double.class;
    private final FieldConfiguration fieldConfiguration;

    private final List<TransformationMethod> transformationMethods;
    private int minYear;
    private int maxYear;
    private DateTimeFormatter dateTimeFormatter;
    private DataTypeDuckDb outputType;
    private Long generatedRandomValue;

    public RandomDateGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(RandomDateOptions.MIN_YEAR.getKey()) instanceof Long)
        {
            minYear = ((Long) fieldConfiguration.getOptions().get(RandomDateOptions.MIN_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.MAX_YEAR.getKey()) instanceof Long)
        {
            maxYear = ((Long) fieldConfiguration.getOptions().get(RandomDateOptions.MAX_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()) instanceof String)
        {
            dateTimeFormatter = DateTimeFormatter.ofPattern((String) fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()));
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.OUTPUT_TYPE.getKey()) instanceof String)
        {
            outputType = fieldConfiguration.getOutputType();
        }
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
        LocalDate randomDate = DateUtility.getRandomDate(minYear, maxYear);
        generatedRandomValue = DateUtility.getRandomDateMilliseconds(minYear,maxYear);
        return randomDate.format(dateTimeFormatter);
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }

    @Override
    public Long getGeneratedRandomValue()
    {
        return generatedRandomValue;
    }
}
