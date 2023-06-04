package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;

import java.util.ArrayList;
import java.util.List;

public class RowBuilder
{
    private List<RowField<?>> rowFields = new ArrayList<>();
    public RowBuilder(DataConfiguration dataConfiguration)
    {
        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            if (fieldConfiguration.getType() == FieldType.CATEGORY)
            {
                rowFields.add(new RowField<String>(new CategoryGenerator(fieldConfiguration), fieldConfiguration.getName()));
            } else if (fieldConfiguration.getType() == FieldType.RANDOMSTRING)
            {
                rowFields.add(new RowField<String>(new RandomStringGenerator(fieldConfiguration), fieldConfiguration.getName()));
            } else if (fieldConfiguration.getType() == FieldType.REGULAREXPRESSION)
            {
                rowFields.add(new RowField<String>(new RegularExpressionGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
        }
    }

    public Row generate(DataConfiguration dataConfiguration) throws Exception
    {
        RandomValueGenerator generator = null;
        Row row = new Row();
        for (RowField<?> rowField : rowFields)
        {
            rowField.generateValue();

            row.addField(rowField);
        }
        return row;
    }
}
