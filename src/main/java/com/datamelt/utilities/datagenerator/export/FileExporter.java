package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.DataExportType;
import org.duckdb.DuckDBConnection;

public interface FileExporter
{
    void export(DuckDBConnection connection, String tablename, String exportFilename) throws Exception;
}
