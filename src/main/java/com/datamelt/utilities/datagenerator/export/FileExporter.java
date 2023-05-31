package com.datamelt.utilities.datagenerator.export;

import org.duckdb.DuckDBConnection;

public interface FileExporter
{
    void export(DuckDBConnection connection, String tablename, String outputFilename) throws Exception;
}
