package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLongGenerator implements RandomValueGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(RandomLongGenerator.class);
    private static final Class<Long> BASE_DATATYPE = Long.class;
    private final FieldConfiguration fieldConfiguration;

    private final List<TransformationMethod> transformationMethods;
    private final long minValue;
    private final long maxValue;

    public RandomLongGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(OptionKey.MIN_VALUE.getKey()) instanceof Integer)
        {
            minValue = ((Integer) fieldConfiguration.getOptions().get(OptionKey.MIN_VALUE.getKey())).longValue();
        }
        else
        {
            minValue = (Long) fieldConfiguration.getOptions().get(OptionKey.MIN_VALUE.getKey());
        }
        if(fieldConfiguration.getOptions().get(OptionKey.MAX_VALUE.getKey()) instanceof Integer)
        {
            maxValue = ((Integer) fieldConfiguration.getOptions().get(OptionKey.MAX_VALUE.getKey())).longValue();
        }
        else
        {
            maxValue = (Long) fieldConfiguration.getOptions().get(OptionKey.MAX_VALUE.getKey());
        }

        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public Long generateRandomValue()
    {
        Long value = ThreadLocalRandom.current().nextLong(minValue, maxValue +1);
        return value ;
    }

    @Override
    public Object transformRandomValue(Object value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
