package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ParquetExportConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String compression = "snappy";
    private String partitionBy;

    public String getCompression() {
        return compression;
    }

    public String getPartitionBy() {
        return partitionBy;
    }
}
