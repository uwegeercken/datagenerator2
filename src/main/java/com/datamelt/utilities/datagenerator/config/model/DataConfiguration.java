package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class DataConfiguration
{
    private String name;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String tableName = "generateddata";
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String databaseName = "datagenerator2.duckdb";
    private List<Field> fields;

    public String getName()
    {
        return name;
    }

    public List<Field> getFields()
    {
        return fields;
    }

    public String getTableName()
    {
        return tableName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }
}
