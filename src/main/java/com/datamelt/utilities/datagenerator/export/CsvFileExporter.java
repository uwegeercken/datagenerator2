package com.datamelt.utilities.datagenerator.export;

import org.duckdb.DuckDBConnection;

import java.sql.Statement;

public class CsvFileExporter implements FileExporter
{
    private String delimiter;
    private boolean includeHeader;

    public CsvFileExporter(String delimiter, boolean includeHeader)
    {
        this.delimiter = delimiter;
        this.includeHeader = includeHeader;
    }
    @Override
    public void export(DuckDBConnection connection, String tablename, String outputFilename) throws Exception
    {
        Statement stmt = connection.createStatement();
        StringBuffer options = new StringBuffer();
        options.append("(");
        if (includeHeader == true) {
            options.append("HEADER, ");
        }
        options.append("DELIMITER '" + delimiter + "'");
        options.append(")");
        stmt.execute("COPY " + tablename + " TO '" + outputFilename + "' " + options.toString());
    }
}
