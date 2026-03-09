package com.datamelt.utilities.datagenerator.error;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface Try<T> permits Success, Failure
{
    T getResult();
    Throwable getError();

    static <T> Try<T> of(Callable<T> code)
    {
        try
        {
            return new Success<T>(code.call());
        }
        catch(Throwable throwable)
        {
            return new Failure<T>(throwable);
        }
    }

    default <R> Try<R> map(Function<T,R> mapper)
    {
        if(this instanceof Success<T>)
        {
            return of(()-> mapper.apply(getResult()));
        }
        else
        {
            return new Failure<>(getError());
        }
    }

    default boolean isSuccess()
    {
        return this instanceof Success<T>;
    }

    default boolean isFailure()
    {
        return this instanceof Failure<T>;
    }
}
