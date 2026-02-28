## Sample 03

The sample data configuration file in this folder can be used to generate random test data.

### Structure
Following data is generated:
- an automatically generated unique row number
- a testboolean column from specifying values and weights without using a category file and converting the value to a boolean value
- a testboolean2 column from generating a long value and converting the value to a boolean value
- a testdouble column from generating a regular expression and converting the value to a double value

### Sample Output in Json Format

    {
        "autonumber":0,
        "testboolean": true,
        "testboolean2": false,
        "testdouble": 25.23
    }

