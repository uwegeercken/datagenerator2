package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowField;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableField;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TableInsertLayout;
import com.datamelt.utilities.datagenerator.utilities.duckdb.structure.TreeNode;
import org.duckdb.DuckDBAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.datamelt.utilities.datagenerator.utilities.Constants.FIELD_DEVIDER_CHARACTER;

public class DataStoreAppender
{
    private static final Logger logger = LoggerFactory.getLogger(DataStoreAppender.class);
    private final DuckDBAppender appender;
    private final TableInsertLayout tableInsertLayout;

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
                Optional<TreeNode> rootNode = getRootNode(fieldName);
                if(rootNode.isEmpty())
                {
                    appendField(row.getField(fieldName));
                }
                else
                {
                    createStruct(rootNode.get(), row);
                }
            }
            appender.endRow();
        }
        catch(SQLException ex)
        {
            logger.error("error appending row [{}]. error {}", row.toString(), ex.getMessage());
        }
    }

    private Optional<TreeNode> getRootNode(String name)
    {
        for(TreeNode node : tableInsertLayout.getRootNodes())
        {
            if(node != null && node.getName().equals(name))
            {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    private void createStruct(TreeNode node, Row row) throws SQLException
    {
        appender.beginStruct();
        for (TableField field : node.getFields())
        {
            RowField<?> rowField = row.getField(node.getName() + FIELD_DEVIDER_CHARACTER + field.getName());
            appendField(rowField);
        }
        if (node.getChildren().size() > 0)
        {
            for(TreeNode child : node.getChildren())
            {
                getChildren(node.getName() + FIELD_DEVIDER_CHARACTER + child.getName(), child, row);
            }
        }
        appender.endStruct();
    }

    private void getChildren(String name, TreeNode node, Row row) throws SQLException
    {
        appender.beginStruct();
        for (TableField field : node.getFields())
        {
            RowField<?> rowField = row.getField(name + FIELD_DEVIDER_CHARACTER + field.getName());
            appendField(rowField);
        }
        if (node.getChildren().size() > 0)
        {
            for(TreeNode child : node.getChildren())
            {
                getChildren(name + FIELD_DEVIDER_CHARACTER + child.getName(), child, row);
            }
        }
        appender.endStruct();
    }

    public void beginRow() throws SQLException
    {
        appender.beginRow();
    }

    public void endRow() throws SQLException
    {
        appender.endRow();
    }

    public void flush()  throws SQLException
    {
        appender.flush();
    }

    private void appendRownumberField(long counter) throws SQLException
    {
        appendLong(counter);
    }

    private void appendField(RowField<?> field) throws SQLException
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

    private void appendInt(int value) throws SQLException
    {
        appender.append(value);
    }

    private void appendLong(long value) throws SQLException
    {
        appender.append(value);
    }

    private void appendString(String value) throws SQLException
    {
        appender.append(value);
    }

    private void appendDouble(double value) throws SQLException
    {
        appender.append(value);
    }

    private void appendFloat(float value) throws SQLException
    {
        appender.append(value);
    }
}
