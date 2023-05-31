package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ProgramConfiguration
{
    private String outputFilename;
    private long numberOfRowsToGenerate;
    private CsvExportConfiguration csvExport;

    public void mergeArguments(ProgramArguments arguments) throws InvalidConfigurationException
    {
        if(arguments.getNumberOfRowsToGenerate()!=null) {
            numberOfRowsToGenerate = Long.parseLong(arguments.getNumberOfRowsToGenerate());
        }
        if(arguments.getOutputFilename()!=null) {
            outputFilename = arguments.getOutputFilename();
        }
        if(arguments.getCsvDelimiter()!=null) {
            csvExport.setDelimiter(arguments.getCsvDelimiter());
        }
        if(arguments.getCsvIncludeHeader()!=null) {
            csvExport.setIncludeHeader(Boolean.parseBoolean(arguments.getCsvIncludeHeader()));
        }

        validateConfiguration();
    }

    private void validateConfiguration() throws InvalidConfigurationException
    {
        if(numberOfRowsToGenerate < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. number of records to generate can not be smaller than zero");
        }
    }

    public CsvExportConfiguration getCsvExport() {
        return csvExport;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public long getNumberOfRowsToGenerate() {
        return numberOfRowsToGenerate;
    }
}
