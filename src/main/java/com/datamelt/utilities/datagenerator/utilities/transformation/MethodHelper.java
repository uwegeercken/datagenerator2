package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MethodHelper
{
    public static Method getMethod(Class dataType, TransformationConfiguration transformationConfiguration) throws NoSuchMethodException {
        Class<DataTransformer> dataTransformer = DataTransformer.class;
        Class[] parameterTypes;

        parameterTypes = getMethodParameters(dataType, transformationConfiguration.getParameters());
        try
        {
            return dataTransformer.getMethod(transformationConfiguration.getName().trim(), parameterTypes);
        }
        catch (NoSuchMethodException nsme)
        {
            throw new NoSuchMethodException("invalid parameters for transformation [" + transformationConfiguration.getName() + "], parameters: " + transformationConfiguration.getParameters());
        }
    }

    private static Class[] getMethodParameters(Class clazz, List<Object> parameters)
    {
        Class[] params = new Class[1 + parameters.size()];
        params[0] = clazz;
        int counter=1;
        for(Object parameter : parameters)
        {
            params[counter] = parameter.getClass();
            counter++;
        }
        return params;
    }
}
