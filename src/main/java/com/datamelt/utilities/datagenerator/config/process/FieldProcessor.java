package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.Field;

public interface FieldProcessor
{
    void validateConfiguration(Field field) throws InvalidConfigurationException;
    void processConfiguration(Field field) throws InvalidConfigurationException;
}
