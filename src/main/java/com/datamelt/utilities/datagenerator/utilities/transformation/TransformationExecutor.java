package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;

import java.util.Arrays;
import java.util.List;

public class TransformationExecutor
{
    private static final Class<DataTransformer> dataTransformer = DataTransformer.class;
    public static <T> T executeAll(T value, List<TransformationMethod> methods) throws TransformationExecutionException
    {
        T transformedValue = value;
        for(TransformationMethod method : methods)
        {
            transformedValue = execute(method, transformedValue);
        }
        return transformedValue;
    }

    private static <T> T execute(TransformationMethod transformationMethod, T value) throws TransformationExecutionException
    {
        if(transformationMethod.getMethod().isSuccess())
        {
            Object[] parameterValues = getMethodParameterValues(value, transformationMethod.getParameters());
            try
            {
                return (T) transformationMethod.getMethod().getResult().invoke(null, parameterValues);
            }
            catch(Exception ex)
            {
                throw new TransformationExecutionException("error invoking transformation [" + transformationMethod.getMethod().getResult().getName() + "] with parameters: " + Arrays.toString(parameterValues) + ", types: " + Arrays.toString(transformationMethod.getMethod().getResult().getParameterTypes()), ex);
            }
        }
        else
        {
            throw new TransformationExecutionException(
            "transformation [" + transformationMethod.getMethod().getError().getMessage() + "] could not be resolved. check the transformation name in your data configuration");
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
