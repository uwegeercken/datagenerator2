package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface RandomValueGenerator
{
    Object generateRandomValue();
    Object transformRandomValue(Object value) throws TransformationExecutionException;
    default List<TransformationMethod> prepareMethods(Class clazz, FieldConfiguration fieldConfiguration)
    {
        final List<TransformationMethod> transformationMethods = new ArrayList<>();
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
                Try<Method> methodTry = Try.of(() -> MethodHelper.getMethod(clazz, transformationConfiguration));
                transformationMethods.add(new TransformationMethod(methodTry, transformationConfiguration.getParameters()));
        }
        return transformationMethods;
    }
}
