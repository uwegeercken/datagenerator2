package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.DataExportType;
import org.duckdb.DuckDBConnection;

import java.sql.SQLException;

public interface FileExporter
{
    void export(DuckDBConnection connection, String tablename, String exportFilename) throws SQLException;
}
