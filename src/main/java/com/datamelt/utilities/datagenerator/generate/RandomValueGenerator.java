package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public interface RandomValueGenerator
{
    <T> T generateRandomValue() throws Exception;
}
