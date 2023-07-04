package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ExcelExportConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    String format = "GDAL";

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    String driver = "xlsx";

    public String getFormat() {
        return format;
    }

    public String getDriver() {
        return driver;
    }
}
