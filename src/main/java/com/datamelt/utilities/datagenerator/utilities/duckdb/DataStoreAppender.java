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
import java.util.Optional;

import static com.datamelt.utilities.datagenerator.utilities.Constants.FIELD_DEVIDER_CHARACTER;

public class DataStoreAppender
{
    private static final Logger logger = LoggerFactory.getLogger(DataStoreAppender.class);
    private final DuckDBAppender appender;
    private final TableInsertLayout tableInsertLayout;
    private long appendFailureCount = 0;
    private boolean lastAppendSucceeded = true;

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
                    RowField field = row.getField(fieldName)
                            .orElseThrow(() -> new SQLException("field not found: " + fieldName));
                    appendField(field);
                }
                else
                {
                    appendStruct(rootNode.get().getName(), rootNode.get(), row);
                }
            }
            appender.endRow();
            lastAppendSucceeded = true;
        }
        catch(SQLException ex)
        {
            appendFailureCount++;
            lastAppendSucceeded = false;
            logger.warn("error appending row [{}], skipping. error: {}", row.toString(), ex.getMessage());
            try
            {
                appender.endRow();
            }
            catch(Exception ex2)
            {
                logger.error("error calling endRow after failed append: {}", ex2.getMessage());
            }
        }
    }

    private Optional<TreeNode> getRootNode(String name)
    {
        return tableInsertLayout.getRootNodes().stream()
                .filter(node -> node != null && node.getName().equals(name))
                .findFirst();
    }

    private void appendStruct(String name, TreeNode node, Row row) throws SQLException
    {
        appender.beginStruct();
        for (TableField field : node.getFields())
        {
            RowField rowField = row.getField(name + FIELD_DEVIDER_CHARACTER + field.getName())
                    .orElseThrow(() -> new SQLException("field not found: " + name + FIELD_DEVIDER_CHARACTER + field.getName()));
            appendField(rowField);
        }
        if (node.getChildren().size() > 0)
        {
            for(TreeNode child : node.getChildren())
            {
                appendStruct(name + FIELD_DEVIDER_CHARACTER + child.getName(), child, row);
            }
        }
        appender.endStruct();
    }

    public long getAppendFailureCount()
    {
        return appendFailureCount;
    }

    public boolean getLastAppendSucceeded()
    {
        return lastAppendSucceeded;
    }

    public void flush()  throws SQLException
    {
        appender.flush();
    }

    private void appendRownumberField(long counter) throws SQLException
    {
        appender.append(counter);
    }

    private void appendField(RowField field) throws SQLException
    {
        switch (field.getValue()) {
            case Integer i -> appender.append(i);
            case Long l    -> appender.append(l);
            case Double d  -> appender.append(d);
            case Float f   -> appender.append(f);
            case Boolean b -> appender.append(b);
            case String  s -> appender.append(s);
            default -> throw new SQLException("Unsupported field type: " + field.getValue().toString());
        }
    }
}
