package com.datamelt.utilities.datagenerator.generate;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private static final String DELIMITER = ",";
    private List<RowField<?>> fields = new ArrayList<>();

    public void addField(RowField<?> field)
    {
        fields.add(field);
    }

    public String getRowHeader()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(RowField<?> field : fields)
        {
            counter++;
            buffer.append(field.getName());
            if(counter < fields.size())
            {
                buffer.append(DELIMITER);
            }
        }
        return buffer.toString();
    }

    public List<RowField<?>> getFields()
    {
        return fields;
    }

    public RowField getField(String name)
    {
        for(RowField<?> field : fields)
        {
            if(field.getName().equals(name))
            {
                return field;
            }
        }
        return null;
    }
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(RowField<?> field : fields)
        {
            counter++;
            buffer.append(field.getValue());
            if(counter < fields.size())
            {
                buffer.append(DELIMITER);
            }
        }
        return buffer.toString();
    }

}
