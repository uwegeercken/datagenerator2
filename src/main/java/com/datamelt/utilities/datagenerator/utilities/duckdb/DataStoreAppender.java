package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowField;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableField;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableInsertLayout;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TreeNode;
import org.duckdb.DuckDBAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.datamelt.utilities.datagenerator.utilities.Constants.FIELD_DEVIDER_CHARACTER;

public class DataStoreAppender
{
    private static Logger logger = LoggerFactory.getLogger(DataStoreAppender.class);
    private DuckDBAppender appender;
    private TableInsertLayout tableInsertLayout;

    public DataStoreAppender(DuckDBAppender appender, TableInsertLayout tableInsertLayout)
    {
        this.appender = appender;
        this.tableInsertLayout = tableInsertLayout;
    }

    public void append(Row row, long counter)
    {
        try
        {
            appender.beginRow();
            appendRownumberField(counter);

            for(String fieldName : tableInsertLayout.getFieldNames())
            {
                TreeNode node = getRootNode(fieldName);
                if(node==null)
                {
                    appendField(row.getField(fieldName));
                }
                else
                {
                    String value = createStruct(node, row);
                    appendString(value);
                }
            }
            appender.endRow();
        }
        catch(Exception ex)
        {
            logger.error("error appending row [{}]. error {}", row.toString(), ex.getMessage());
        }
    }

    private TreeNode getRootNode(String name)
    {
        for(TreeNode node : tableInsertLayout.getRootNodes())
        {
            if(node.getName().equals(name))
            {
                return node;
            }
        }
        return null;
    }

    private String createStruct(TreeNode node, Row row)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        int counter = 0;
        for(TableField field : node.getFields())
        {
            counter++;
            RowField<?> rowField = row.getField(node.getName() + FIELD_DEVIDER_CHARACTER + field.getName());
            buffer.append("\"");
            buffer.append(field.getName());
            buffer.append("\": '");
            buffer.append(rowField.getValue());
            buffer.append("'");
            if (counter < node.getFields().size())
            {
                buffer.append(", ");
            }
        }
        if(node.getChildren().size()>0)
        {
            buffer.append(",");
            getChildren(node.getName(), node, row, buffer);
        }
        buffer.append("}");
        return buffer.toString();
    }

    private static void getChildren(String name, TreeNode node, Row row, StringBuffer buffer)
    {
        for (int i=0;i<node.getChildren().size();i++)
        {
            String fullName = name + FIELD_DEVIDER_CHARACTER + node.getChildren().get(i).getName();
            buffer.append("\"");
            buffer.append(node.getChildren().get(i).getName());
            buffer.append("\":");
            buffer.append(" {");
            buildStructFieldsStatement(fullName, node.getChildren().get(i), row, buffer);
            if(node.getChildren().get(i).getChildren().size()>0) {
                getChildren(fullName, node.getChildren().get(i), row, buffer);
            }
            if(i<node.getChildren().size()-1)
            {
                buffer.append("}");
                buffer.append(",");
            }
            else {
                buffer.append("}");
            }
        }
    }

    private static void buildStructFieldsStatement(String name, TreeNode node, Row row, StringBuffer buffer)
    {
        for(int i=0;i < node.getFields().size();i++)
        {
            RowField<?> rowField = row.getField(name + FIELD_DEVIDER_CHARACTER + node.getFields().get(i).getName());
            buffer.append("\"");
            buffer.append(node.getFields().get(i).getName());
            buffer.append("\": '");
            buffer.append(rowField.getValue());
            buffer.append("'");
            if(i<node.getFields().size()-1)
            {
                buffer.append(",");
            }
        }
        if(node.getFields().size()>0 && node.getChildren().size()>0)
        {
            buffer.append(",");
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

    private void appendField(RowField<?> field) throws Exception
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
