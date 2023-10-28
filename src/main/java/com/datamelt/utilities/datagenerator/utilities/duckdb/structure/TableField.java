package com.datamelt.utilities.datagenerator.utilities.duckdb.structure;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

public class TableField
{
    private final String name;

    private final DataTypeDuckDb dataTypeDuckDb;

    public TableField(String name, DataTypeDuckDb dataTypeDuckDb)
    {
        this.name = name;
        this.dataTypeDuckDb = dataTypeDuckDb;
    }

    public String getName()
    {
        return name;
    }

    public DataTypeDuckDb getDataTypeDuckDb()
    {
        return dataTypeDuckDb;
    }
}
