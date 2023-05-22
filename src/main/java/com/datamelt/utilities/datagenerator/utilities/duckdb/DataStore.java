package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.FieldValue;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.Row;
import com.datamelt.utilities.datagenerator.utilities.RowField;
import org.duckdb.DuckDBAppender;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DataStore
{
    private static Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final String SCHEMANAME = "main";
    private long numberOfRecordsInserted = 0;
    private DuckDBConnection connection;
    private DataStoreAppender appender;
    private MainConfiguration configuration;

    public DataStore(MainConfiguration configuration) throws Exception
    {
        this.configuration = configuration;
        connection = (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:" + configuration.getDatabaseName());

        cleanupDatabase();
        createDatabaseStructure();
        createAppender();
    }

    private void cleanupDatabase() throws Exception
    {
        dropTable();
        dropEnums();
    }

    private void createDatabaseStructure() throws Exception
    {
        createEnums();
        createTable();
    }

    private void createEnums() throws Exception
    {
        for(Field field : configuration.getFields())
        {
            if(DataTypeJava.valueOf(field.getDataType().toUpperCase()) == DataTypeJava.STRING)
            {
                Statement stmt = connection.createStatement();
                stmt.execute("create type " + field.getName() + " AS ENUM (" + field.getValuesAsString() + ");");
            }
        }
    }

    private void dropEnums() throws Exception
    {
        for(Field field : configuration.getFields())
        {
            if(DataTypeJava.valueOf(field.getDataType().toUpperCase()) == DataTypeJava.STRING)
            {
                Statement stmt = connection.createStatement();
                stmt.execute("drop type " + field.getName());
            }
        }
    }

    private void createTable() throws Exception
    {
        Statement stmt = connection.createStatement();
        stmt.execute("create table " + configuration.getTableName() + " (" + getDataTypesAndNames() + ")");
    }

    private void dropTable() throws Exception
    {
        Statement stmt = connection.createStatement();
        stmt.execute("drop table " + configuration.getTableName());
    }

    private void createAppender() throws Exception
    {
        this.appender = new DataStoreAppender(connection.createAppender(SCHEMANAME, configuration.getTableName()));
    }

    private String getDataTypesAndNames() throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(Field field : configuration.getFields())
        {
            counter++;
            buffer.append(field.getName());
            buffer.append(" ");

            if(DataTypeJava.valueOf(field.getDataType().toUpperCase()) == DataTypeJava.STRING)
            {
                buffer.append(field.getName());
            }
            else
            {
                buffer.append(getDuckDbType(DataTypeJava.valueOf(field.getDataType().toUpperCase())));
            }
            if(counter < configuration.getFields().size())
            {
                buffer.append(", ");
            }
        }
        return buffer.toString();
    }

    public void insert(Row row)
    {
        appender.append(row);
    }

    public void flush() throws Exception
    {
        appender.flush();
    }

    public String getDuckDbType(DataTypeJava javaType)
    {
        switch(javaType)
        {
            case  LONG:
                return DataTypeDuckDb.BIGINT.toString();
            case BOOLEAN:
                return DataTypeDuckDb.BOOLEAN.toString();
            case DATE:
                return DataTypeDuckDb.DATE.toString();
            case DOUBLE:
                return DataTypeDuckDb.DOUBLE.toString();
            case INTEGER:
                return DataTypeDuckDb.INTEGER.toString();
            default:
                return DataTypeDuckDb.VARCHAR.toString();
        }
    }

    public void getValueCounts(Field field)
    {
        try
        {
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("select " + field.getName() + ", count(1) as total from " + configuration.getTableName() +" group by " + field.getName()))
            {
                while (rs.next()) {
                    System.out.println("value: " + rs.getString(1) + ", total: " + rs.getDouble("total") / numberOfRecordsInserted * 100);
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error executing insert statement");
        }
    }
}
