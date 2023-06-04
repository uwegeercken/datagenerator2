package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public class RegularExpressionGenerator implements RandomValueGenerator
{

    private FieldConfiguration fieldConfiguration;

    public RegularExpressionGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
    }
    @Override
    public String generateRandomValue() throws Exception
    {
        return null;
    }
}
