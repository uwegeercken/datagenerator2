package com.datamelt.utilities.datagenerator.utilities.duckdb;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import java.util.*;

public class TableStructure
{
    private TreeNode base;

    public void addStruct(FieldConfiguration fieldConfiguration)
    {
        String[] parts = fieldConfiguration.getName().split("\\.");
        if(parts.length>1)
        {
            base = new TreeNode(parts[0]);

            TreeNode currentNode = base;
            for(int i=1;i<parts.length-1;i++)
            {
                currentNode = currentNode.addChild(parts[i]);
            }

            System.out.println();
        }
        else
        {

        }
    }

}