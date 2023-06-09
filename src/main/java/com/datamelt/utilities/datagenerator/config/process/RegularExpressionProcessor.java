package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

public class RegularExpressionProcessor extends FieldProcessor
{
    public RegularExpressionProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }

    @Override
    public void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {

    }

    @Override
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
