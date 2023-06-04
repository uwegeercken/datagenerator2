package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public class RowField<T> {
    private String name;
    private T value;
    private RandomValueGenerator generator;

    public RowField(RandomValueGenerator generator, String name)
    {
        this.generator = generator;
        this.name = name;
    }

    public void generateValue() throws Exception
    {
        this.value = generator.generateRandomValue();
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
}
