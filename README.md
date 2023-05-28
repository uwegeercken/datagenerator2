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
"first names", etc. the generator will randomly pick a value from the configured word lists.

In the yaml configuration additional values for a given word list (also value which are already defined in the word list) may be defined including a weight for individual values.
This allows to specify a higher priority/weight for defined values. The weight of a value is always specified on the base of 100 percent. 

E.g. one may define the days of the week in a word list and in the configuration "saturday" with a weight of 10 percent and 
"sunday" with a weight of 10 percent. The other days "monday" to "friday" will then be assigned a weight of 16 percent so that the overall sum is 100 %.

The datagenerator will then produce random data (pick random values from the word list) according to the weights assigned.




last update: uwe geercken - uwe.geercken@web.de - 2023-05-28
