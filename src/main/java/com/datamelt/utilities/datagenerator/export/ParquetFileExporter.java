package com.datamelt.utilities.datagenerator.export;

import com.datamelt.utilities.datagenerator.config.model.JsonExportConfiguration;
import com.datamelt.utilities.datagenerator.config.model.ParquetExportConfiguration;
import org.duckdb.DuckDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

public class ParquetFileExporter implements FileExporter
{
    private static Logger logger = LoggerFactory.getLogger(ParquetFileExporter.class);
    private String compression;
    private String partionBy;
    public ParquetFileExporter(ParquetExportConfiguration configuration)
    {
        this.compression = configuration.getCompression();
        this.partionBy = configuration.getPartitionBy();
    }
    @Override
    public void export(DuckDBConnection connection, String tablename, String exportFilename) throws Exception
    {
        logger.info("export of generated data to parquet file: [{}],", exportFilename);
        Statement stmt = connection.createStatement();
        StringBuffer options = new StringBuffer();
        options.append("(");
        options.append("FORMAT PARQUET, ");
        options.append("COMPRESSION  '" + compression + "', " );
        options.append(("OVERWRITE_OR_IGNORE"));
        if(partionBy!=null)
        {
            options.append(", ");
            options.append("PARTITION_BY (" + partionBy + ")");
        }
        options.append(")");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
