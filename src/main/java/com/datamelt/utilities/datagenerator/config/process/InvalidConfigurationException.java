package com.datamelt.utilities.datagenerator.config.process;

public class InvalidConfigurationException extends Exception
{
    public InvalidConfigurationException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidConfigurationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
