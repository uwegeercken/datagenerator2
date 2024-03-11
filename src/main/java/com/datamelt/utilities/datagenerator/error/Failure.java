package com.datamelt.utilities.datagenerator.error;

public record Failure<T>(Throwable throwable) implements Try<T>
{
    @Override
    public T getResult()
    {
        throw new RuntimeException("invalid invocation");
    }

    @Override
    public Throwable getError()
    {
        return throwable;
    }
}
