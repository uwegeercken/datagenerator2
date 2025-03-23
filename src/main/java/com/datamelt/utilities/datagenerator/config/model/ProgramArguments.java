package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.Optional;

public class ProgramArguments
{
    private String numberOfRowsToGenerate;
    private String numberOfThreads;
    private String numberOfRowsPerThread;
    private String exportFilename;
    private String exportType;
    private String dataConfigurationFilename;
    private String programConfigurationFilename;
    private String generatedRowsLogInterval;
    private String logLevel;
    private boolean generateStatistics;

    public ProgramArguments(String[] args) throws InvalidConfigurationException
    {
        parseArguments(args);
    }

    public void parseArguments(String[] args) throws InvalidConfigurationException
    {
        for(int i=0;i<args.length;i++)
        {
            if(args[i].startsWith(Argument.NUMBEROFROWSTOGENERATE.getAbbreviation()))
            {
                numberOfRowsToGenerate = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.NUMBEROFTHREADS.getAbbreviation()))
            {
                numberOfThreads = args[i].substring(args[i].indexOf("=")+1).trim();
            }
            else if(args[i].startsWith(Argument.NUMBEROFROWSPERTHREAD.getAbbreviation()))
            {
                numberOfRowsPerThread = args[i].substring(args[i].indexOf("=")+1).trim();
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
            else if(args[i].equals(Argument.GENERATESTATISTICS.getAbbreviation()))
            {
                generateStatistics = true;
            }
            else if (args[i].startsWith(Argument.LOGLEVEL.getAbbreviation()))
            {
                logLevel = args[i].substring(args[i].indexOf("=") + 1).trim();
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

    public String getGeneratedRowsLogInterval() {
        return generatedRowsLogInterval;
    }

    public String getLogLevel()
    {
        return logLevel;
    }

    public boolean getGenerateStatistics()
    {
        return generateStatistics;
    }

    public String getNumberOfThreads()
    {
        return numberOfThreads;
    }

    public String getNumberOfRowsPerThread()
    {
        return numberOfRowsPerThread;
    }
}
