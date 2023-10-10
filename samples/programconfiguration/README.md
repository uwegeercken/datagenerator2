## Program Configuration
The programconfiguration yaml file in this folder contains settings to run the datagenerator2 tool
and settings for the export of the data to different file formats.

### Configuration
The "general" section is mainly used to steer where the data is exported to and in which format,
how many rows to generate, how often a log message is output during data generation and the name of
the automatically generated row number.

The other sections define various options for the different export file formats.

    general:
        exportFilename: /home/uwe/development/datagenerator2/datagenerator_export.json
        exportType: json
        numberOfRowsToGenerate: 5
        generatedRowsLogInterval: 20
        rowNumberFieldName: autonumber
    csvExport:
        delimiter: comma
        includeHeader: true
    jsonExport:
        asArray: true
    excelExport:
        format: gdal
        driver: xlsx
    parquetExport:
        compression: gzip
        #partitionBy: weekday

#### CSV Export
The CSV export can use one of the defined delimiters:
- comma
- colon
- semicolon
- tab
- verticalbar
- atsign

#### Json Export
If "asArray" is set to "true", then all generated data is put into an array. If set to "false"
the each generated row - in Json format - is output to a single row in the file.

#### Excel output
At the moment there are no configuration options for the Excel export. The defined values should not be changed.

#### Parquet Export
The "compression" attribute allows to steer how parquet files are compressed. Possible values are:
- uncompressed
- snappy
- gzip
- zstd

The "partitionBy" attribute can be set to a column defined in the generated data to partition data by this column.


#### NOTE
Please note that structures and substructures generated with the datagenerator2 tool can not be exported
to Excel format because Excel does not support complex structures. You can export data with complex structures
to Json or Parquet format.