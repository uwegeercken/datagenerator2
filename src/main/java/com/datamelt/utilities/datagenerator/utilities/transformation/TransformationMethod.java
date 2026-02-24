package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.error.Try;

import java.lang.reflect.Method;
import java.util.List;

public class TransformationMethod
{
    private final Try<Method> method;
    private final List<Object> parameters;

    public TransformationMethod(Try<Method> method, List<Object> parameters)
    {
        this.method = method;
        this.parameters = parameters;
    }

    public Try<Method> getMethod()
    {
        return method;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }
}
