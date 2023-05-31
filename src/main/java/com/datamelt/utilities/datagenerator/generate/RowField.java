package com.datamelt.utilities.datagenerator.generate;

public class RowField<T> {
    private String name;
    private T value;

    public RowField(String name, T value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
