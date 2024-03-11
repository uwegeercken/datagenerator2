package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;

public class RowField<T> {
    private final String name;
    private T value;
    private final RandomValueGenerator<T> generator;

    public RowField(RandomValueGenerator<T> generator, String name)
    {
        this.generator = generator;
        this.name = name;
    }

    public void generateValue() throws InvalidConfigurationException, TransformationExecutionException
    {
        this.value = transformValue(generator.generateRandomValue());
    }

    private T transformValue(T value) throws TransformationExecutionException
    {
        return generator.transformRandomValue(value);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public RandomValueGenerator<T> getGenerator()
    {
        return generator;
    }
}
