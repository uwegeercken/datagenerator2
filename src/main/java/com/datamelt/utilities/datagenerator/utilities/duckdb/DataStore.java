package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.export.FileExporter;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeJava;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DataStore
{
    private static Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final String SCHEMANAME = "main";
    private long numberOfRecordsInserted = 0;
    private DuckDBConnection connection;
    private DataStoreAppender appender;
    private DataConfiguration configuration;
    private FileExporter fileExporter;

    public DataStore(DataConfiguration configuration, FileExporter fileExporter) throws Exception
    {
        this.configuration = configuration;
        this.fileExporter = fileExporter;
        logger.debug("connecting to database [{}]", configuration.getDatabaseName());
        connection = (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:" + configuration.getDatabaseName());

        logger.trace("executing database cleanup [{}]", configuration.getDatabaseName());
        cleanupDatabase();

        logger.trace("creating database structure [{}]", configuration.getDatabaseName());
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
            Statement stmt = connection.createStatement();
            if(field.getType().equals("category"))
            {
                String sqlCreateTpye = "create type " + field.getName() + " AS ENUM (" + field.getValuesAsString() + ")";
                logger.trace("creating type [{}]", sqlCreateTpye);
                stmt.execute(sqlCreateTpye);
            }
        }
    }

    private void dropEnums() throws Exception
    {
        for(Field field : configuration.getFields())
        {
            if(field.getType().equals("category"))
            {
                Statement stmt = connection.createStatement();
                String sqlDropType = "drop type if exists " + field.getName();
                logger.trace("dropping type [{}]", sqlDropType);
                stmt.execute(sqlDropType);
            }
        }
    }

    private void createTable() throws Exception
    {
        Statement stmt = connection.createStatement();
        String sqlCreateTable = "create table " + configuration.getTableName() + " (" + getDataTypesAndNames() + ")";
        logger.trace("creating table [{}]", sqlCreateTable);
        stmt.execute(sqlCreateTable);
    }

    private void dropTable() throws Exception
    {
        Statement stmt = connection.createStatement();
        String sqlDropTable = "drop table if exists " + configuration.getTableName();
        logger.trace("dropping table [{}]", sqlDropTable);
        stmt.execute(sqlDropTable);
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

            if(field.getType().equals("category"))
            {
                buffer.append(field.getName());
            }
            else
            {
                buffer.append(getDuckDbType(DataTypeJava.valueOf(field.getDataType().toUpperCase())));
            }
            if (counter < configuration.getFields().size())
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

    public void exportToFile(String tablename, String outputFilename) throws Exception
    {
        fileExporter.export(connection, tablename, outputFilename);
    }

    private String getDuckDbType(DataTypeJava javaType)
    {
        switch(javaType)
        {
            case LONG:
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
