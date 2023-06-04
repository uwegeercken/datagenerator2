package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public interface FieldProcessor
{
    void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
    void validateOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
    void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
}
