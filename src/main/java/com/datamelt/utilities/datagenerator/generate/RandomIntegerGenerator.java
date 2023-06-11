package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomIntegerOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.utilities.Constants;
import com.datamelt.utilities.datagenerator.utilities.TransformationExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomIntegerGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomIntegerGenerator.class);

    private FieldConfiguration fieldConfiguration;

    private int minValue;
    private int maxValue;
    private String[] transform;

    public RandomIntegerGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;

        minValue = (Integer) fieldConfiguration.getOptions().get(RandomIntegerOptions.MIN_VALUE.getKey());
        maxValue = (Integer) fieldConfiguration.getOptions().get(RandomIntegerOptions.MAX_VALUE.getKey());
        transform = ((String) fieldConfiguration.getOptions().get(CategoryOptions.TRANSFORM.getKey())).split(Constants.OPTION_TRANSFORM_DIVIDER);
    }

    @Override
    public <T> T  generateRandomValue() throws Exception {

        Random random = new Random();
        Integer value = random.nextInt(minValue,maxValue);
        return (T) value ;
    }

    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(transform, value);
    }
}
