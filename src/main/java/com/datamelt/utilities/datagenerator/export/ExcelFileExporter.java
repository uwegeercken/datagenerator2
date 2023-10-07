package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.ExcelExportConfiguration;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

public class ExcelFileExporter implements FileExporter
{
    private static Logger logger = LoggerFactory.getLogger(ExcelFileExporter.class);
    private String format;
    private String driver;
    public ExcelFileExporter(ExcelExportConfiguration configuration)
    {
        this.format = configuration.getFormat();
        this.driver = configuration.getDriver();
    }
    @Override
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws Exception
    {
        logger.info("export of generated data to excel file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuilder options = new StringBuilder()
            .append(" WITH (FORMAT ").append(format).append(", " ).append("DRIVER '").append(driver).append("')");
        stmt.execute("install spatial");
        stmt.execute("load spatial");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options);
    }
}
