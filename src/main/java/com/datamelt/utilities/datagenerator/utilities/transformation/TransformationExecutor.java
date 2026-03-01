package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;

import java.util.Arrays;
import java.util.List;

public class TransformationExecutor
{
    private static Class<DataTransformer> dataTransformer = DataTransformer.class;
    public static <T> T executeAll(T value, List<TransformationMethod> methods) throws TransformationExecutionException
    {
        T transformedValue = value;
        for(TransformationMethod method : methods)
        {
            transformedValue = (T) execute(method, transformedValue);
        }
        return transformedValue;
    }

    private static <T> T execute(TransformationMethod transformationMethod, T value) throws TransformationExecutionException
    {
        Object[] parameterValues = null;
        if(transformationMethod.getMethod().isSuccess())
        {
            try
            {
                return (T) transformationMethod.getMethod().getResult().invoke(null, getMethodParameterValues(value, transformationMethod.getParameters()));
            }
            catch(Exception ex)
            {
                throw new TransformationExecutionException("error invoking the transformation [" + transformationMethod.getMethod().getResult().getName() + "] with the configured parameters: " + Arrays.toString(parameterValues) + ", types: " + Arrays.toString(transformationMethod.getMethod().getResult().getParameterTypes()),ex);
            }
        }
        else
        {
            // TODO: check return value if try was failure
            return value;
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
