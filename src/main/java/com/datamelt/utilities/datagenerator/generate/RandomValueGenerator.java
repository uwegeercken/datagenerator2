package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.Field;

public interface RandomValueGenerator
{
    RowField generateRandomValue(Field field) throws Exception;
}
