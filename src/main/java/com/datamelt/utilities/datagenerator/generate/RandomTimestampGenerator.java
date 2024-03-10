package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomTimestampOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
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
    private SimpleDateFormat dateFormat;
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
            dateFormat = new SimpleDateFormat((String) fieldConfiguration.getOptions().get(RandomTimestampOptions.DATE_FORMAT.getKey()));
        }
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue() throws InvalidConfigurationException
    {
        Random random = new Random();
        Long minYearMilliseonds = DateUtility.getMinDate(minYear);
        Long maxYearMilliseonds = DateUtility.getMaxDate(maxYear);
        generatedRandomValue = random.nextLong(minYearMilliseonds, maxYearMilliseonds);
        return dateFormat.format(generatedRandomValue) ;
    }

    @Override
    public String transformRandomValue(String value) throws InvalidConfigurationException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }

    @Override
    public Long getGeneratedRandomValue()
    {
        return generatedRandomValue;
    }
}
