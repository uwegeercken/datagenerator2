package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.Field;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.ProgramConfiguration;

import java.util.List;

public class RowBuilder
{
    public static Row generate(DataConfiguration dataConfiguration) throws Exception
    {
        RandomValueGenerator generator = null;
        Row row = new Row();
        for (Field field : dataConfiguration.getFields())
        {
            if (field.getType() == FieldType.CATEGORY)
            {
                generator = new CategoryGenerator();

            } else if (field.getType() == FieldType.RANDOMSTRING)
            {
                generator = new RandomStringGenerator(dataConfiguration.getRandomCharacters());

            } else if (field.getType() == FieldType.REGULAREXPRESSION)
            {
                generator = new RegularExpressionGenerator();

            }
            row.addField(generator.generateRandomValue(field));
        }
        return row;
    }
}
