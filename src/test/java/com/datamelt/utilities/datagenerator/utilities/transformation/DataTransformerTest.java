package com.datamelt.utilities.datagenerator.utilities.transformation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTransformerTest
{
    @Test
    @DisplayName("testing reverse")
    void validateReverseString() throws Exception
    {
        String input = "Hello World";
        assertEquals("dlroW olleH", DataTransformer.reverse(input));
    }

    @Test
    @DisplayName("testing uppercase")
    void validateUppercaseString() throws Exception
    {
        String input = "Hello World";
        assertEquals("HELLO WORLD", DataTransformer.uppercase(input));
    }

    @Test
    @DisplayName("testing lowercase")
    void validateLowercaseString() throws Exception
    {
        String input = "Hello World";
        assertEquals("hello world", DataTransformer.lowercase(input));
    }

    @Test
    @DisplayName("testing base64encode")
    void validateBase64EncodedString() throws Exception
    {
        String input = "Hello World";
        assertEquals("SGVsbG8gV29ybGQ=", DataTransformer.base64encode(input));
    }

    @Test
    @DisplayName("testing prepend")
    void validatePrefixedString() throws Exception
    {
        String input = "Hello World";
        String prefix = "Message - ";
        assertEquals("Message - Hello World", DataTransformer.prepend(input, prefix));
    }

    @Test
    @DisplayName("testing append")
    void validatePostfixedString() throws Exception
    {
        String input = "Hello World";
        String postfix = " - Message";
        assertEquals("Hello World - Message", DataTransformer.append(input, postfix));
    }

    @Test
    @DisplayName("testing negate long")
    void validateNegatedLong() throws Exception
    {
        long input = 1000;
        assertEquals(-input, DataTransformer.negate(input));
    }

    @Test
    @DisplayName("testing negate double")
    void validateNegatedDouble() throws Exception
    {
        double input = 123.45;
        assertEquals(-input, DataTransformer.negate(input));
    }

    @Test
    @DisplayName("testing rounded double with two decimals")
    void validateRoundedDouble() throws Exception
    {
        double input = 123.456789;
        assertEquals(123.46, DataTransformer.round(input,2l));
    }

    @Test
    @DisplayName("testing rounded double no decimals")
    void validateRoundedDoubleNoDecimals() throws Exception
    {
        double input = 123.456789;
        assertEquals(123, DataTransformer.round(input,0l));
    }

    @Test
    @DisplayName("testing maskLeading")
    void validateMaskLeading() throws Exception
    {
        String input = "1234567890";
        assertEquals("#####67890", DataTransformer.maskLeading(input, 5L, "#"));
    }

    @Test
    @DisplayName("testing maskLeading all")
    void validateMaskLeadingAll() throws Exception
    {
        String input = "1234567890";
        assertEquals("##########", DataTransformer.maskLeading(input, 55L, "#"));
    }

    @Test
    @DisplayName("testing maskLeading none")
    void validateMaskLeadingNone() throws Exception
    {
        String input = "1234567890";
        assertEquals("1234567890", DataTransformer.maskLeading(input, 0L, "#"));
    }

    @Test
    @DisplayName("testing maskLeading double character")
    void validateMaskLeadingDoubleCharacter() throws Exception
    {
        String input = "1234567890";
        assertEquals("#:#:34567890", DataTransformer.maskLeading(input, 2L, "#:"));
    }

    @Test
    @DisplayName("testing String to Long conversion")
    void validateStringToLongConversion() throws Exception
    {
        String input = "12345";
        assertEquals(12345, DataTransformer.toLong(input));
    }

    @Test
    @DisplayName("testing Regex to Double conversion")
    void validateRegularExpressionToLongConversion() throws Exception
    {
        String input = "6789.34";
        assertEquals(6789.34, DataTransformer.toDouble(input));
    }

    @Test
    @DisplayName("testing String to Boolean conversion")
    void validateStringToBooleanConversion() throws Exception
    {
        String input1 = "true";
        String input2 = "false";
        assertEquals(true, DataTransformer.toBoolean(input1));
        assertEquals(false, DataTransformer.toBoolean(input2));
    }

    @Test
    @DisplayName("testing Long to Boolean conversion")
    void validateLongToBooleanConversion() throws Exception
    {
        long input1 = 1;
        long input2 = 0;
        assertEquals(true, DataTransformer.toBoolean(input1));
        assertEquals(false, DataTransformer.toBoolean(input2));
    }

    @Test
    @DisplayName("testing Long to Boolean conversion 2")
    void validateLongToBooleanConversion2() throws Exception
    {
        long input1 = 167;
        long input2 = -125;
        assertEquals(true, DataTransformer.toBoolean(input1));
        assertEquals(false, DataTransformer.toBoolean(input2));
    }




    @Test
    @DisplayName("testing maskTrailing")
    void validateMaskTrailing() throws Exception
    {
        String input = "1234567890";
        assertEquals("12345#####", DataTransformer.maskTrailing(input, 5L, "#"));
    }

    @Test
    @DisplayName("testing maskTrailing all")
    void validateMaskTrailingAll() throws Exception
    {
        String input = "1234567890";
        assertEquals("##########", DataTransformer.maskTrailing(input, 99L, "#"));
    }

    @Test
    @DisplayName("testing maskTrailing none")
    void validateMaskTrailingNone() throws Exception
    {
        String input = "1234567890";
        assertEquals("1234567890", DataTransformer.maskTrailing(input, 0L, "#"));
    }

    @Test
    @DisplayName("testing replaceAll")
    void validateStringReplaceAll() throws Exception
    {
        String input = "This is my world";
        assertEquals("This is your world", DataTransformer.replaceAll(input, "my", "your"));
    }

    @Test
    @DisplayName("testing replaceAll multiple")
    void validateStringReplaceAllMultiple() throws Exception
    {
        String input = "This is my world for my children";
        assertEquals("This is your world for your children", DataTransformer.replaceAll(input, "my", "your"));
    }

    @Test
    @DisplayName("remove specified characters from string")
    void removeSpecifiedCharacters()
    {
        assertEquals("Hll Wrld", DataTransformer.remove("Hello World", "eo"));
    }

    @Test
    @DisplayName("remove returns original string when no characters match")
    void removeNoMatchingCharacters()
    {
        assertEquals("Hello World", DataTransformer.remove("Hello World", "xyz"));
    }

    @Test
    @DisplayName("remove all characters returns empty string")
    void removeAllCharacters()
    {
        assertEquals("", DataTransformer.remove("abc", "abc"));
    }

    @Test
    @DisplayName("remove handles empty removal set")
    void removeEmptyRemovalSet()
    {
        assertEquals("Hello", DataTransformer.remove("Hello", ""));
    }

    @Test
    @DisplayName("remove handles special characters")
    void removeSpecialCharacters()
    {
        assertEquals("HelloWorld", DataTransformer.remove("Hello-World!", "-!"));
    }

    @ParameterizedTest(name = "month {0} maps to {1}")
    @CsvSource({
            "01, Q1",
            "02, Q1",
            "03, Q1",
            "04, Q2",
            "05, Q2",
            "06, Q2",
            "07, Q3",
            "08, Q3",
            "09, Q3",
            "10, Q4",
            "11, Q4",
            "12, Q4"
    })
    @DisplayName("toQuarter maps all months correctly")
    void toQuarterMapsAllMonths(String month, String expectedQuarter)
    {
        assertEquals(expectedQuarter, DataTransformer.toQuarter(month));
    }

    @Test
    @DisplayName("toQuarter returns original value for invalid month below range")
    void toQuarterInvalidMonthBelowRange()
    {
        assertEquals("0", DataTransformer.toQuarter("0"));
    }

    @Test
    @DisplayName("toQuarter returns original value for invalid month above range")
    void toQuarterInvalidMonthAboveRange()
    {
        assertEquals("13", DataTransformer.toQuarter("13"));
    }

    // --- toHalfYear ---

    @ParameterizedTest(name = "month {0} maps to {1}")
    @CsvSource({
            "01, H1",
            "02, H1",
            "03, H1",
            "04, H1",
            "05, H1",
            "06, H1",
            "07, H2",
            "08, H2",
            "09, H2",
            "10, H2",
            "11, H2",
            "12, H2"
    })
    @DisplayName("toHalfYear maps all months correctly")
    void toHalfYearMapsAllMonths(String month, String expectedHalfYear)
    {
        assertEquals(expectedHalfYear, DataTransformer.toHalfYear(month));
    }

    @Test
    @DisplayName("toHalfYear returns original value for invalid month below range")
    void toHalfYearInvalidMonthBelowRange()
    {
        assertEquals("0", DataTransformer.toHalfYear("0"));
    }

    @Test
    @DisplayName("toHalfYear returns original value for invalid month above range")
    void toHalfYearInvalidMonthAboveRange()
    {
        assertEquals("13", DataTransformer.toHalfYear("13"));
    }
}