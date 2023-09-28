package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.*;

public class TableStructure
{
    private static final String STRUCT_SPLIT_REGEX_CHARACTER = "\\.";
    private List<TreeNode> rootNodes = new ArrayList<>();
    private List<TableField> fields = new ArrayList<>();

    private StringBuilder createTableStatementBuilder = new StringBuilder();
    private String createTableStatement;
    public TableStructure(DataConfiguration dataConfiguration)
    {
        createStructs(dataConfiguration);
        inititalizeCreateTableStatement();
        buildStandardFieldsStatement();
        buildStructsStatement();
        finalizeCreateTableStatement();
        this.createTableStatement = createTableStatementBuilder.toString();
        System.out.println(createTableStatement);

    }

    private void inititalizeCreateTableStatement()
    {
        createTableStatementBuilder.append("create table " + "testtable (");
    }

    private void finalizeCreateTableStatement()
    {
        createTableStatementBuilder.append(")");
    }

    private void createStructs(DataConfiguration dataConfiguration)
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

    private List<TreeNode> getRootNodes()
    {
        return rootNodes;
    }

    private void buildStructsStatement()
    {
        for (int i=0;i< rootNodes.size();i++ )
        {
            createTableStatementBuilder.append(rootNodes.get(i).name).append(" struct(");
            buildStructFieldsStatement(rootNodes.get(i));
            createTableStatementBuilder.append(",");
            getChildren(rootNodes.get(i));
            createTableStatementBuilder.append(")");
            if(i< rootNodes.size()-1)
            {
                createTableStatementBuilder.append(",");
            }
        }
    }

    private void getChildren(TreeNode node)
    {
        int counter = 0;
        for (TreeNode childNode : node.children)
        {
            counter++;
            //TODO: process fields
            //StringBuilder builder = new StringBuilder();
            createTableStatementBuilder.append(childNode.name).append(" struct(");
            buildStructFieldsStatement(childNode);
            getChildren(childNode);
            createTableStatementBuilder.append(")");
            if(counter< node.children.size())
            {
                createTableStatementBuilder.append(",");
            }
        }
    }

    private void buildStructFieldsStatement(TreeNode node)
    {
        for(int i=0; i< node.fields.size();i++)
        {
            createTableStatementBuilder.append(node.fields.get(i).getName());
            createTableStatementBuilder.append(" ");
            createTableStatementBuilder.append(getDuckDbType(node.fields.get(i).getFieldType()));
            if(i<node.fields.size()-1)
            {
                createTableStatementBuilder.append(", ");
            }
        }
    }

    private void buildStandardFieldsStatement()
    {
        for(int i=0; i< fields.size();i++)
        {
            createTableStatementBuilder.append(fields.get(i).getName());
            createTableStatementBuilder.append(" ");
            createTableStatementBuilder.append(getDuckDbType(fields.get(i).getFieldType()));
            if(i<fields.size()-1)
            {
                createTableStatementBuilder.append(", ");
            }
        }
        if(rootNodes.size()>0) {
            createTableStatementBuilder.append(",");
        }
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
}