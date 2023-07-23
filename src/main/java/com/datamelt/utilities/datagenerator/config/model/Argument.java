package com.datamelt.utilities.datagenerator.config.model;

public enum Argument
{
    NUMBEROFROWSTOGENERATE("-n","number of rows to generate"),
    GENERATEDROWSLOGINTERVAL("-l", "interval for log messages during data generation"),
    EXPORTFILENAME("-x", "path and filename of the export file"),
    EXPORTTYPE("-xt", "type of the export to generate. possible values: csv"),
    DATACONFIGURATIONFILENAME("-dc", "path and filename of tha data configuration yaml file"),
    PROGRAMCONFIGURATIONFILENAME("-pc", "path and filename of tha program configuration yaml file"),
    CSVDELIMITER("-cd", "delimiter to be used for export files of type CSV"),
    CSVINCLUDEHEADER("-ch", "indicator if a header row should be output for export files of type CSV"),
    GENERATESTATISTICS("-s", "output statistics for the generated field values");

    private String abbreviation;
    private String explanation;
    Argument(String abbreviation, String explanation)
    {
        this.abbreviation = abbreviation;
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
}
