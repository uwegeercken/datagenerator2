package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.Arrays;

public enum Transformations
{
    LOWERCASE ("lowercase", new Class[]{String.class}),
    UPPERCASE("uppercase", new Class[]{String.class}),
    TRIM("trim", new Class[]{String.class}),
    NEGATE("negate", new Class[]{Long.class, Double.class}),
    ROUND("round", new Class[]{Double.class}),
    REVERSE("reverse", new Class[]{String.class}),
    PREPEND("prepend", new Class[]{String.class}),
    APPEND("append", new Class[]{String.class}),
    ENCRYPT("encrypt", new Class[]{String.class}),
    BASE64ENCODE("base64encode", new Class[]{String.class}),
    TOQUARTER("toQuarter", new Class[]{String.class}),
    TOHALFYEAR("toHalfYear", new Class[]{String.class}),
    MASKLEADING("maskLeading", new Class[]{String.class}),
    REPLACEALL("replaceAll", new Class[]{String.class}),
    MASKTRAILING("maskTrailing", new Class[]{String.class});

    private final String name;
    private final Class<?>[] classes;

    Transformations(String name, Class<?>[] classes)
    {
        this.name = name;
        this.classes = classes;
    }

    public String getName()
    {
        return name;
    }

    public Class<?>[] getClasses()
    {
        return classes;
    }

    public Class<?> getClass(Class<?> clazz) throws InvalidConfigurationException
    {
        for(Class<?> c : classes)
        {
            if(c.getName().equals(clazz.getName()))
            {
                return c;
            }
        }
        throw new InvalidConfigurationException("the transformation [" + name + "] does not allow type [" + clazz.getName() + "] - possible types: " + Arrays.toString(classes));
    }

    public boolean possibleClass(Class<?> clazz)
    {
        return Arrays.asList(this.classes).contains(clazz);
    }
}
