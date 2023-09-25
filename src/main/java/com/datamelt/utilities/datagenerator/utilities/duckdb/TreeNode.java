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

    private void addField(String name)
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


    public static void main(String[] args)
    {
        List<String> fields = new ArrayList<>();

        fields.add("person.address.street.name");
        fields.add("person.address.street.number");
        fields.add("job.company.location.name");

        List<TreeNode> rootNodes = new ArrayList<>();

        for(String field : fields)
        {
            String[] fieldParts = field.split("\\.");
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
            currentNode.addField(fieldParts[fieldParts.length-1]);
        }

        for (TreeNode node: rootNodes )
        {
            System.out.println("start node: " + node.name);
            getChildren(node);
        }

        System.out.println();

    }

    public static void getChildren(TreeNode node )
    {
        Iterator<TreeNode> iterator = node.getChildren().iterator();
        while(iterator.hasNext())
        {
            TreeNode childNode = iterator.next();
            //TODO: process fields
            System.out.println("start node child: " + childNode.name);
            getChildren(childNode);
        }
    }


}
