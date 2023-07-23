package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.Map;

public class ProgramArguments
{
    private String numberOfRowsToGenerate;
    private String exportFilename;
    private String exportType;
    private String dataConfigurationFilename;
    private String programConfigurationFilename;
    private String csvDelimiter;
    private String csvIncludeHeader;
    private String generatedRowsLogInterval;
    private boolean generateStatistics;
    public ProgramArguments(String[] args) throws Exception
    {
        parseArguments(args);
    }
    private void parseArguments(String[] args) throws InvalidConfigurationException
    {
        for(int i=0;i<args.length;i++)
        {
            if(args[i].startsWith(Argument.NUMBEROFROWSTOGENERATE.getAbbreviation()))
            {
                numberOfRowsToGenerate = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.GENERATEDROWSLOGINTERVAL.getAbbreviation()))
            {
                generatedRowsLogInterval = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.EXPORTFILENAME.getAbbreviation()))
            {
                exportFilename = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.EXPORTTYPE.getAbbreviation()))
            {
                exportType = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.DATACONFIGURATIONFILENAME.getAbbreviation()))
            {
                dataConfigurationFilename = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.PROGRAMCONFIGURATIONFILENAME.getAbbreviation()))
            {
                programConfigurationFilename = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.CSVDELIMITER.getAbbreviation()))
            {
                csvDelimiter = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.CSVINCLUDEHEADER.getAbbreviation()))
            {
                csvIncludeHeader = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].equals(Argument.GENERATESTATISTICS.getAbbreviation()))
            {
                generateStatistics = true;
            }
            else
            {
                throw new InvalidConfigurationException("invalid configuration. the argument " + args[i] + " is unknown");
            }
        }

        validate();
    }

    private void validate() throws InvalidConfigurationException
    {
        if(programConfigurationFilename == null)
        {
            throw new InvalidConfigurationException("invalid configuration. program requires a program configuration yaml file to run");
        }
        if(dataConfigurationFilename == null)
        {
            throw new InvalidConfigurationException("invalid configuration. program requires a data configuration yaml file to run");
        }
    }
    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public String getDataConfigurationFilename() {
        return dataConfigurationFilename;
    }

    public String getProgramConfigurationFilename() { return programConfigurationFilename; }

       public String getExportFilename() {
        return exportFilename;
    }

    public String getExportType()
    {
        return exportType;
    }

    public String getNumberOfRowsToGenerate() {
        return numberOfRowsToGenerate;
    }

    public String getCsvIncludeHeader() {
        return csvIncludeHeader;
    }

    public String getGeneratedRowsLogInterval() {
        return generatedRowsLogInterval;
    }

    public boolean getGenerateStatistics()
    {
        return generateStatistics;
    }
}
