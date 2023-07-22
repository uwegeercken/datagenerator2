package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowField;
import org.duckdb.DuckDBAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStoreAppender
{
    private static Logger logger = LoggerFactory.getLogger(DataStoreAppender.class);
    private DuckDBAppender appender;
    private Map<String,Struct> structs;

    public DataStoreAppender(DuckDBAppender appender, Map<String,Struct> structs)
    {
        this.appender = appender;
        this.structs = structs;
    }

    public void append(Row row, long counter)
    {
        List<String> processedStructs = new ArrayList<>();
        try
        {
            appender.beginRow();
            appendRownumberField(counter);
            for(RowField field : row.getFields())
            {
                String[] nameParts = field.getName().split("\\.");
                if(nameParts.length==1)
                {
                    appendField(field);
                }
                else
                {
                    if(structs.containsKey(nameParts[0]) && !processedStructs.contains(nameParts[0]))
                    {
                        Struct struct = structs.get(nameParts[0]);
                        String value = createStruct(struct, row);
                        appendString(value);
                        processedStructs.add(nameParts[0]);
                    }
                }
            }
            appender.endRow();
        }
        catch(Exception ex)
        {
            logger.error("error appending row [{}]. error {}", row.toString(), ex.getMessage());
        }
    }

    private String createStruct(Struct struct, Row row)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        int counter = 0;
        for(StructField field : struct.getFields())
        {
            counter++;
            RowField<?> rowField = row.getField(struct.getName() + "." + field.getName());
            buffer.append("\"");
            buffer.append(field.getName());
            buffer.append("\": '");
            buffer.append(rowField.getValue());
            buffer.append("'");

            if (counter < struct.getFields().size())
            {
                buffer.append(", ");
            }
        }
        buffer.append("}");
        return buffer.toString();
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
            appendString((String) field.getValue());
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

}
