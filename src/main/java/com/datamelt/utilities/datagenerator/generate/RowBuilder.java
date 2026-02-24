package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.application.DataGenerator;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.error.Failure;
import com.datamelt.utilities.datagenerator.error.Success;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.ArrayList;
import java.util.List;

public class RowBuilder
{
    private final List<RowField> rowFields = new ArrayList<>();
    public RowBuilder(DataConfiguration dataConfiguration)
    {
        // TODO: rethink runtimeexception
        try
        {
            createRowFields(dataConfiguration);
        }
        catch (InvalidConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void createRowFields(DataConfiguration dataConfiguration) throws  InvalidConfigurationException
    {
        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            FieldType fieldType = fieldConfiguration.getType();
            RandomValueGenerator generator = fieldType.getRandomValueGeneratorFunction().apply(fieldConfiguration);

            if (fieldConfiguration.getType() != FieldType.RANDOMDATE)
            {
                rowFields.add(new RowField(generator, fieldConfiguration.getName()));
            }
            else
            {
                // TODO: refactor to use a transformation instead of two seperate classes
                if (fieldConfiguration.getOutputType() == DataTypeDuckDb.LONG)
                {
                    rowFields.add(new RowField(new RandomDateAsLongGenerator(fieldConfiguration), fieldConfiguration.getName()));
                }
                else
                {
                    rowFields.add(new RowField(new RandomDateGenerator(fieldConfiguration), fieldConfiguration.getName()));
                }
            }


//            if (fieldConfiguration.getType() == FieldType.CATEGORY)
//            {
//                rowFields.add(new RowField(new CategoryGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMSTRING)
//            {
//                rowFields.add(new RowField(new RandomStringGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMLONG)
//            {
//                rowFields.add(new RowField(new RandomLongGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMDOUBLE)
//            {
//                rowFields.add(new RowField(new RandomDoubleGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.REGULAREXPRESSION)
//            {
//                rowFields.add(new RowField(new RegularExpressionGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMDATE)
//            {
//                if(fieldConfiguration.getOutputType() == DataTypeDuckDb.LONG)
//                {
//                    rowFields.add(new RowField(new RandomDateAsLongGenerator(fieldConfiguration), fieldConfiguration.getName()));
//                }
//                else
//                {
//                    rowFields.add(new RowField(new RandomDateGenerator(fieldConfiguration), fieldConfiguration.getName()));
//                }
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMTIMESTAMP)
//            {
//                rowFields.add(new RowField(new RandomTimestampGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
//            {
//                rowFields.add(new RowField(new DateReferenceGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
//            else if (fieldConfiguration.getType() == FieldType.RANDOMUUID)
//            {
//                rowFields.add(new RowField(new RandomUuidGenerator(fieldConfiguration), fieldConfiguration.getName()));
//            }
        }

        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
            {
                String referenceFieldName = (String)fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey());
                RowField referenceRowField = getRowfield(referenceFieldName);
                if(referenceRowField==null)
                {
                    throw new InvalidConfigurationException("field [" +fieldConfiguration.getName() + "] references field [" + referenceFieldName + "] but this field does not exist");
                }
                RowField rowField = getRowfield(fieldConfiguration.getName());

                DateReferenceGenerator generator = (DateReferenceGenerator) rowField.getGenerator();
                generator.addReferenceRowField(referenceRowField);
            }
        }
    }

    public RowField getRowfield(String name)
    {
        for(RowField rowField : rowFields)
        {
            if(rowField.getName().equals(name))
            {
                return rowField;
            }
        }
        return null;
    }

    public Try<Row> generate()
    {
        Row row = new Row();
        for (RowField rowField : rowFields)
        {
            RowField rowFieldCopy = rowField.copy();
            try
            {
                rowFieldCopy.generateValue();
                row.addField(rowFieldCopy);
            }
            catch (InvalidConfigurationException | TransformationExecutionException ex)
            {
                return new Failure<>(ex);
            }
        }
        return new Success<>(row);
    }

}
