package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public abstract class FieldProcessor
{
    private DataConfiguration configuration;

    public FieldProcessor(DataConfiguration configuration)
    {
        this.configuration = configuration;
    }

    protected abstract void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
    protected abstract void setDefaultOptions(FieldConfiguration fieldConfiguration);
    protected abstract void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException;
}
