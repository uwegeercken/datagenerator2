package com.datamelt.utilities.datagenerator.utilities;

import java.util.Calendar;

public class DateUtility
{
    private static final int DEFAULT_MAXDATE_MONTH  = 11;
    private static final int DEFAULT_MAXDATE_DAY    = 31;
    private static final int DEFAULT_MAXDATE_HOUR   = 23;
    private static final int DEFAULT_MAXDATE_MINUTE = 59;
    private static final int DEFAULT_MAXDATE_SECOND = 59;

    private static final int DEFAULT_MINDATE_MONTH  = 0;
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

}
