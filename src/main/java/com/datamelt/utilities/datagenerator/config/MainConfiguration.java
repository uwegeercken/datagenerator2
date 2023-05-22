package com.datamelt.utilities.datagenerator.config;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class MainConfiguration
{
    private String name;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String tableName = "generateddata";
    private String databaseName;
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
