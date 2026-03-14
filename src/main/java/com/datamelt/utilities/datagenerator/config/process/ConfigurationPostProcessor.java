package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

@FunctionalInterface
public interface ConfigurationPostProcessor
{
    void process(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
}