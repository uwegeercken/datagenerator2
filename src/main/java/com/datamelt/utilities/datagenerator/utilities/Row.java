package com.datamelt.utilities.datagenerator.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
    private static final String DELIMITER = ",";
    private List<RowField> fields = new ArrayList<>();

    public void addField(String name, Object value)
    {
        fields.add(new RowField(name, value));
    }

    public void addField(RowField field)
    {
        fields.add(field);
    }

    public String getRowHeader()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(RowField field : fields)
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
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for(RowField field : fields)
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
