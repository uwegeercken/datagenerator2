package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProgramConfiguration
{
    @JsonProperty("general")
    private ProgramGeneralConfiguration generalConfiguration;
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
            generalConfiguration.setNumberOfRowsToGenerate(Long.parseLong(arguments.getNumberOfRowsToGenerate()));
        }
        if(arguments.getGeneratedRowsLogInterval()!=null)
        {
            generalConfiguration.setGeneratedRowsLogInterval(Long.parseLong(arguments.getGeneratedRowsLogInterval()));
        }
        if(arguments.getExportFilename()!=null) {
            generalConfiguration.setExportFilename(arguments.getExportFilename());
        }
        if(arguments.getExportType()!=null) {
            generalConfiguration.setExportType(DataExportType.valueOf(arguments.getExportType().toUpperCase()));
        }


        validateConfiguration();
    }

    private void validateConfiguration() throws InvalidConfigurationException
    {
        if(generalConfiguration.getNumberOfRowsToGenerate() < 0)
        {
            throw new InvalidConfigurationException("invalid configuration. number of records to generate can not be smaller than zero");
        }
        if(generalConfiguration.getGeneratedRowsLogInterval() < 0)
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

    public ProgramGeneralConfiguration getGeneralConfiguration()
    {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(ProgramGeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
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
