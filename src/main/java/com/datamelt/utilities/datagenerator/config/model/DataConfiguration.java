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

    private List<FieldConfiguration> fields;

    public String getName()
    {
        return name;
    }

    public List<FieldConfiguration> getFields()
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

    public void setName(String name) {
        this.name = name;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setFields(List<FieldConfiguration> fields) {
        this.fields = fields;
    }
}
