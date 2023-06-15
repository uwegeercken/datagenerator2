package com.datamelt.utilities.datagenerator.utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataTransformer
{
    public static String lowercase(String value)
    {
        return value.toLowerCase();
    }

    public static String uppercase(String value)
    {
        return value.toUpperCase();
    }

    public static String base64encode(String value)
    {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String reverse(String value)
    {
        StringBuffer buffer = new StringBuffer();
        for(int i=value.length()-1;i>=0;i--)
        {
            buffer.append(value.substring(i, i+1));
        }
        return buffer.toString();
    }

    public static String prepend(String value, String prefix)
    {
        return prefix + value;
    }

    public static long negate(long value)
    {
        return -value;
    }
}
