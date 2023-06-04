package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldDataType;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.export.FileExporter;
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
        for(FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            Statement stmt = connection.createStatement();
            if(fieldConfiguration.getType().equals("category"))
            {
                String sqlCreateTpye = "create type " + fieldConfiguration.getName() + " AS ENUM (" + fieldConfiguration.getValuesAsString() + ")";
                logger.trace("creating type [{}]", sqlCreateTpye);
                stmt.execute(sqlCreateTpye);
            }
        }
    }

    private void dropEnums() throws Exception
    {
        for(FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            if(fieldConfiguration.getType().equals("category"))
            {
                Statement stmt = connection.createStatement();
                String sqlDropType = "drop type if exists " + fieldConfiguration.getName();
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
        for(FieldConfiguration fieldConfiguration : configuration.getFields())
        {
            counter++;
            buffer.append(fieldConfiguration.getName());
            buffer.append(" ");

            if(fieldConfiguration.getType() == FieldType.CATEGORY)
            {
                buffer.append(fieldConfiguration.getName());
            }
            else
            {
                buffer.append(getDuckDbType(fieldConfiguration.getDataType()));
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

    private String getDuckDbType(FieldDataType dataType)
    {
        switch(dataType)
        {
            case LONG:
                return DataTypeDuckDb.BIGINT.toString();
            case BOOLEAN:
                return DataTypeDuckDb.BOOLEAN.toString();
            case DOUBLE:
                return DataTypeDuckDb.DOUBLE.toString();
            case FLOAT:
                return DataTypeDuckDb.DOUBLE.toString();
            case INTEGER:
                return DataTypeDuckDb.INTEGER.toString();
            default:
                return DataTypeDuckDb.VARCHAR.toString();
        }
    }

    public void getValueCounts(FieldConfiguration fieldConfiguration)
    {
        try
        {
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("select " + fieldConfiguration.getName() + ", count(1) as total from " + configuration.getTableName() +" group by " + fieldConfiguration.getName()))
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
