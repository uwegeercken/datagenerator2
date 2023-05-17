package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.DataGenerator;
import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.utilities.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DataStore
{
    private static Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final String TABLENAME = "generateddata";

    private Connection connection;
    private PreparedStatement preparedStatement;
    public DataStore()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:duckdb:/home/uwe/development/datagenerator2/generateddata.duckdb");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (gender VARCHAR, season varchar)");
            preparedStatement = connection.prepareStatement("INSERT INTO " + TABLENAME + " VALUES (?, ?);");

        }
        catch(Exception ex)
        {
            logger.error("error connecting to database");
        }
    }


    public void insert(Row row)
    {
        try
        {
            preparedStatement.setString(1, row.getFields().get(0).getValue().toString());
            preparedStatement.setString(2, row.getFields().get(1).getValue().toString());
            preparedStatement.execute();
        }
        catch(Exception ex)
        {
            logger.error("error executing insert statement");
        }
    }

    public void getDistinctValues(Field field, long totalRows)
    {
        try
        {
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("select " + field.getName() + ", count(1) as total from " + TABLENAME +" group by " + field.getName()))
            {
                while (rs.next()) {
                    System.out.println("value: " + rs.getString(1) + ", total: " + rs.getInt("total")*1.0d/totalRows*100);
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error executing insert statement");
        }
    }


}
