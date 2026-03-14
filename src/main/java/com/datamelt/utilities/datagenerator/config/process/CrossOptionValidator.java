package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

@FunctionalInterface
public interface CrossOptionValidator
{
    void validate(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
}