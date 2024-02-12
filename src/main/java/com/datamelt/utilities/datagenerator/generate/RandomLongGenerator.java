package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class RandomLongGenerator implements RandomValueGenerator<Long>
{
    private static final Logger logger = LoggerFactory.getLogger(RandomLongGenerator.class);
    private static final Class<Long> BASE_DATATYPE = Long.class;
    private final FieldConfiguration fieldConfiguration;

    private final List<TransformationMethod> transformationMethods;
    private final long minValue;
    private final long maxValue;

    public RandomLongGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(RandomLongOptions.MIN_VALUE.getKey()) instanceof Integer)
        {
            minValue = ((Integer) fieldConfiguration.getOptions().get(RandomLongOptions.MIN_VALUE.getKey())).longValue();
        }
        else
        {
            minValue = (Long) fieldConfiguration.getOptions().get(RandomLongOptions.MIN_VALUE.getKey());
        }
        if(fieldConfiguration.getOptions().get(RandomLongOptions.MAX_VALUE.getKey()) instanceof Integer)
        {
            maxValue = ((Integer) fieldConfiguration.getOptions().get(RandomLongOptions.MAX_VALUE.getKey())).longValue();
        }
        else
        {
            maxValue = (Long) fieldConfiguration.getOptions().get(RandomLongOptions.MAX_VALUE.getKey());
        }

        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public Long generateRandomValue() throws InvalidConfigurationException {

        Random random = new Random();
        Long value = random.nextLong(minValue, maxValue);
        return value ;
    }

    @Override
    public Long transformRandomValue(Long value) throws InvalidConfigurationException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
