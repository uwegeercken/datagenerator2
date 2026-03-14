package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.error.Failure;
import com.datamelt.utilities.datagenerator.error.Success;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RowBuilder
{
    private final List<RowField> rowFields = new ArrayList<>();
    public RowBuilder(DataConfiguration dataConfiguration) throws InvalidConfigurationException
    {
            createRowFields(dataConfiguration);
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
        }

        for (FieldConfiguration fieldConfiguration : dataConfiguration.getFields())
        {
            if (fieldConfiguration.getType() == FieldType.DATEREFERENCE)
            {
                String referenceFieldName = (String)fieldConfiguration.getOptions().get(OptionKey.REFERENCE.getKey());
                RowField referenceRowField = getRowfield(referenceFieldName)
                        .orElseThrow(() -> new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] references field [" + referenceFieldName + "] but this field does not exist"));

                RowField rowField = getRowfield(fieldConfiguration.getName())
                        .orElseThrow(() -> new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] does not exist"));

                DateReferenceGenerator generator = (DateReferenceGenerator) rowField.getGenerator();
                generator.addReferenceRowField(referenceRowField);
            }
        }
    }

    public Optional<RowField> getRowfield(String name)
    {
        return rowFields.stream()
                .filter(rowField -> rowField.getName().equals(name))
                .findFirst();
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
