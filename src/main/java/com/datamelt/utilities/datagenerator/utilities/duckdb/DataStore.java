package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.*;
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
    private static final String COLUMN_ROWNUMBER_DATATYPE = "long";
    private long numberOfRecordsInserted = 0;
    private DuckDBConnection connection;
    private DataStoreAppender appender;
    private ProgramConfiguration programConfiguration;
    private DataConfiguration dataConfiguration;
    private FileExporter fileExporter;

    public DataStore(ProgramConfiguration programConfiguration, DataConfiguration dataConfiguration, FileExporter fileExporter) throws Exception
    {
        this.programConfiguration = programConfiguration;
        this.dataConfiguration = dataConfiguration;
        this.fileExporter = fileExporter;
        logger.debug("connecting to database [{}]", dataConfiguration.getDatabaseName());
        connection = (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:" + dataConfiguration.getDatabaseName());

        logger.trace("executing database cleanup [{}]", dataConfiguration.getDatabaseName());
        cleanupDatabase();

        logger.trace("creating database structure [{}]", dataConfiguration.getDatabaseName());
        createDatabaseStructure();
        createAppender();
    }

    private void cleanupDatabase() throws Exception
    {
        dropTable();
        //dropEnums();
    }

    private void createDatabaseStructure() throws Exception
    {
        //createEnums();
        createTable();
    }

    private void createEnums() throws Exception
    {
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
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
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
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
        String sqlCreateTable = "create table " + dataConfiguration.getTableName() + " (" + getDataTypesAndNames() + ")";
        logger.trace("creating table [{}]", sqlCreateTable);
        stmt.execute(sqlCreateTable);
    }

    private void dropTable() throws Exception
    {
        Statement stmt = connection.createStatement();
        String sqlDropTable = "drop table if exists " + dataConfiguration.getTableName();
        logger.trace("dropping table [{}]", sqlDropTable);
        stmt.execute(sqlDropTable);
    }

    private void createAppender() throws Exception
    {
        this.appender = new DataStoreAppender(connection.createAppender(SCHEMANAME, dataConfiguration.getTableName()));
    }

    private String getDataTypesAndNames() throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        buffer.append(programConfiguration.getGeneral().getRowNumberFieldName() + " " + COLUMN_ROWNUMBER_DATATYPE + ", ");
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            counter++;
            buffer.append("\"");
            buffer.append(fieldConfiguration.getName());
            buffer.append("\" ");
            buffer.append(getDuckDbType(fieldConfiguration.getType()));
            if (counter < dataConfiguration.getFields().size())
            {
                buffer.append(", ");
            }

        }
        return buffer.toString();
    }

    public void insert(Row row, long counter)
    {
        appender.append(row, counter);
    }

    public void flush() throws Exception
    {
        appender.flush();
    }

    public void exportToFile(String tablename, String exportFilename) throws Exception
    {
        fileExporter.export(connection, tablename, exportFilename);
    }

    private String getDuckDbType(FieldType type)
    {
        switch(type)
        {
            case CATEGORY:
                return DataTypeDuckDb.VARCHAR.toString();
            case RANDOMDOUBLE:
                return DataTypeDuckDb.DOUBLE.toString();
            case RANDOMINTEGER:
                return DataTypeDuckDb.INTEGER.toString();
            case RANDOMLONG:
                return DataTypeDuckDb.LONG.toString();
            case RANDOMDATE:
                return DataTypeDuckDb.DATE.toString();
            case DATEREFERENCE:
                return DataTypeDuckDb.VARCHAR.toString();
            case RANDOMSTRING:
                return DataTypeDuckDb.VARCHAR.toString();
            case REGULAREXPRESSION:
                return DataTypeDuckDb.VARCHAR.toString();
            default:
                return DataTypeDuckDb.VARCHAR.toString();
        }
    }

    public void getValueCounts(FieldConfiguration fieldConfiguration)
    {
        try
        {
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("select " + fieldConfiguration.getName() + ", count(1) as total from " + dataConfiguration.getTableName() +" group by " + fieldConfiguration.getName()))
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
