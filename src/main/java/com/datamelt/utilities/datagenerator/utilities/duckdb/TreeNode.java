package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.*;

public class TreeNode implements Comparable<TreeNode>
{
    private String name;
    private TreeNode parent;
    private LinkedList<TreeNode> children;

    private List<TableField> fields;

    public TreeNode(String name) {
        this.name = name;
        this.children = new LinkedList<TreeNode>();
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

    public void addField(TableField field)
    {
        fields.add(field);
    }

    public List<TableField> getFields()
    {
        return fields;
    }

    public String getName()
    {
        return name;
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public LinkedList<TreeNode> getChildren()
    {
        return children;
    }
    @Override
    public int compareTo(TreeNode treeNode)
    {
        if(this.name.equals(treeNode.name))
            return 0;
        return 1;
    }
}
