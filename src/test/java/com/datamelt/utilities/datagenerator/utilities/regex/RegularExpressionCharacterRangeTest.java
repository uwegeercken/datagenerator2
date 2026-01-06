package com.datamelt.utilities.datagenerator.utilities.regex;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionCharacterRangeTest
{
    @Test
    public void characterGroupHasCorrectLength() throws InvalidConfigurationException
    {
        String characterRange = "[A-C]";
        String multiplierValue = "{10}";
        RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(multiplierValue);
        RegularExpressionCharacterRange range = new RegularExpressionCharacterRange(characterRange, multiplier);

        String result = range.generateValue();
        assertEquals(10, result.length());
    }

    @Test
    public void parsedCharacterRangeContainsAllCharacters() throws InvalidConfigurationException
    {
        RegularExpressionCharacterRange range = new RegularExpressionCharacterRange("[A-C6-9d-fxYZ]");
        String allCharacters = range.getAllInvolvedCharacters();

        assert allCharacters.chars()
                .mapToObj(ch -> (char) ch)
                .collect(Collectors.toSet())
                .containsAll(Arrays.asList('A', 'B', 'C', 'd', 'e', 'f', '6', '7', '8', '9', 'x', 'Y', 'Z'));
    }

    @Test
    public void parsedCharacterRangeRemovesDuplicates() throws InvalidConfigurationException
    {
        RegularExpressionCharacterRange range = new RegularExpressionCharacterRange("[A-C]ABC");
        String allCharacters = range.getAllInvolvedCharacters();

        assert allCharacters.chars()
                .mapToObj(ch -> (char) ch)
                .collect(Collectors.toSet())
                .containsAll(Arrays.asList('A', 'B', 'C'));
    }
}