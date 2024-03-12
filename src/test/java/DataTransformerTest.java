import com.datamelt.utilities.datagenerator.utilities.transformation.DataTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        assertEquals("#####67890", DataTransformer.maskLeading(input, 5, "#"));
    }

    @Test
    @DisplayName("testing maskLeading all")
    void validateMaskLeadingAll() throws Exception
    {
        String input = "1234567890";
        assertEquals("##########", DataTransformer.maskLeading(input, 55, "#"));
    }

    @Test
    @DisplayName("testing maskLeading none")
    void validateMaskLeadingNone() throws Exception
    {
        String input = "1234567890";
        assertEquals("1234567890", DataTransformer.maskLeading(input, 0, "#"));
    }

    @Test
    @DisplayName("testing maskLeading double character")
    void validateMaskLeadingDoubleCharacter() throws Exception
    {
        String input = "1234567890";
        assertEquals("#:#:34567890", DataTransformer.maskLeading(input, 2, "#:"));
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
}