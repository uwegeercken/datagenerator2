## Sample 02

The sample data configuration file in this folder can be used to generate random test data.

### Structure
Following data is generated:
- an automatically generated unique row number
- a gender column from the gender.category file assigning individual weights
- a weekday column from the weekday.category file assigning weights just for two weekdays
- a city column from the city.category file

### Sample Output in Json Format

    {
      "autonumber":0,
      "gender":"m",
      "weekday":"Wednesday",
      "city":"Minneapolis"
    }
