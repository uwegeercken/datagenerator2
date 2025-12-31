package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.*;
import com.datamelt.utilities.datagenerator.error.Success;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.export.FileExporter;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableInsertLayout;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableLayout;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

public class DataStore
{
    private static Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final String SCHEMANAME = "main";
    private long numberOfRecordsInserted = 0;
    private final DuckDBConnection connection;
    private DataStoreAppender appender;
    private final ProgramConfiguration programConfiguration;
    private final DataConfiguration dataConfiguration;
    private final FileExporter fileExporter;

    TableInsertLayout tableInsertLayout;

    public DataStore(ProgramConfiguration programConfiguration, DataConfiguration dataConfiguration, FileExporter fileExporter) throws SQLException
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

    private void cleanupDatabase() throws SQLException
    {
        dropTable();
        //dropEnums();
    }

    private void createDatabaseStructure() throws SQLException
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

    private void createTable() throws SQLException
    {

        Statement stmt = connection.createStatement();
        String createTableStatement = TableLayout.getCreateTableStatement(programConfiguration, dataConfiguration);
        tableInsertLayout = new TableInsertLayout(TableLayout.getFields(), TableLayout.getRootNodes());
        logger.debug("creating table [{}]", dataConfiguration.getTableName());
        logger.trace("creating table statement [{}]", createTableStatement);
        stmt.execute(createTableStatement);
    }

    private void dropTable() throws SQLException
    {
        Statement stmt = connection.createStatement();
        String sqlDropTable = "drop table if exists " + dataConfiguration.getTableName();
        logger.trace("dropping table [{}]", sqlDropTable);
        stmt.execute(sqlDropTable);
    }

    private void createAppender() throws SQLException
    {
        this.appender = new DataStoreAppender(connection.createAppender(SCHEMANAME, dataConfiguration.getTableName()), tableInsertLayout);
    }


    public void insert(Row row)
    {
        appender.append(row, numberOfRecordsInserted);
        numberOfRecordsInserted++;
    }

    public void flush() throws SQLException
    {
        appender.flush();
    }

    public void exportToFile(String tablename, String exportFilename) throws SQLException
    {
        fileExporter.export(connection, tablename, exportFilename);
    }

    public FieldStatistics getValueCounts(FieldConfiguration fieldConfiguration)
    {
        FieldStatistics statistics = new FieldStatistics(fieldConfiguration.getName(), fieldConfiguration.getType());
        try
        {
            Statement stmt = connection.createStatement();
            String sqlDistinct = "select count(distinct " + fieldConfiguration.getName() + ") as distinctValues from " + dataConfiguration.getTableName();
            ResultSet rsDistinct = stmt.executeQuery(sqlDistinct);
            rsDistinct.next();
            long numberOfDistinctValues = rsDistinct.getLong("distinctValues");
            rsDistinct.close();
            statistics.setNumberOfDistinctValues(numberOfDistinctValues);

            if(numberOfDistinctValues <= 50)
            {
                String sql = "select " + fieldConfiguration.getName() + " as fieldvalue, count(1) as total from " + dataConfiguration.getTableName() + " group by " + fieldConfiguration.getName();
                ResultSet rsCounts = stmt.executeQuery(sql);
                while (rsCounts.next())
                {
                    BigDecimal valueCount = new BigDecimal(rsCounts.getDouble("total") / numberOfRecordsInserted * 100);
                    valueCount = valueCount.setScale(2, RoundingMode.HALF_UP);
                    statistics.addValueCount(rsCounts.getString("fieldvalue"), valueCount.doubleValue());
                }
            }
        }
        catch(SQLException ex)
        {
            logger.error("error collecting value counts for field [{}]. error [{}]", fieldConfiguration.getName(), ex.getMessage());
        }
        return statistics;
    }
}
