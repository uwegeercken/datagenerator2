package com.datamelt.utilities.datagenerator.config.model.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Transformations
{
    LOWERCASE ("lowercase", String.class),
    UPPERCASE("uppercase", String.class),
    NEGATE("negate", Long.class),
    ROUND("round", Double.class),
    REVERSE("reverse", String.class),
    PREPEND("prepend", String.class),
    APPEND("append", String.class),
    ENCRYPT("encrypt", String.class),
    BASE64ENCODE("base64encode", String.class);

    private String name;
    private Class clazz;

    Transformations(String name, Class clazz)
    {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName()
    {
        return name;
    }

    public Class getClazz()
    {
        return clazz;
    }

    public static List<String> getValues(Class clazz)
    {
        List<String> possibleValues = new ArrayList<>();
        for (Transformations transformation : values())
        {
            if (transformation.clazz == clazz)
            {
                possibleValues.add(transformation.name);
            }
        }
        return possibleValues;
    }
}
