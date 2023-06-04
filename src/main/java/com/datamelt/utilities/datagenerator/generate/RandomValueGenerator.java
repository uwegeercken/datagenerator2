package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public interface RandomValueGenerator
{
    void generateRandomValue(FieldConfiguration fieldConfiguration) throws Exception;
}
