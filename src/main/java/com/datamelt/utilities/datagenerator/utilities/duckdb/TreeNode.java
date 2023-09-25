package com.datamelt.utilities.datagenerator.utilities.duckdb;

import java.util.*;

public class TreeNode implements Comparable<TreeNode>
{
    String name;
    TreeNode parent;
    TreeSet<TreeNode> children;

    List<String> fields;

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
        lastChild.addField(children[children.length-1]);
        return lastChild;
    }

    public TreeSet<TreeNode> getChildren()
    {
        return children;
    }

    public void addField(String name)
    {
        fields.add(name);
    }

    public List<String> getFields()
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

    public static void getChildren(TreeNode node)
    {
        for (TreeNode childNode : node.getChildren())
        {
            //TODO: process fields
            System.out.println("child: " + childNode.name);
            System.out.println("child fields: " + childNode.getFields());
            getChildren(childNode);
        }
    }


}
