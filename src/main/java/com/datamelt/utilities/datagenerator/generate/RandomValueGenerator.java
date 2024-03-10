package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;

import java.util.ArrayList;
import java.util.List;

public interface RandomValueGenerator<T>
{
    T generateRandomValue() throws InvalidConfigurationException;
    T transformRandomValue(T value) throws InvalidConfigurationException;
    default List<TransformationMethod> prepareMethods(Class clazz, FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        final List<TransformationMethod> transformationMethods = new ArrayList<>();
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(MethodHelper.getMethod(clazz, transformationConfiguration), transformationConfiguration.getParameters()));
        }
        return transformationMethods;
    }
}
