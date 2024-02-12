package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.Arrays;
import java.util.List;

public class TransformationExecutor
{
    private static Class<DataTransformer> dataTransformer = DataTransformer.class;
    public static <T> T executeAll(T value, List<TransformationMethod> methods) throws InvalidConfigurationException
    {
        T transformedValue = value;
        for(TransformationMethod method : methods)
        {
            transformedValue = (T) execute(method, value);
        }
        return transformedValue;
    }

    private static <T> T execute(TransformationMethod transformationMethod, T value) throws InvalidConfigurationException
    {
        Object[] parameterValues = null;
        try
        {
            return (T) transformationMethod.getMethod().invoke(null, getMethodParameterValues(value, transformationMethod.getParameters()));
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("the transformation [" + transformationMethod.getMethod().getName() + "] with the configured parameters: " + Arrays.toString(parameterValues) + ", types: " + Arrays.toString(transformationMethod.getMethod().getParameterTypes()) + " does not exist");
        }
    }

    private static Object[] getMethodParameterValues(Object value, List<Object> parameters)
    {
        Object[] values = new Object[1 + parameters.size()];
        values[0] = value;
        int counter=1;
        for(Object parameter : parameters)
        {
            values[counter] = parameter;
            counter++;
        }
        return values;
    }
}
