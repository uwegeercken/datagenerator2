package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.config.model.options.Transformations;

import java.lang.reflect.Method;

public class TransformationExecutor
{
    public static void main(String[] args) throws Exception
    {
        String[] names = {"subtract100"};
        //String[] names = {"lowercase", "base64encode","subtract100"};

        Object transformedValue = executeAll(names, 101);
        System.out.println(transformedValue);
    }

    public static <T> T executeAll(String[] transform, T value ) throws Exception
    {
        T transformedValue = value;
        for(String enumName : transform)
        {
            if(!enumName.trim().equals(Transformations.UNCHANGED.getName()))
            {
                Class clazz = Transformations.valueOf(enumName.trim().toUpperCase()).getClazz();
                transformedValue = (T) execute(transformedValue, enumName.trim(), clazz);
            }

        }
        return transformedValue;
    }

    public static <T> T execute(T value, String name, Class clazz) throws Exception
    {
        Class<DataTransformer> dataTransformer = DataTransformer.class;
        Method method = dataTransformer.getMethod(name, clazz);
        return (T) method.invoke(null, value);
    }


}
