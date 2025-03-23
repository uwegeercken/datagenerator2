package com.datamelt.utilities.datagenerator.config.model;

import org.apache.logging.log4j.Level;

public enum Argument
{
    NUMBEROFROWSTOGENERATE("-n", false,"number of threads to use during data generation"),
    NUMBEROFTHREADS("-t", false,"number of rows to generate per thread"),
    NUMBEROFROWSPERTHREAD("-r", false,"number of rows to generate"),
    GENERATEDROWSLOGINTERVAL("-l", false,"interval for log messages during data generation"),
    LOGLEVEL("-g", false,"log level to be used for logging output. must be one out of [OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL]"),
    EXPORTFILENAME("-xp", false, "path and filename of the export file"),
    EXPORTTYPE("-xt", false,"type of the export to generate. possible values: csv"),
    DATACONFIGURATIONFILENAME("-dc", true, "path and filename of tha data configuration yaml file"),
    PROGRAMCONFIGURATIONFILENAME("-pc", true, "path and filename of tha program configuration yaml file"),
    CSVDELIMITER("-cd", false, "delimiter to be used for export files of type CSV"),
    CSVINCLUDEHEADER("-ch", false, "indicator if a header row should be output for export files of type CSV"),
    GENERATESTATISTICS("-s", false,"output statistics for the generated field values");

    private String abbreviation;
    private boolean mandatory;
    private String explanation;
    Argument(String abbreviation, boolean mandatory, String explanation)
    {
        this.abbreviation = abbreviation;
        this.mandatory = mandatory;
        this.explanation = explanation;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public String getExplanation()
    {
        return explanation;
    }

    public boolean isMandatory() { return mandatory;  }
}
