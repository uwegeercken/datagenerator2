package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ProgramConfiguration
{
    private ProgramGeneralConfiguration general;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private CsvExportConfiguration csvExport = new CsvExportConfiguration();

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private JsonExportConfiguration jsonExport = new JsonExportConfiguration();
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ParquetExportConfiguration parquetExport = new ParquetExportConfiguration();

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ExcelExportConfiguration excelExport = new ExcelExportConfiguration();

    public void mergeArguments(ProgramArguments arguments) throws InvalidConfigurationException
    {
        if(arguments.getNumberOfRowsToGenerate()!=null) {
            general.setNumberOfRowsToGenerate(Long.parseLong(arguments.getNumberOfRowsToGenerate()));
        }
        if(arguments.getGeneratedRowsLogInterval()!=null)
        {
            general.setGeneratedRowsLogInterval(Long.parseLong(arguments.getGeneratedRowsLogInterval()));
        }
        if(arguments.getExportFilename()!=null) {
            general.setExportFilename(arguments.getExportFilename());
        }
        if(arguments.getExportType()!=null) {
            general.setExportType(DataExportType.valueOf(arguments.getExportType().toUpperCase()));
        }


        validateConfiguration();
    }

    private void validateConfiguration() throws InvalidConfigurationException
    {
        if(general.getNumberOfRowsToGenerate() < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. number of records to generate can not be smaller than zero");
        }
        if(general.getGeneratedRowsLogInterval() < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. generated rows log interval can not be smaller than zero");
        }
    }

    public CsvExportConfiguration getCsvExport() {
        return csvExport;
    }

    public JsonExportConfiguration getJsonExport() { return jsonExport; }

    public ParquetExportConfiguration getParquetExport() { return parquetExport; }

    public ExcelExportConfiguration getExcelExport() { return excelExport; }

    public ProgramGeneralConfiguration getGeneral()
    {
        return general;
    }

    public void setGeneral(ProgramGeneralConfiguration general) {
        this.general = general;
    }

    public void setCsvExport(CsvExportConfiguration csvExport) {
        this.csvExport = csvExport;
    }

    public void setJsonExport(JsonExportConfiguration jsonExport) {
        this.jsonExport = jsonExport;
    }

    public void setParquetExport(ParquetExportConfiguration parquetExport) {
        this.parquetExport = parquetExport;
    }

    public void setExcelExport(ExcelExportConfiguration excelExport) {
        this.excelExport = excelExport;
    }
}
