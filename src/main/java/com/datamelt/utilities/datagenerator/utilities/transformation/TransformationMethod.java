package com.datamelt.utilities.datagenerator.utilities.transformation;

import java.lang.reflect.Method;
import java.util.List;

public class TransformationMethod
{
    private Method method;
    private List<Object> parameters;

    public TransformationMethod(Method method, List<Object> parameters)
    {
        this.method = method;
        this.parameters = parameters;
    }

    public Method getMethod()
    {
        return method;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }
}
