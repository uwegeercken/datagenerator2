package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.ProgramConfiguration;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.*;

public class TableStructure
{
    private static final String STRUCT_SPLIT_REGEX_CHARACTER = "\\.";
    private static final String COLUMN_ROWNUMBER_DATATYPE = "long";
    private static List<TreeNode> rootNodes;
    private static List<TableField> fields;
    private static StringBuilder createTableStatementBuilder;
    public static String getCreateTableStatement(ProgramConfiguration programConfiguration, DataConfiguration dataConfiguration)
    {
        createTableStatementBuilder = new StringBuilder();
        rootNodes = new ArrayList<>();
        fields = new ArrayList<>();
        createStructs(dataConfiguration);
        inititalizeCreateTableStatement(dataConfiguration.getTableName());
        buildRownumberField(programConfiguration.getGeneral().getRowNumberFieldName());
        buildStandardFieldsStatement();
        buildStructsStatement();
        finalizeCreateTableStatement();
        return createTableStatementBuilder.toString();
    }

    private static void inititalizeCreateTableStatement(String tableName)
    {
        createTableStatementBuilder.append("create table " + tableName +"(");
    }

    private static void buildRownumberField(String rownumberFieldName)
    {
        createTableStatementBuilder.append("\"")
                .append(rownumberFieldName)
                .append("\"")
                .append(" " + COLUMN_ROWNUMBER_DATATYPE + ",");
    }
    private static void finalizeCreateTableStatement()
    {
        createTableStatementBuilder.append(")");
    }

    private static void createStructs(DataConfiguration dataConfiguration)
    {
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            String[] fieldParts = fieldConfiguration.getName().split(STRUCT_SPLIT_REGEX_CHARACTER);
            if(fieldParts.length>1)
            {
                TreeNode rootNode = null;
                for (TreeNode node: rootNodes )
                {
                    if(node.name.equals(fieldParts[0]))
                    {
                        rootNode = node;
                        break;
                    }
                }
                if(rootNode==null)
                {
                    rootNode = new TreeNode(fieldParts[0]);
                    rootNodes.add(rootNode);
                }

                TreeNode currentNode = null;
                for(int i=1; i<fieldParts.length-1;i++)
                {
                    if(currentNode==null)
                    {
                        currentNode = rootNode.addChild(fieldParts[i]);
                    }
                    else
                    {
                        currentNode = currentNode.addChild(fieldParts[i]);
                    }
                }
                if(currentNode==null)
                {
                    rootNode.addField(new TableField(fieldParts[fieldParts.length-1], fieldConfiguration.getType()));
                }
                else
                {
                    currentNode.addField(new TableField(fieldParts[fieldParts.length-1], fieldConfiguration.getType()));
                }
            }
            else
            {
                fields.add(new TableField(fieldParts[0], fieldConfiguration.getType()));
            }
        }
    }

    private static List<TreeNode> getRootNodes()
    {
        return rootNodes;
    }

    private static void buildStructsStatement()
    {
        for (int i=0;i< rootNodes.size();i++ )
        {
            createTableStatementBuilder.append(rootNodes.get(i).name).append(" struct(");
            buildStructFieldsStatement(rootNodes.get(i));
            if(rootNodes.get(i).children.size()>0)
            {
                getChildren(rootNodes.get(i));
            }
            createTableStatementBuilder.append(")");
            if(i < rootNodes.size()-1)
            {
                createTableStatementBuilder.append(",");
            }
        }
    }

    private static void getChildren(TreeNode node)
    {
        for (int i=0;i<node.children.size();i++)
        {
            createTableStatementBuilder.append(node.children.get(i).name).append(" struct(");
            buildStructFieldsStatement(node.children.get(i));
            if(node.children.get(i).children.size()>0) {
                getChildren(node.children.get(i));
            }
            if(i<node.children.size()-1)
            {
                createTableStatementBuilder.append(")");
                createTableStatementBuilder.append(",");
            }
            else {
                createTableStatementBuilder.append(")");
            }
        }
    }

    private static void buildStructFieldsStatement(TreeNode node)
    {
        for(int i=0;i < node.fields.size();i++)
        {
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(node.fields.get(i).getName());
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(" ");
            createTableStatementBuilder.append(getDuckDbType(node.fields.get(i).getFieldType()));
            if(i<node.fields.size()-1)
            {
                createTableStatementBuilder.append(",");
            }
        }
        if(node.fields.size()>0 && node.children.size()>0)
        {
            createTableStatementBuilder.append(",");
        }
    }

    private static void buildStandardFieldsStatement()
    {
        for(int i=0;i < fields.size();i++)
        {
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(fields.get(i).getName());
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(" ");
            createTableStatementBuilder.append(getDuckDbType(fields.get(i).getFieldType()));
            if(i<fields.size()-1)
            {
                createTableStatementBuilder.append(",");
            }
        }
        if(rootNodes.size()>0) {
            createTableStatementBuilder.append(",");
        }
    }

    private static String getDuckDbType(FieldType type)
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
}