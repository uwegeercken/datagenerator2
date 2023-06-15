package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import java.util.List;

public interface RandomValueGenerator
{
    <T> T generateRandomValue() throws Exception;
    <T> T transformRandomValue(T value) throws Exception;
}
