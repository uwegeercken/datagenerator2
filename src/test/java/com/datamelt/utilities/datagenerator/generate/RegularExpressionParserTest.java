package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.regex.CharacterGenerator;
import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionParserTest
{
    @Test
    void singleDigitMultiplierReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("ABC{4}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());

        assertEquals("ABCCCC", result);
    }

    @Test
    void singleDigitMultipliersReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("ABC{4}DEFGH{3}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());

        assertEquals("ABCCCCDEFGHHH", result);
    }

    @Test
    void singleDigitMultiplierWithTrailingCharactersReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("ABC{1}XYZ");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());

        assertEquals("ABCXYZ", result);
    }

    @Test
    void twoDigitMultiplierReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("ABC{1,4}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());
        assertTrue(result.length() <= 6 && result.length() >= 3);
    }

    @Test
    void characterRangeWithMultiplierReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("XYZ-[A-Za-z0-9]{6}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());
        assertEquals(10, result.length());
    }

    @Test
    void characterRangeAtStartOfPatternReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("[A-Za-z0-9]{6}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());
        assertEquals(6, result.length());
    }

    @Test
    void characterRangeWithTwoDigitMultiplierReturnsCorrectSequence() throws InvalidConfigurationException
    {
        RegularExpressionParser translator = new RegularExpressionParser();

        List<CharacterGenerator> regularExpressionCharacters = translator.translate("[A-Za-z0-9]{1,6}");
        String result = regularExpressionCharacters.stream()
                .map(CharacterGenerator::generateValue)
                .collect(Collectors.joining());
        assertTrue(result.length() <= 6 && result.length() >= 1);
    }
}