package com.datamelt.utilities.datagenerator.config.process;

public class TransformationExecutionException extends Exception
{
    public TransformationExecutionException(String errorMessage) {
        super(errorMessage);
    }

    public TransformationExecutionException(String errorMessage, Throwable error)
    {
        super(errorMessage, error);
    }
}
