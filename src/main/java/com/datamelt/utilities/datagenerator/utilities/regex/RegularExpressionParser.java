package com.datamelt.utilities.datagenerator.utilities.regex;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegularExpressionTranslator
{
    private static final String OPEN_CURLY_BRACKET = "{";
    private static final String CLOSE_CURLY_BRACKET = "}";
    private static final String OPEN_SQUARE_BRACKET = "[";
    private static final String CLOSE_SQUARE_BRACKET = "]";

    public List<CharacterGenerator> translate(String pattern) throws InvalidConfigurationException
    {
        List<CharacterGenerator> characterGenerators = new ArrayList<CharacterGenerator>();
        for(int i=0;i<pattern.length();i++)
        {
            String character = pattern.substring(i, i+1);
            if(character.equals(OPEN_SQUARE_BRACKET))
            {
                int closingSquareBracketPosition = findNextClosingBracket(pattern, CLOSE_SQUARE_BRACKET, i);
                String characterRange = pattern.substring(i+1, closingSquareBracketPosition+1);
                i = i + (characterRange.length());

                Optional<RegularExpressionMultiplier> multiplier = getMultiplier(pattern,i);
                if(multiplier.isPresent())
                {
                    characterGenerators.add(new RegularExpressionCharacterRange(characterRange, multiplier.get()));
                    i = i + multiplier.get().getMultiplierLength();
                }
                else
                {
                    characterGenerators.add(new RegularExpressionCharacterRange(characterRange));
                }
            }
            else
            {
                Optional<RegularExpressionMultiplier> multiplier = getMultiplier(pattern,i);
                if(multiplier.isPresent())
                {
                    characterGenerators.add(new RegularExpressionCharacter(character, multiplier.get()));
                    i = i + multiplier.get().getMultiplierLength();
                }
                else
                {
                    characterGenerators.add(new RegularExpressionCharacter(character));
                }
            }
        }
        return characterGenerators;
    }

    private Optional<String> getNextCharacter(String pattern, int position)
    {
        if(position+2 <= pattern.length())
        {
            String nextCharacter = pattern.substring(position + 1, position + 2);
            return Optional.of(nextCharacter);
        }
        else
        {
            return Optional.empty();
        }
    }

    private int findNextClosingBracket(String pattern, String bracket, int position)
    {
        int result = pattern.indexOf(bracket, position);
        return result;
    }

    private Optional<RegularExpressionMultiplier> getMultiplier(String pattern, int position) throws InvalidConfigurationException
    {
        Optional<String> nextCharacter = getNextCharacter(pattern, position);
        if(nextCharacter.isPresent() && nextCharacter.get().equals(OPEN_CURLY_BRACKET))
        {
            return Optional.of(constructMultiplier(pattern, position));
        }
        else
        {
            return Optional.empty();
        }
    }

    private RegularExpressionMultiplier constructMultiplier(String pattern, int position) throws InvalidConfigurationException
    {
        int closingCurlyBracketPosition = findNextClosingBracket(pattern, CLOSE_CURLY_BRACKET, position);
        if(closingCurlyBracketPosition == -1)
        {
            throw new InvalidConfigurationException("found open curly bracket at position [" + position + "] but no closing curly bracket in pattern [" + pattern + "]");
        }
        String multiplierValue = pattern.substring(position + 1, closingCurlyBracketPosition + 1);
        if(multiplierValue.equals(OPEN_CURLY_BRACKET + CLOSE_CURLY_BRACKET))
        {
            throw new InvalidConfigurationException("no value specified between curly brackets at position [" + position + "] in pattern [" + pattern + "]");
        }
        return new RegularExpressionMultiplier(multiplierValue);
    }
}
