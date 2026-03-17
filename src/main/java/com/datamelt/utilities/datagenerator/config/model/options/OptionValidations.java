package com.datamelt.utilities.datagenerator.config.model.options;

import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionMultiplier;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OptionValidations
{
    private OptionValidations() {}

    public static final Predicate<Object> IS_POSITIVE_LONG =
            v -> (Long) v > 0;

    public static final Predicate<Object> IS_NON_NEGATIVE_LONG =
            v -> (Long) v >= 0;

    public static final Predicate<Object> IS_LONG =
            v -> v instanceof Long;

    public static final Predicate<Object> IS_NOT_EMPTY_STRING =
            v -> v != null && !((String) v).isBlank();

    public static final Predicate<Object> IS_PERCENTAGE =
            v -> (Long) v >= 0 && (Long) v <= 100;

    public static final Predicate<Object> IS_VALID_SIMPLE_DATE_FORMAT =
            v -> {
                try
                {
                    new SimpleDateFormat((String) v);
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            };

    public static final Predicate<Object> IS_VALID_DATETIME_FORMAT =
            v -> {
                try
                {
                    DateTimeFormatter.ofPattern((String) v).withZone(ZoneId.of("UTC"));
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            };

    public static final Predicate<Object> IS_VALID_REGEX_PATTERN =
            v -> {
                String pattern = (String) v;
                if (pattern == null || pattern.isEmpty())
                {
                    return false;
                }
                Pattern multiplierPattern = Pattern.compile("\\{[^}]*\\}");
                Matcher matcher = multiplierPattern.matcher(pattern);
                while (matcher.find())
                {
                    if (matcher.group().length() <= 2)
                    {
                        return false;
                    }
                    RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(matcher.group());
                    if (multiplier.getMinimalNumberOfCharacters() < 1)
                    {
                        return false;
                    }
                    if (multiplier.isMaximalNumberProvided() && multiplier.getMaximalNumberOfCharacters() < 1)
                    {
                        return false;
                    }
                    if (multiplier.isMaximalNumberProvided() && multiplier.getMaximalNumberOfCharacters() <= multiplier.getMinimalNumberOfCharacters())
                    {
                        return false;
                    }
                }
                return true;
            };
}