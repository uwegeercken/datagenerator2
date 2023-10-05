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
    private static final List<TreeNode> rootNodes = new ArrayList<>();
    private static final List<TableField> fields= new ArrayList<>();

    private static int fieldNumber;
    private static final Map<String, Integer> fieldMap = new HashMap<>();
    private static final StringBuilder createTableStatementBuilder = new StringBuilder();
    private static final StringBuilder appenderStatementBuilder = new StringBuilder();

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
        appenderStatementBuilder.append(rownumberFieldName).append(",");
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
                int lastRegexCharacter = fieldConfiguration.getName().lastIndexOf(".");
                String allStructsName = fieldConfiguration.getName().substring(0, lastRegexCharacter);
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
                    rootNode = new TreeNode(allStructsName, fieldParts[0]);
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
            appenderStatementBuilder.append("{");
            buildStructFieldsStatement(rootNodes.get(i));
            if(rootNodes.get(i).children.size()>0)
            {
                getChildren(rootNodes.get(i));
            }
            createTableStatementBuilder.append(")");
            appenderStatementBuilder.append("}");
            if(i < rootNodes.size()-1)
            {
                createTableStatementBuilder.append(",");
                appenderStatementBuilder.append(",");
            }
        }
    }

    private static void getChildren(TreeNode node)
    {
        for (int i=0;i<node.children.size();i++)
        {
            createTableStatementBuilder.append(node.children.get(i).name).append(" struct(");
            appenderStatementBuilder.append("{");
            buildStructFieldsStatement(node.children.get(i));
            if(node.children.get(i).children.size()>0) {
                getChildren(node.children.get(i));
            }
            if(i<node.children.size()-1)
            {
                createTableStatementBuilder.append(")");
                createTableStatementBuilder.append(",");
                appenderStatementBuilder.append("}");
                appenderStatementBuilder.append(",");
            }
            else {
                createTableStatementBuilder.append(")");
                appenderStatementBuilder.append("}");
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
            appenderStatementBuilder.append(node.allStructsName + "." + node.fields.get(i).getName());
            if(i<node.fields.size()-1)
            {
                createTableStatementBuilder.append(",");
                appenderStatementBuilder.append(",");
            }
        }
        if(node.fields.size()>0 && node.children.size()>0)
        {
            createTableStatementBuilder.append(",");
            appenderStatementBuilder.append(",");
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
            appenderStatementBuilder.append(fields.get(i).getName());
            if(i<fields.size()-1)
            {
                createTableStatementBuilder.append(",");
                appenderStatementBuilder.append(",");
            }
        }
        if(rootNodes.size()>0) {
            createTableStatementBuilder.append(",");
            appenderStatementBuilder.append(",");
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