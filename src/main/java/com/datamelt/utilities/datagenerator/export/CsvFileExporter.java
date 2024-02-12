package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.CsvDelimiterType;
import com.datamelt.utilities.datagenerator.config.model.CsvExportConfiguration;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class CsvFileExporter implements FileExporter
{
    private static Logger logger = LoggerFactory.getLogger(CsvFileExporter.class);
    private CsvDelimiterType delimiter;
    private boolean includeHeader;

    public CsvFileExporter(CsvExportConfiguration configuration)
    {
        this.delimiter = configuration.getDelimiter();
        this.includeHeader = configuration.isIncludeHeader();
    }
    @Override
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws SQLException
    {
        logger.info("export of generated data to csv file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuilder options = new StringBuilder()
            .append("(");
        if (includeHeader == true) {
            options.append("HEADER, ");
        }
        options.append("DELIMITER '").append(delimiter.getValue()).append("', FORMAT CSV)");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
