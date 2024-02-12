package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RegularExpressionGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RegularExpressionGenerator.class);
    private static final Class BASE_DATATYPE = String.class;
    private FieldConfiguration fieldConfiguration;
    private List<TransformationMethod> transformationMethods = new ArrayList<>();

    public RegularExpressionGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;
        prepareMethods();
    }

    private void prepareMethods() throws NoSuchMethodException
    {
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(  MethodHelper.getMethod(BASE_DATATYPE, transformationConfiguration),transformationConfiguration.getParameters()));
        }
    }
    @Override
    public String generateRandomValue() throws InvalidConfigurationException
    {
        return null;
    }

    @Override
    public <T> T transformRandomValue(T value) throws InvalidConfigurationException
    {
        return null;
    }
}
