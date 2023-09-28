package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.*;

public class TreeNode implements Comparable<TreeNode>
{
    String name;
    TreeNode parent;
    TreeSet<TreeNode> children;

    List<TableField> fields;

    public TreeNode(String name) {
        this.name = name;
        this.children = new TreeSet<TreeNode>();
        this.fields = new ArrayList<>();
    }

    public TreeNode addChild(String child) {
        TreeNode childNode = null;
        for(TreeNode node : this.children)
        {
            if(node.name.equals(child))
            {
                childNode = node;
            }
        }
        if(childNode==null)
        {
            childNode = new TreeNode(child);
            childNode.parent = this;
            this.children.add(childNode);
        }
        return childNode;
    }

    public TreeNode addChildren(String name)
    {
        String[] children = name.split("\\.");
        TreeNode lastChild = null;
        for(int i=1;i<children.length-1;i++)
        {
            if(lastChild == null)
            {
                lastChild = addChild(children[i]);
            }
            else
            {
                lastChild = lastChild.addChild(children[i]);
            }
        }
        return lastChild;
    }



    public void addField(TableField field)
    {
        fields.add(field);
    }

    public List<TableField> getFields()
    {
        return fields;
    }

    @Override
    public int compareTo(TreeNode treeNode)
    {
        if(this.name.equals(treeNode.name))
            return 0;
        return 1;

    }



    private String getDuckDbType(FieldType type) {
        switch (type) {
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
