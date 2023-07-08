# datagenerator2

The datagenerator tool is currently (July '23) under development. Additional features and capabilities will be added over time.

The datagenerator tool allows to generate random data. The aim is to have a tool that generates data in a way which is flexible enough to satisfy the needs of developers or analysts or anybody else who needs some sort
of test data - possibly with dependencies between individual fields and variying/definable distribution of fieldConfiguration values. 

The tool requires a yaml file which contains configuration details for the tool itself, including attributes for the
export of the generated data to files. A second yaml file defines how the data is generated in terms of fields, field weight and
other attributes.
Some of the configuration attributes may also be passed as arguments when running the datagenerator tool. In this case these
will override the same attributes from the configuration files.

## Features
- select random values from word lists
- generate random strings, numbers or floating point numbers
- generate random dates
- generate random data according to a given regular expression (to be implemented)
- transform the generated data values: uppercase, lowercase, base64 encode, negate, round, encrypt and more
- export rows of generated data in CSV, Excel or Json
- export rows of generated data in Parquet format - including partitioning

## Types of generators
Different types of generators are available to generate different type of data such as strings, numbers, dates, etc.

For each field specified in the yaml configuration file one of the generators has to be defined by specifying a field type in the
data configuration file.

The type for each field can be one of following values:
- category
- randomstring
- randomlong
- randomdouble
- randomdate

If no type is specified then type=category is assumed.

### Random Strings
This type of generator (type=randomstring) generates purely random text. The options in the yaml configuration file allow to specify the range of characters to be used for constructing the random text. Additional options allow to specify the minimum and maximum length.
Setting minLength=maxLength will create a constant length string.

#### Available options:
| Option           | Description                                     | Data Type   | Default                      |
|------------------|-------------------------------------------------|-------------|------------------------------|
| minLength        | minimum length of the value                     | long        | 0                            |
| maxLength        | maximum length of the value                     | long        | 40                           |
| randomCharacters | characters to be used when generating the value | String      | [a-z] + [A-Z] + [0-9] + [-_] |

#### Available transformations:
| Transformation | Description                            | Parameters |
|----------------|----------------------------------------|------------|
| uppercase      | convert the value to uppercase         | none       |
| lowercase      | convert the value to lowercase         | none       |
| reverse        | reverse the characters of the value    | none       |
| base64encode   | encode the value to base64 format      | none       |

### Random Numbers
This generator (type=randomlong) allows to generate numbers. The options for this type of generator allow to specify a lowerbound and upperbound for the generated value.

#### Available options:
| Option     | Description    | Data Type    | Default   |
|------------|----------------|--------------|-----------|
| minValue   | minimum value  | long         | 0         |
| maxValue   | maximum value  | long         | 1000000   |

### Random Floating Point Numbers
This generator (type=randomdouble) allows to generate floating point numbers. The options for this type of generator allow to specify a lowerbound and upperbound for the generated value.

#### Available options:
| Option     | Description    | Data Type | Default  |
|------------|----------------|-----------|----------|
| minValue   | minimum value  | long      | 0        |
| maxValue   | maximum value  | long      | 1000000  |

#### Available transformations:
| Transformation | Description                                 | Parameters                         |
|----------------|---------------------------------------------|------------------------------------|
| round          | round the value using rounding mode HALF_UP | number of decimal places (integer) |

### Random Dates
This generator (type=randomdate) allows to generate dates. The options for this type of generator allow to specify a minimum and maximun date, as well as the output format for the generated value.

#### Available options:
| Option       | Description                                       | Data Type | Default     |
|--------------|---------------------------------------------------|-----------|-------------|
| minYear      | minimum value                                     | long      | 2020        |
| maxYear      | maximum value                                     | long      | 2030        |
| dateFormat   | output format of the date (Java SimpleDateFormat) | string    | yyyy-MM-dd  |

#### Available transformations:
| Transformation | Description          | Parameters   |
|----------------|----------------------|--------------|
|                |                      |              |

### Word lists
Word lists allow to define values for certain categories such as "weekdays", "seasons", "car types",
"first names", etc. the generator (type=category) will randomly pick a value from the configured word lists. Word lists are simple text files where each row contains one value.
As such all values of the word lists are treated as strings (even if you have a word list containing e.g numbers).

Using word lists offers a few advantages:
- word lists can be stored in a directory hierarchy where e.g. different directories defined the same word lists but in different languages or the structure defines word lists for different environments (test/production)
- word lists can be created from a data extract from a database, such as a select distinct on a certain column
- word lists can be constructed from a script processing a data file or consuming a Rest API
- word lists can be constructed or changed easily using a simple text editor

In the yaml configuration additional values for a given word list (also value which are already defined in the word list itself) may be defined, including a weight for individual values.
This allows to specify a higher priority/weight for defined values. The weight of a value is always specified on the base of 100 percent. 

E.g. one may define the days of the week in a word list and in the configuration file "Saturday" with a weight of 10 percent and 
"Sunday" with a weight of 10 percent. The other days "Monday" to "Friday" will then be assigned a weight of 16 percent so that the overall sum of percentages is 100 %.

If a value for a given word list appears both in the word list file and the yaml configuration file, the setting from the configuration will overrule the value from the word list file.

The datagenerator will then produce random data (pick random values from the word list) according to the weights assigned. In the example above "saturday" and "sunday" will occur
less often in the generated number of rows then the other days, because these values have a lower weight.

A word list is optional. All values to be used for randomly generating data can also be defined solely in the yaml configuration file. Anyways, the sum of the weight definition
must be 100 percent (and can not exceed 100 percent). Individual values can not be negative percentage values.

**NOTE**: If values and their weight are specified in a word lists but for some values no weight is defined, the datagenerator will calculate the weight for those fields that have no weight definition
and equally distribute the weight value. But, depending on the number of values without a weight definition , it might not be possible to exactly evenly distribute the value. In this case some values
from the word list might get a slightly higher weight value. If weight definitions are assigned in a way that the remaining percentage for the other values is less than 1 percent an error occurs. 

#### Available options:
| Option                  | Description                                         | Data Type | Default |
|-------------------------|-----------------------------------------------------|-----------|---------|
| categoryFileSeparator   | separator between value and weight in category file | string    | ,       |

#### Available transformations:
| Transformation | Description                                            | Parameters                |
|----------------|--------------------------------------------------------|---------------------------|
| uppercase      | convert the value to uppercase                         | none                      |
| lowercase      | convert the value to lowercase                         | none                      |
| reverse        | reverse the characters of the value                    | none                      |            
| prepend        | add a prefix to the value                              | prefix to add (String)    | 
| append         | add a suffix to the value                              | suffix to add (String)    |
| base64encode   | encode the value to base64 format                      | none                      | 
| encrypt        | encrypt the value using AES/CBC/PKCS5Padding algorithm | none                      |


## Processing steps
First, the given program configuration and the data configuration yaml files are analyzed for their correctness. Any existing table definitions and data is removed from the DuckDb, if a file with the specified name of the database is found.

After that the value for each field is generated and then transformed (if any transformation options are specified). The fields are processed sequentially and build a row of data.
The tool generates the desired number of rows and stores it in a local DuckDb instance. Finally, the data is exported to the desired output format.

## Yaml configuration for the datagenerator tool
The configuration file contains various attributes to steer the behavior of the datagenerator tool.

Sample program configuration:

    general:    
      exportFilename: /tmp/csv_export.json
      exportType: json
      numberOfRowsToGenerate: 5000
      generatedRowsLogInterval: 1000
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
      partitionBy: gender


- the name of the export file for the generated data
- the type of the export file: csv, excel or json
- the number of rows to generate
- after how many generated rows a log message shall be output
- details for the export to a csv file - delimiter and header settings
- details for the export to a json file - output as separate lines or as array
- details for the export to a parquet file or partitioned file
- details for the export to an excel file

## Yaml configuration for the definition of fields to generate
The configuration file contains a list of fields/attributes to generate - see the sample yaml files in this repository under: src/main/resources/config. For each field, options and transformations may be defined depending on the type of generator used.

Sample fields configuration:

    name: Sample 1
    databaseName: /home/uwe/development/datagenerator2/generateddata.duckdb
    tableName: generateddata
    fields:
      - name: random1
        type: randomstring
        randomCharacters: abcdefghijk0123456789
        options:
          minLength: 20
          maxLength: 40
      - name: random2
        type: randomlong
        options:
          minValue: -1000
          maxValue: 999
      - name: random3
        type: randomdouble
        options:
          minValue: 100
          maxValue: 5000
        transformations:
          - name: round
            parameters:
              - 3
          - name: negate
    - name: gender
      values:
        - value: m
          weight: 40
        - value: f
          weight: 40
        - value: d
          weight: 20
          valuesFile: /home/uwe/development/datagenerator2/categories/gender.category
      - name: weekday
        values:
          - value: Saturday
            weight: 5
          - value: Sunday
            weight: 5
            valuesFile: /home/uwe/development/datagenerator2/categories/weekday.category
      - name: season
        valuesFile: /home/uwe/development/datagenerator2/categories/season.category
        options:
          categoryFileSeparator: "#"
        values:
          - value: Summer
            weight: 70
      - name: city
        valuesFile: /home/uwe/development/datagenerator2/categories/city.category
      - name: number
        valuesFile: /home/uwe/development/datagenerator2/categories/number.category


## Running the datagenerator tool
To run the tool you must pass at least two arguments to the program as shown below. These point to the program configuration file
and the data configuration file. There are additional arguments available which can be used to override the values specified in the
configuration yaml files.

Run the datagenerator tool:

    java -cp . com.datamelt.utilities.datagenerator.DataGenerator -pc=<program configuration file> -dc=<data configuration file>

You can get help about the available program arguments by running:

    java -cp . com.datamelt.utilities.datagenerator.DataGenerator --help

See the sample yaml file for the program configuration in this repository under: src/main/resources/config.

last update: uwe geercken - uwe.geercken@web.de - 2023-07-08
