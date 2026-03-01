package com.datamelt.utilities.datagenerator.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilityTest
{
    @Test
    @DisplayName("getRandomDateTime returns value within year bounds")
    void randomDateTimeWithinYearBounds()
    {
        int minYear = 2020;
        int maxYear = 2025;

        for (int i = 0; i < 1000; i++)
        {
            LocalDateTime result = DateUtility.getRandomDateTime(minYear, maxYear);
            assertTrue(result.getYear() >= minYear && result.getYear() <= maxYear,
                    "year " + result.getYear() + " should be between " + minYear + " and " + maxYear);
        }
    }

    @Test
    @DisplayName("getRandomDateTime minute is in valid range 0-59")
    void randomDateTimeMinuteInValidRange()
    {
        // This test catches the copy-paste bug where DEFAULT_MAXDATE_HOUR (23)
        // was used instead of DEFAULT_MAXDATE_MINUTE (59) for the minute parameter
        int minYear = 2020;
        int maxYear = 2030;

        boolean foundMinuteAbove23 = false;
        for (int i = 0; i < 10000; i++)
        {
            LocalDateTime result = DateUtility.getRandomDateTime(minYear, maxYear);
            int minute = result.getMinute();
            assertTrue(minute >= 0 && minute <= 59,
                    "minute " + minute + " is out of valid range [0, 59]");
            if (minute > 23)
            {
                foundMinuteAbove23 = true;
            }
        }
        // With 10000 samples, we must see minutes above 23 if the bug is fixed
        assertTrue(foundMinuteAbove23, "no minute values above 23 were generated - the minute bug may not be fixed");
    }

    @Test
    @DisplayName("getRandomDateTime second is in valid range 0-59")
    void randomDateTimeSecondInValidRange()
    {
        for (int i = 0; i < 1000; i++)
        {
            LocalDateTime result = DateUtility.getRandomDateTime(2020, 2030);
            int second = result.getSecond();
            assertTrue(second >= 0 && second <= 59,
                    "second " + second + " is out of valid range [0, 59]");
        }
    }

    @Test
    @DisplayName("getRandomDate returns value within year bounds")
    void randomDateWithinYearBounds()
    {
        int minYear = 2020;
        int maxYear = 2025;

        for (int i = 0; i < 1000; i++)
        {
            LocalDate result = DateUtility.getRandomDate(minYear, maxYear);
            assertTrue(result.getYear() >= minYear && result.getYear() <= maxYear,
                    "year " + result.getYear() + " should be between " + minYear + " and " + maxYear);
        }
    }

    @Test
    @DisplayName("getRandomDateTime with same min and max year stays within that year")
    void randomDateTimeWithSameMinMaxYear()
    {
        int year = 2023;
        for (int i = 0; i < 500; i++)
        {
            LocalDateTime result = DateUtility.getRandomDateTime(year, year);
            assertEquals(year, result.getYear());
        }
    }

    @Test
    @DisplayName("getRandomDateMilliseconds returns positive epoch value")
    void randomDateMillisecondsIsPositive()
    {
        long millis = DateUtility.getRandomDateMilliseconds(2020, 2030);
        assertTrue(millis > 0, "epoch milliseconds should be positive");
    }
}