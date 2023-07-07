package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowField;
import org.duckdb.DuckDBAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataStoreAppender
{
    private static Logger logger = LoggerFactory.getLogger(DataStoreAppender.class);
    private DuckDBAppender appender;
    private long counter = 0;
    private long errorCounter = 0;

    public DataStoreAppender(DuckDBAppender appender)
    {
        this.appender = appender;
    }

    public void append(Row row, long counter)
    {
        try
        {
            appender.beginRow();
            appendRownumberField(counter);
            for(RowField field : row.getFields())
            {
                appendField(field);
            }
            appender.endRow();
            counter++;
        }
        catch(Exception ex)
        {
            errorCounter++;
            logger.error("error appending row [{}]. error {}", row.toString(), ex.getMessage());
        }
    }

    public void beginRow() throws Exception
    {
        appender.beginRow();
    }

    public void endRow() throws Exception
    {
        appender.endRow();
    }

    public void flush() throws Exception
    {
        appender.flush();
    }

    private void appendRownumberField(long counter) throws Exception
    {
        appendLong(counter);
    }

    private void appendField(RowField field) throws Exception
    {
        if(field.getValue() instanceof Integer)
        {
            appendInt((Integer) field.getValue());
        }
        else if(field.getValue() instanceof Long)
        {
            appendLong((Long) field.getValue());
        }
        else if(field.getValue() instanceof Float)
        {
            appendFloat((Float) field.getValue());
        }
        else if(field.getValue() instanceof Double)
        {
            appendDouble((Double) field.getValue());
        }
        else if(field.getValue() instanceof String)
        {
            appendString( (String) field.getValue());
        }

    }

    private void appendInt(int value) throws Exception
    {
        appender.append(value);
    }

    private void appendLong(long value) throws Exception
    {
        appender.append(value);
    }

    private void appendString(String value) throws Exception
    {
        appender.append(value);
    }

    private void appendDouble(double value) throws Exception
    {
        appender.append(value);
    }

    private void appendFloat(float value) throws Exception
    {
        appender.append(value);
    }

    public long getCounter()
    {
        return counter;
    }
}
