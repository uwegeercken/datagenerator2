package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.DataGenerator;
import com.datamelt.utilities.datagenerator.config.model.CsvDelimiterType;
import com.datamelt.utilities.datagenerator.config.model.CsvExportConfiguration;
import com.datamelt.utilities.datagenerator.config.model.DataExportType;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws Exception
    {
        logger.info("export of generated data to csv file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuffer options = new StringBuffer();
        options.append("(");
        if (includeHeader == true) {
            options.append("HEADER, ");
        }
        options.append("DELIMITER '" + delimiter.getValue() + "'");
        options.append(")");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
