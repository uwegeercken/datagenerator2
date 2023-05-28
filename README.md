# datagenerator2
The datagenerator tool allows to generate random data.

The tool requires a yaml file which contains various configuration attributes,
plus a numeric value which defines how many rows of data to generate.

## Features
- select random values from word lists
- generate random strings (to be implemented)
- generate data according to a given regular expression (to be implemented)
- export generated rows of data in CSV, Json or Parquet format (to be implemented)
### Word lists
Word lists allow to define values for certain categories such as "weekdays", "seasons", "car types",
"first names", etc. the generator will randomly pick a value from the configured word lists. Word lists are simple text files where each row contains one value.

Using word lists offers a few advantages:
- word lists can be stored in a directory hierarchy where e.g. different directories defined the same word lists but in different languages
- word lists can be created from a data extract from a database
- word lists can be constructed from a script processing a data file or consuming a Rest API
- word lists can be changed or enahnced using a simple text editor

In the yaml configuration additional values for a given word list (also value which are already defined in the word list) may be defined including a weight for individual values.
This allows to specify a higher priority/weight for defined values. The weight of a value is always specified on the base of 100 percent. 

E.g. one may define the days of the week in a word list and in the configuration "saturday" with a weight of 10 percent and 
"sunday" with a weight of 10 percent. The other days "monday" to "friday" will then be assigned a weight of 16 percent so that the overall sum is 100 %.

If a value for a given word list appears both in the word list file and the yaml configuration, the setting from the configuration will overrule the value from the word list file.

The datagenerator will then produce random data (pick random values from the word list) according to the weights assigned. In the example above "saturday" and "sunday" will occur
less often in the generated number of rows then the other days, because they have a lower weight.

A word list is optional. All values to be used for randomly generating data can also be defined solely in the yaml configuration file. Anyways, the sum of the weight definition can
must be 100 percent (and can not exceed 100 percent).

**NOTE**: If values and their weight are specified in a word lists but for some values no weight is defined, the datagenerator will calculate the weight for those fields that have no weight definition
and equally distribute the weight value. But, depending on the number of values without a weight definition , it might not be possible to exactly evenly distribute the value. In this case some values
from the word list might get a slightly higher weight value.
 
## Processing steps
First, the given yaml configuration file is analyzed for its correctness. The fields/attributes for which to
generate random data are processed sequentially and build a row of data. The tool generates the desired number of rows.


The data is then generated and stored in a local duckdb instance. The data types specified in the yaml configuration are used to defined the data types of the duckdb table which is created.

## Yaml configuration
The yaml configuration file contains a list of fields/attributes to generate.






last update: uwe geercken - uwe.geercken@web.de - 2023-05-28
