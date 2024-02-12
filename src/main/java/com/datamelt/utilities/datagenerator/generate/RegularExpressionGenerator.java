package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegularExpressionGenerator implements RandomValueGenerator<String>
{
    private static final Logger logger = LoggerFactory.getLogger(RegularExpressionGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;
    private final List<TransformationMethod> transformationMethods;

    public RegularExpressionGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue() throws InvalidConfigurationException
    {
        return null;
    }

    @Override
    public String transformRandomValue(String value) throws InvalidConfigurationException
    {
        return null;
    }
}
