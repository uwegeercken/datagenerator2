package com.datamelt.utilities.datagenerator.config.model;

import java.util.Map;

public class ProgramArguments
{
    private String numberOfRowsToGenerate;
    private String outputFilename;
    private String dataConfigurationFilename;
    private String programConfigurationFilename;
    private String csvDelimiter;
    private String csvIncludeHeader;
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
                numberOfRowsToGenerate = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-o="))
            {
                outputFilename = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-c="))
            {
                dataConfigurationFilename = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-p="))
            {
                programConfigurationFilename = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-cd="))
            {
                csvDelimiter = args[i].substring(args[i].indexOf("=")+1);
            }
            else if(args[i].startsWith("-ch="))
            {
                csvIncludeHeader = args[i].substring(args[i].indexOf("=")+1);
            }
        }
    }

    public void setDataConfigurationFilename(String dataConfigurationFilename) {
        this.dataConfigurationFilename = dataConfigurationFilename;
    }

    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(String csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
    }

   public String getDataConfigurationFilename() {
        return dataConfigurationFilename;
    }

    public String getProgramConfigurationFilename() { return programConfigurationFilename; }

       public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public String getNumberOfRowsToGenerate() {
        return numberOfRowsToGenerate;
    }

    public String getCsvIncludeHeader() {
        return csvIncludeHeader;
    }
}
