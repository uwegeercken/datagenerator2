package com.datamelt.utilities.datagenerator.utilities.regex;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionCharacterRangeTest
{
    @Test
    public void characterGroupHasCorrectLength() throws InvalidConfigurationException
    {
        String characterRange = "[A-Ca-c1-4]";
        String multiplierValue = "{10}";
        RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(multiplierValue);
        RegularExpressionCharacterRange range = new RegularExpressionCharacterRange(characterRange, multiplier);

        String result = range.generateValue();
        assertEquals(10, result.length());
    }
}