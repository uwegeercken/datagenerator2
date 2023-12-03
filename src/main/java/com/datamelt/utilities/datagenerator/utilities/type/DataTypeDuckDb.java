package com.datamelt.utilities.datagenerator.utilities.type;

public enum DataTypeDuckDb
{
    BIGINT,
    BOOLEAN,
    DATE,
    TIMESTAMP,
    DOUBLE,
    INTEGER,
    LONG,
    VARCHAR;

    public static DataTypeDuckDb getDataType(String key)
    {
        for(DataTypeDuckDb type : DataTypeDuckDb.values())
        {
            if(key.trim().toUpperCase().equals(type.name()))
            {
                return type;
            }
        }
        return null;
    }

}
