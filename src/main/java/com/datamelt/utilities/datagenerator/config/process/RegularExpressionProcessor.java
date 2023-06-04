package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public class RegularExpressionProcessor implements FieldProcessor
{
    private DataConfiguration configuration;
    public RegularExpressionProcessor(DataConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }

    @Override
    public void validateOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {

    }

    @Override
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
