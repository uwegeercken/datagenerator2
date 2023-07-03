package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ProgramConfiguration
{
    private String exportFilename;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private DataExportType exportType = DataExportType.CSV;
    private long numberOfRowsToGenerate = 10000;
    private long generatedRowsLogInterval = 1000;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private CsvExportConfiguration csvExport = new CsvExportConfiguration();

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private JsonExportConfiguration jsonExport = new JsonExportConfiguration();

    public void mergeArguments(ProgramArguments arguments) throws InvalidConfigurationException
    {
        if(arguments.getNumberOfRowsToGenerate()!=null) {
            numberOfRowsToGenerate = Long.parseLong(arguments.getNumberOfRowsToGenerate());
        }
        if(arguments.getExportFilename()!=null) {
            exportFilename = arguments.getExportFilename();
        }
        if(arguments.getExportType()!=null) {
            exportType = DataExportType.valueOf(arguments.getExportType().toUpperCase());
        }
        if(arguments.getCsvDelimiter()!=null) {
            csvExport.setDelimiter(CsvDelimiterType.valueOf(arguments.getCsvDelimiter().toUpperCase()));
        }
        if(arguments.getCsvIncludeHeader()!=null) {
            csvExport.setIncludeHeader(Boolean.parseBoolean(arguments.getCsvIncludeHeader()));
        }
        if(arguments.getGeneratedRowsLogInterval()!=null)
        {
            generatedRowsLogInterval = Long.parseLong(arguments.getGeneratedRowsLogInterval());
        }

        validateConfiguration();
    }

    private void validateConfiguration() throws InvalidConfigurationException
    {
        if(numberOfRowsToGenerate < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. number of records to generate can not be smaller than zero");
        }
        if(generatedRowsLogInterval < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. generated rows log interval can not be smaller than zero");
        }
    }

    public CsvExportConfiguration getCsvExport() {
        return csvExport;
    }

    public JsonExportConfiguration getJsonExport() { return jsonExport; }

    public String getExportFilename() {
        return exportFilename;
    }

    public long getNumberOfRowsToGenerate() {
        return numberOfRowsToGenerate;
    }

    public long getGeneratedRowsLogInterval() {
        return generatedRowsLogInterval;
    }

    public DataExportType getExportType() { return exportType; }
}
