package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.CsvDelimiterType;
import com.datamelt.utilities.datagenerator.config.model.JsonExportConfiguration;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws Exception
    {
        logger.info("export of generated data to json file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuffer options = new StringBuffer();
        options.append("(");
        options.append("ARRAY " + asArray );
        options.append(")");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
