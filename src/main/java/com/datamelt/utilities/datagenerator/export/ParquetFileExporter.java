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
        if(partionBy!=null)
        {
            logger.info("export of generated data to parquet to partitioned folder: [{}],", exportFilename);
        }
        else {
            logger.info("export of generated data to parquet file: [{}],", exportFilename);
        }

        Statement stmt = connection.createStatement();
        StringBuilder options = new StringBuilder()
            .append("(FORMAT PARQUET, COMPRESSION  '").append(compression).append("', OVERWRITE_OR_IGNORE");
        if(partionBy!=null)
        {
            options.append(", PARTITION_BY (").append(partionBy).append(")");
        }
        options.append(")");
        stmt.execute("COPY " + tablename + " TO '" + exportFilename + "' " + options.toString());
    }
}
