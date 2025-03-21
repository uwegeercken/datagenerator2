package com.datamelt.utilities.datagenerator.utilities;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtility
{
    private static final int DEFAULT_MAXDATE_MONTH  = 12;
    private static final int DEFAULT_MAXDATE_DAY    = 31;
    private static final int DEFAULT_MAXDATE_HOUR   = 23;
    private static final int DEFAULT_MAXDATE_MINUTE = 59;
    private static final int DEFAULT_MAXDATE_SECOND = 59;

    private static final int DEFAULT_MINDATE_MONTH  = 1;
    private static final int DEFAULT_MINDATE_DAY    = 1;
    private static final int DEFAULT_MINDATE_HOUR   = 0;
    private static final int DEFAULT_MINDATE_MINUTE = 0;
    private static final int DEFAULT_MINDATE_SECOND = 0;

    public static long getMaxDate(int maximumYear)
    {
        Calendar calMax = Calendar.getInstance();
        calMax.set(Calendar.YEAR, maximumYear);
        calMax.set(Calendar.MONTH, DEFAULT_MAXDATE_MONTH);
        calMax.set(Calendar.DAY_OF_MONTH,DEFAULT_MAXDATE_DAY);
        calMax.set(Calendar.HOUR_OF_DAY, DEFAULT_MAXDATE_HOUR);
        calMax.set(Calendar.MINUTE, DEFAULT_MAXDATE_MINUTE);
        calMax.set(Calendar.SECOND, DEFAULT_MAXDATE_SECOND);
        return calMax.getTimeInMillis();
    }

    public static long getMinDate(int minimumYear)
    {
        Calendar calMin = Calendar.getInstance();
        calMin.set(Calendar.YEAR, minimumYear);
        calMin.set(Calendar.MONTH, DEFAULT_MINDATE_MONTH);
        calMin.set(Calendar.DAY_OF_MONTH,DEFAULT_MINDATE_DAY);
        calMin.set(Calendar.HOUR_OF_DAY, DEFAULT_MINDATE_HOUR);
        calMin.set(Calendar.MINUTE, DEFAULT_MINDATE_MINUTE);
        calMin.set(Calendar.SECOND, DEFAULT_MINDATE_SECOND);
        return calMin.getTimeInMillis();
    }

    public static LocalDate getRandomDate(int minimumYear, int maximumYear)
    {
        LocalDate startDate = LocalDate.of(minimumYear, DEFAULT_MINDATE_MONTH, DEFAULT_MINDATE_DAY);
        LocalDate endDate = LocalDate.of(maximumYear, DEFAULT_MAXDATE_MONTH, DEFAULT_MAXDATE_DAY);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);

        return startDate.plusDays(randomDays);
    }

    public static LocalDateTime getRandomDateTime(int minimumYear, int maximumYear)
    {
        LocalDateTime startDateTime = LocalDateTime.of(minimumYear, DEFAULT_MINDATE_MONTH, DEFAULT_MINDATE_DAY, DEFAULT_MINDATE_HOUR, DEFAULT_MINDATE_MINUTE, DEFAULT_MINDATE_SECOND);
        LocalDateTime endDateTime = LocalDateTime.of(maximumYear, DEFAULT_MAXDATE_MONTH, DEFAULT_MAXDATE_DAY, DEFAULT_MAXDATE_HOUR, DEFAULT_MAXDATE_HOUR, DEFAULT_MAXDATE_SECOND);

        long startEpoch = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endEpoch = endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long randomEpochMillis = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
        return Instant.ofEpochMilli(randomEpochMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long getRandomDateMilliseconds(int minimumYear, int maximumYear)
    {
        LocalDate randomDate = getRandomDate(minimumYear, maximumYear);
        LocalDateTime dateTime = randomDate.atStartOfDay();
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());

        return zonedDateTime.toInstant().toEpochMilli();
    }

}
