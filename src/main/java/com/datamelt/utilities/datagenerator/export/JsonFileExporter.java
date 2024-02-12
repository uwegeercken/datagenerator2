package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.JsonExportConfiguration;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class JsonFileExporter implements FileExporter
{
    private static Logger logger = LoggerFactory.getLogger(JsonFileExporter.class);
    private boolean asArray;
    public JsonFileExporter(JsonExportConfiguration configuration)
    {
        this.asArray = configuration.isAsArray();
    }
    @Override
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws SQLException
    {
        logger.info("export of generated data to json file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuilder options = new StringBuilder()
            .append("(ARRAY ").append(asArray).append(", FORMAT JSON)");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
