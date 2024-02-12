package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.List;

public interface RandomValueGenerator
{
    <T> T generateRandomValue() throws InvalidConfigurationException;
    <T> T transformRandomValue(T value) throws InvalidConfigurationException;

}
