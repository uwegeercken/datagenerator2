package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;


public class ProgramGeneralConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String exportFilename = "datagenerator_export.csv";
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private DataExportType exportType = DataExportType.CSV;
    private long numberOfRowsToGenerate = 10000;
    private int numberOfThreads = 1;
    private int numberOfRowsPerThread = 1000;
    private long generatedRowsLogInterval = 1000;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String rowNumberFieldName = "rownumber";

    public String getExportFilename()
    {
        return exportFilename;
    }

    public DataExportType getExportType()
    {
        return exportType;
    }

    public long getNumberOfRowsToGenerate()
    {
        return numberOfRowsToGenerate;
    }

    public long getGeneratedRowsLogInterval()
    {
        return generatedRowsLogInterval;
    }

    public void setExportFilename(String exportFilename)
    {
        this.exportFilename = exportFilename;
    }

    public void setExportType(DataExportType exportType)
    {
        this.exportType = exportType;
    }

    public void setNumberOfRowsToGenerate(long numberOfRowsToGenerate)
    {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;
    }

    public void setGeneratedRowsLogInterval(long generatedRowsLogInterval)
    {
        this.generatedRowsLogInterval = generatedRowsLogInterval;
    }

    public String getRowNumberFieldName()
    {
        return rowNumberFieldName;
    }

    public void setRowNumberFieldName(String rowNumberFieldName) {
        this.rowNumberFieldName = rowNumberFieldName;
    }

    public int getNumberOfThreads()
    {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads)
    {
        this.numberOfThreads = numberOfThreads;
    }

    public int getNumberOfRowsPerThread()
    {
        return numberOfRowsPerThread;
    }

    public void setNumberOfRowsPerThread(int numberOfRowsPerThread)
    {
        this.numberOfRowsPerThread = numberOfRowsPerThread;
    }
}
