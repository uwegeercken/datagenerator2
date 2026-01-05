package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionMultiplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionMultiplierTest
{
    @Test
    @DisplayName("testing one value multiplier")
    void oneValueMultiplierReturnsSameValueForMinAndMax() throws InvalidConfigurationException
    {
        String token = "{5}";
        RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(token);

        assertEquals(5, multiplier.getMinimalNumberOfCharacters());
        assertEquals(5, multiplier.getMaximalNumberOfCharacters());

    }

    @Test
    @DisplayName("testing two value multiplier")
    void twoValuesMultiplierReturnsCorrectValues() throws InvalidConfigurationException
    {
        String token = "{5,9}";
        RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(token);

        assertEquals(5, multiplier.getMinimalNumberOfCharacters());
        assertEquals(9, multiplier.getMaximalNumberOfCharacters());
    }
}