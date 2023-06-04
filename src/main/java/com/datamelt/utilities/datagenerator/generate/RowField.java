package com.datamelt.utilities.datagenerator.generate;

public class RowField<T> {
    private String name;
    private T value;
    private RandomValueGenerator generator;

    public RowField(RandomValueGenerator generator, String name)
    {
        this.generator = generator;
        this.name = name;
    }

    public void generateValue()
    {
        //this.value = generator.generateRandomValue(field);
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
