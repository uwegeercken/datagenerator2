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

    public static int negate(Integer value)
    {
        return -value;
    }
}
