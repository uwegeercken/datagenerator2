package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.*;
import com.datamelt.utilities.datagenerator.export.FileExporter;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.appender = new DataStoreAppender(connection.createAppender(SCHEMANAME, dataConfiguration.getTableName()),getStructs());
    }

    private String getDataTypesAndNames() throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        buffer.append(programConfiguration.getGeneral().getRowNumberFieldName() + " " + COLUMN_ROWNUMBER_DATATYPE + ", ");
        Map<String, Struct> structs = getStructs();
        List<String> processedStructs = new ArrayList<>();
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            counter++;
            String[] namesParts = fieldConfiguration.getName().split("\\.");
            if(namesParts.length==1)
            {
                buffer.append("\"");
                buffer.append(fieldConfiguration.getName());
                buffer.append("\" ");
                buffer.append(getDuckDbType(fieldConfiguration.getType()));

                if (counter < dataConfiguration.getFields().size())
                {
                    buffer.append(", ");
                }
            }
            else
            {
                if(structs.containsKey(namesParts[0]) && !processedStructs.contains(namesParts[0]))
                {
                    Struct struct = structs.get(namesParts[0]);
                    buffer.append(createStruct(struct));
                    processedStructs.add(namesParts[0]);

                    if (counter < dataConfiguration.getFields().size())
                    {
                        buffer.append(", ");
                    }
                }
            }
        }
        return buffer.toString();
    }

    private Map<String, Struct> getStructs()
    {
        //TODO: remove after testing
        TableStructure structure = new TableStructure(dataConfiguration);
        Map<String, Struct> structs = new HashMap<>();
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            String[] namesParts = fieldConfiguration.getName().split("\\.");
            if(namesParts.length==2)
            {
                if(structs.containsKey(namesParts[0]))
                {
                    Struct struct = structs.get(namesParts[0]);
                    struct.addField(namesParts[1], fieldConfiguration.getType());
                }
                else
                {
                    Struct struct = new Struct(namesParts[0]);
                    struct.addField(namesParts[1], fieldConfiguration.getType());
                    structs.put(namesParts[0], struct);
                }
            }
        }
        return structs;
    }
    private String createStruct(Struct struct)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(struct.getName());
        buffer.append(" struct(");
        int counter = 0;
        for(TableField field : struct.getFields())
        {
            counter++;
            buffer.append("\"");
            buffer.append(field.getName());
            buffer.append("\" ");
            buffer.append(getDuckDbType(field.getFieldType()));

            if (counter < struct.getFields().size())
            {
                buffer.append(", ");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    public void insert(Row row, long rowNumber)
    {
        appender.append(row, rowNumber);
        numberOfRecordsInserted++;
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

    public FieldStatistics getValueCounts(FieldConfiguration fieldConfiguration)
    {
        FieldStatistics statistics = new FieldStatistics(fieldConfiguration.getName());
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
        catch(Exception ex)
        {
            logger.error("error collecting value counts for field [{}]. error [{}]", fieldConfiguration.getName(), ex.getMessage());
        }
        return statistics;
    }
}
