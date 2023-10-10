## Sample 01

The sample data configuration file in this folder can be used to generate random test data.

### Structure
Following data is generated:
- an automatically generated unique row number
- a date structure
  - a random date in the year 2023
  - a year column based on the random date
  - a month column based on the random date
  - a day of month column based on the random date
  - a day name in week column based on the random date
- a person structure
  - a firstname column from the firstname.category file
  - a lastname column from the lastname.category file
  - a person address substructure
    - a city substructure
      - a city name column from the city.category file
    - a street substructure
      - a street name column from the street.category file
      - a street number column from a randomly generated number between 1 and 300

### Sample Output in Json Format

    {
      "autonumber": 1,
      "date": {
        "fulldate": "2023-12-11",
        "year": "2023",
        "month": "12",
        "dayofmonth": "11",
        "dayofweek": "Montag"
      },
      "person": {
        "firstname": "Alberto",
        "lastname": "Richmond",
        "address": {
          "city": {
            "name": "Oklahoma City"
          },
          "street": {
            "name": "3rd Avenue",
            "number": 68
          }
        }
      }
    }

