package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.utilities.TransformationExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomDoubleGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomDoubleGenerator.class);

    private FieldConfiguration fieldConfiguration;

    private long minValue;
    private long maxValue;

    public RandomDoubleGenerator(FieldConfiguration fieldConfiguration)
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
    }

    @Override
    public <T> T  generateRandomValue() throws Exception {

        Random random = new Random();
        Double value = random.nextDouble(minValue, maxValue);
        return (T) value ;
    }

    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(value, fieldConfiguration.getTransformations());
    }
}
