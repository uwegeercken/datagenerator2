package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class RandomTimestampGenerator implements RandomValueGenerator<String>, RandomValueProvider<Long>
{
    private static final Logger logger = LoggerFactory.getLogger(RandomTimestampGenerator.class);
    private static final Class<Double> BASE_DATATYPE = Double.class;
    private final FieldConfiguration fieldConfiguration;
    private final List<TransformationMethod> transformationMethods;
    private int minYear;
    private int maxYear;
    private long generatedRandomValue;
    private DateTimeFormatter dateTimeFormatter;
    public RandomTimestampGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(RandomTimestampOptions.MIN_YEAR.getKey()) instanceof Long)
        {
            minYear = ((Long) fieldConfiguration.getOptions().get(RandomTimestampOptions.MIN_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomTimestampOptions.MAX_YEAR.getKey()) instanceof Long)
        {
            maxYear = ((Long) fieldConfiguration.getOptions().get(RandomTimestampOptions.MAX_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomTimestampOptions.DATE_FORMAT.getKey()) instanceof String)
        {
            dateTimeFormatter = DateTimeFormatter.ofPattern((String) fieldConfiguration.getOptions().get(RandomTimestampOptions.DATE_FORMAT.getKey()));
        }
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
        LocalDateTime randomDateTime = DateUtility.getRandomDateTime(minYear, maxYear);
        generatedRandomValue = randomDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        String formattedDateTime = dateTimeFormatter.withZone(ZoneId.systemDefault()).format(randomDateTime);
        return formattedDateTime;
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
