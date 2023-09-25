package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import java.util.*;

public class TableStructure
{
    List<TreeNode> rootNodes = new ArrayList<>();
    List<String> fields = new ArrayList<>();

    public TableStructure(DataConfiguration dataConfiguration)
    {
        createStructs(dataConfiguration);
        processRootNode();

    }

    private void createStructs(DataConfiguration dataConfiguration)
    {
        for(FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            String[] fieldParts = fieldConfiguration.getName().split("\\.");
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
                    rootNode.addField(fieldParts[fieldParts.length-1]);
                }
                else
                {
                    currentNode.addField(fieldParts[fieldParts.length-1]);
                }
            }
            else
            {
                fields.add(fieldParts[0]);
            }
        }
    }

    public List<TreeNode> getRootNodes()
    {
        return rootNodes;
    }

    private void processRootNode()
    {
        for (TreeNode node: rootNodes )
        {
            System.out.println("start node: " + node.name);
            System.out.println("start node fields: " + node.getFields());
            TreeNode.getChildren(node);
        }
    }
}