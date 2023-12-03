package com.datamelt.utilities.datagenerator.utilities.duckdb.structure;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
    private String name;
    private List<TableField> fields = new ArrayList<>();

    public Struct(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableField> getFields()
    {
        return fields;
    }

    public void setFields(List<TableField> fields)
    {
        this.fields = fields;
    }

    public void addField(String name, DataTypeDuckDb dataTypeDuckDb)
    {
        fields.add(new TableField(name, dataTypeDuckDb));
    }
}
