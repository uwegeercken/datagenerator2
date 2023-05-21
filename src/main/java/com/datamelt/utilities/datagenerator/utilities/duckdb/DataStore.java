package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.utilities.Row;
import com.datamelt.utilities.datagenerator.utilities.RowField;
import org.duckdb.DuckDBAppender;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class DataStore
{
    private static Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final String TABLENAME = "generateddata";

    private long numberOfRecordsInserted=0;
    private DuckDBConnection connection;
    private DataStoreAppender appender;
    //private PreparedStatement preparedStatement;
    public DataStore(List<Field> fields)
    {
        try
        {
            connection = (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:/home/uwe/development/datagenerator2/generateddata.duckdb");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" + getDataTypesAndNames(fields) + ")");
            String placeholders = "?,".repeat(fields.size()-1) + "?";
            appender = new DataStoreAppender(connection.createAppender("main", TABLENAME));

            //preparedStatement = connection.prepareStatement("INSERT INTO " + TABLENAME + " VALUES (" + placeholders + ");");

        }
        catch(Exception ex)
        {
            logger.error("error connecting to database error {}", ex.getMessage());
        }
    }

    private String getDataTypesAndNames(List<Field> fields)
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(Field field : fields)
        {
            counter++;
            buffer.append(field.getName());
            buffer.append(" ");
            buffer.append(getDuckDbType(DataTypeJava.valueOf(field.getDataType().toUpperCase())));
            if(counter< fields.size())
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
            try (ResultSet rs = stmt.executeQuery("select " + field.getName() + ", count(1) as total from " + TABLENAME +" group by " + field.getName()))
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
