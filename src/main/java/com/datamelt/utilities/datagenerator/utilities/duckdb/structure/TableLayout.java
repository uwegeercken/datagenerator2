package com.datamelt.utilities.datagenerator.utilities.duckdb.structure;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.ProgramConfiguration;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.*;

import static com.datamelt.utilities.datagenerator.utilities.Constants.FIELD_SPLIT_REGEX_CHARACTER;

public class TableLayout
{

    private static final String COLUMN_ROWNUMBER_DATATYPE = "long";
    private static final List<TreeNode> rootNodes = new ArrayList<>();
    private static final List<TableField> fields= new ArrayList<>();
    private static final StringBuilder createTableStatementBuilder = new StringBuilder();

    public static String getCreateTableStatement(ProgramConfiguration programConfiguration, DataConfiguration dataConfiguration)
    {
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
                .append(" ").append(COLUMN_ROWNUMBER_DATATYPE)
                .append(",");
    }
    private static void finalizeCreateTableStatement()
    {
        createTableStatementBuilder.append(")");
    }

    private static void createStructs(DataConfiguration dataConfiguration)
    {
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            String[] fieldParts = fieldConfiguration.getName().split(FIELD_SPLIT_REGEX_CHARACTER);

            if(fieldParts.length>1)
            {
                TreeNode rootNode = null;
                for (TreeNode node: rootNodes )
                {
                    if(node.getName().equals(fieldParts[0]))
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

    public static List<TreeNode> getRootNodes()
    {
        return rootNodes;
    }

    public static List<TableField> getFields() { return fields;}

    private static void buildStructsStatement()
    {
        for (int i=0;i< rootNodes.size();i++ )
        {
            createTableStatementBuilder.append(rootNodes.get(i).getName()).append(" struct(");
            buildStructFieldsStatement(rootNodes.get(i));
            if(rootNodes.get(i).getChildren().size()>0)
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
        for (int i=0;i<node.getChildren().size();i++)
        {
            createTableStatementBuilder.append(node.getChildren().get(i).getName()).append(" struct(");
            buildStructFieldsStatement(node.getChildren().get(i));
            if(node.getChildren().get(i).getChildren().size()>0) {
                getChildren(node.getChildren().get(i));
            }
            if(i<node.getChildren().size()-1)
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
        for(int i=0;i < node.getFields().size();i++)
        {
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(node.getFields().get(i).getName());
            createTableStatementBuilder.append("\"");
            createTableStatementBuilder.append(" ");
            createTableStatementBuilder.append(getDuckDbType(node.getFields().get(i).getFieldType()));
            if(i<node.getFields().size()-1)
            {
                createTableStatementBuilder.append(",");
            }
        }
        if(node.getFields().size()>0 && node.getChildren().size()>0)
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