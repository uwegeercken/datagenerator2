package com.datamelt.utilities.datagenerator.utilities.regex;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RegularExpressionCharacter implements CharacterGenerator
{
    private final String character;
    private final RegularExpressionMultiplier multiplier;

    public RegularExpressionCharacter(String character, RegularExpressionMultiplier multiplier)
    {
        this.character = character;
        this.multiplier = multiplier;
    }

    public RegularExpressionCharacter(String character)
    {
        this.character = character;
        this.multiplier = null;
    }

    public String getCharacter()
    {
        return character;
    }

    public Optional<RegularExpressionMultiplier> getMultiplier()
    {
        return Optional.ofNullable(multiplier);
    }

    @Override
    public String generateValue()
    {
        return getMultipliedValue();
    }

    private void calculateRepetitions()
    {
        int numberOfRepeats = ThreadLocalRandom.current().nextInt(multiplier.getMinimalNumberOfCharacters(), multiplier.getMaximalNumberOfCharacters());

    }

    private String getMultipliedValue()
    {
        if (getMultiplier().isPresent())
        {
            int repetitions;
            if (multiplier.getMaximalNumberOfCharacters() > multiplier.getMinimalNumberOfCharacters())
            {
                repetitions = ThreadLocalRandom.current().nextInt(multiplier.getMinimalNumberOfCharacters(), multiplier.getMaximalNumberOfCharacters() +1);
            }
            else
            {
                repetitions = multiplier.getMinimalNumberOfCharacters();
            }
            return character.repeat(repetitions);
        }
        else
        {
            return character;
        }
    }
}
