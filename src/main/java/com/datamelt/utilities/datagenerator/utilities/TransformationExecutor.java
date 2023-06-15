package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;

import java.lang.reflect.Method;
import java.util.List;

public class TransformationExecutor
{
    public static <T> T executeAll(T value, List<TransformationConfiguration> transformationConfigurations) throws Exception
    {
        T transformedValue = value;
        for(TransformationConfiguration transformation : transformationConfigurations)
        {
            if(!transformation.getName().trim().equals(Transformations.UNCHANGED.getName()))
            {
                Class clazz = Transformations.valueOf(transformation.getName().trim().toUpperCase()).getClazz();
                transformedValue = (T) execute(transformedValue, transformation.getName().trim(), clazz, transformation.getParameters());
            }

        }
        return transformedValue;
    }

    private static <T> T execute(T value, String name, Class clazz, List<Object> parameters) throws Exception
    {
        Class<DataTransformer> dataTransformer = DataTransformer.class;
        Method method = dataTransformer.getMethod(name, getMethodParameters(clazz,parameters));
        return (T) method.invoke(null, getMethodParameterValues(value, parameters));
    }

    private static Class[] getMethodParameters(Class clazz, List<Object> parameters)
    {
        Class[] params = new Class[1 + parameters.size()];
        params[0] = clazz;
        int counter=1;
        for(Object parameter : parameters)
        {
            params[counter] = parameters.get(counter-1).getClass();
        }
        return params;
    }

    private static Object[] getMethodParameterValues(Object value, List<Object> parameters)
    {
        Object[] values = new Object[1 + parameters.size()];
        values[0] = value;
        int counter=1;
        for(Object parameter : parameters)
        {
            values[counter] = parameters.get(counter-1);
        }
        return values;
    }


}
