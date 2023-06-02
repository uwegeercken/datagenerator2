package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.Field;

import java.util.List;

public class RowBuilder
{
    public static Row generate(List<Field> fields) throws Exception
    {
        RandomValueGenerator generator = null;
        Row row = new Row();
        for (Field field : fields)
        {
            if (field.getType().equals("category"))
            {
                generator = new CategoryGenerator();

            } else if (field.getType().equals("randomstring"))
            {
                generator = new RandomStringGenerator("abcdefghijklmnopqrstuvw");

            } else if (field.getType().equals("regex"))
            {
                generator = new RegularExpressionGenerator();

            }
            row.addField(generator.generateRandomValue(field));
        }
        return row;
    }
}
