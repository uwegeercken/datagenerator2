package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.RandomTimestampProcessor;

import java.util.ArrayList;
import java.util.List;

public class RowBuilder
{
    private List<RowField<?>> rowFields = new ArrayList<>();
    public RowBuilder(DataConfiguration dataConfiguration) throws NoSuchMethodException, InvalidConfigurationException
    {
        createRowFields(dataConfiguration);
    }

    private void createRowFields(DataConfiguration dataConfiguration) throws NoSuchMethodException, InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            if (fieldConfiguration.getType() == FieldType.CATEGORY)
            {
                rowFields.add(new RowField<String>(new CategoryGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMSTRING)
            {
                rowFields.add(new RowField<String>(new RandomStringGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMLONG)
            {
                rowFields.add(new RowField<Long>(new RandomLongGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMDOUBLE)
            {
                rowFields.add(new RowField<Double>(new RandomDoubleGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.REGULAREXPRESSION)
            {
                rowFields.add(new RowField<String>(new RegularExpressionGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMDATE)
            {
                rowFields.add(new RowField<String>(new RandomDateGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.RANDOMTIMESTAMP)
            {
                rowFields.add(new RowField<String>(new RandomTimestampGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
            else if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
            {
                rowFields.add(new RowField<String>(new DateReferenceGenerator(fieldConfiguration), fieldConfiguration.getName()));
            }
        }

        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
            {
                String referenceFieldName = (String)fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey());
                RowField<?> referenceRowField = getRowfield(referenceFieldName);
                if(referenceRowField==null)
                {
                    throw new InvalidConfigurationException("field [" +fieldConfiguration.getName() + "] references field [" + referenceFieldName + "] but this field does not exist");
                }
                RowField<?> rowField = getRowfield(fieldConfiguration.getName());

                DateReferenceGenerator generator = (DateReferenceGenerator) rowField.getGenerator();
                generator.addReferenceRowField(referenceRowField);
            }
        }
    }

    public RowField<?> getRowfield(String name)
    {
        for(RowField<?> rowField : rowFields)
        {
            if(rowField.getName().equals(name))
            {
                return rowField;
            }
        }
        return null;
    }

    public Row generate() throws Exception
    {
        Row row = new Row();
        for (RowField<?> rowField : rowFields)
        {
            rowField.generateValue();
            row.addField(rowField);
        }
        return row;
    }
}
