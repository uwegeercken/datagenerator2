package com.datamelt.utilities.datagenerator.utilities.duckdb.structure;

import java.util.ArrayList;
import java.util.List;

public class TableInsertLayout
{
    private final List<TableField> fields;
    private final List<TreeNode> rootNodes;

    public TableInsertLayout(List<TableField> fields, List<TreeNode> rootNodes)
    {
        this.fields = fields;
        this.rootNodes = rootNodes;
    }

    public List<String> getFieldNames()
    {
        List<String> names = new ArrayList<>();
        for(TableField field : fields)
        {
            names.add(field.getName());
        }
        for(TreeNode node : rootNodes)
        {
            names.add(node.getName());
        }
        return names;
    }

    public List<TableField> getFields()
    {
        return fields;
    }

    public List<TreeNode> getRootNodes()
    {
        return rootNodes;
    }
}
