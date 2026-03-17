package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;

import java.util.concurrent.ThreadLocalRandom;

public class RowField
{
    private final String name;
    private Object value;
    private final RandomValueGenerator generator;

    public RowField(RandomValueGenerator generator, String name)
    {
        this.generator = generator;
        this.name = name;
    }

    public void generateValue(int nullProbability) throws InvalidConfigurationException, TransformationExecutionException
    {
        if (nullProbability > 0 && ThreadLocalRandom.current().nextInt(1, 101) <= nullProbability)
        {
            this.value = null;
            return;
        }
        this.value = transformValue(generator.generateRandomValue());
    }

    public RowField copy()
    {
        return new RowField(generator, name);
    }

    private Object transformValue(Object value) throws TransformationExecutionException
    {
        return generator.transformRandomValue(value);
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public RandomValueGenerator getGenerator()
    {
        return generator;
    }
}