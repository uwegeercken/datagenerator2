package com.datamelt.utilities.datagenerator.config.model;

public class ProgramArguments
{
    private long numberOfRowsToGenerate;
    private String outputFilename;
    private String configurationFilename;

    private String csvDelimiter = ",";
    private boolean csvIncludeHeader=true;
    public ProgramArguments(String[] args)
    {
        parseArguments(args);
    }

    private void parseArguments(String[] args)
    {
        for(int i=0;i<args.length;i++)
        {
            if(args[i].startsWith("-n="))
            {
                numberOfRowsToGenerate = Long.parseLong(args[i].substring(args[i].indexOf("=")+1));
            }
            else if(args[i].startsWith("-o="))
            {
                outputFilename = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-c="))
            {
                configurationFilename = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-cd="))
            {
                csvDelimiter = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-ch="))
            {
                csvIncludeHeader = Boolean.parseBoolean(args[i].substring(args[i].indexOf("=")+1));
            }
        }
    }

    public void setConfigurationFilename(String configurationFilename) {
        this.configurationFilename = configurationFilename;
    }

    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(String csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
    }

    public boolean isCsvIncludeHeader() {
        return csvIncludeHeader;
    }

    public void setCsvIncludeHeader(boolean csvIncludeHeader) {
        this.csvIncludeHeader = csvIncludeHeader;
    }

    public String getConfigurationFilename() {
        return configurationFilename;
    }

    public long getNumberOfRowsToGenerate() {
        return numberOfRowsToGenerate;
    }

    public void setNumberOfRowsToGenerate(long numberOfRowsToGenerate) {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }
}
