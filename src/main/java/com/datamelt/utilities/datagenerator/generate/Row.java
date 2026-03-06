package com.datamelt.utilities.datagenerator.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Row {
    private static final String DELIMITER = ",";
    private final List<RowField> fields = new ArrayList<>();
    public void addField(RowField field)
    {
        fields.add(field);
    }

    public String getRowHeader()
    {
        return fields.stream()
                .map(RowField::getName)
                .collect(Collectors.joining(DELIMITER));
    }

    public List<RowField> getFields()
    {
        return fields;
    }

    public Optional<RowField> getField(String name)
    {
        return fields.stream()
                .filter(field -> field.getName().equals(name))
                .findFirst();
    }
    @Override
    public String toString()
    {
        return fields.stream()
                .map(rowField -> rowField.getValue().toString())
                .collect(Collectors.joining(DELIMITER));
    }
}
