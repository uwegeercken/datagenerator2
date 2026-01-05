package com.datamelt.utilities.datagenerator.utilities.regex;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegularExpressionCharacterRange implements CharacterGenerator
{
    private final String characterRange;
    private final RegularExpressionMultiplier multiplier;
    private final String allCharacters;

    public RegularExpressionCharacterRange(String characterRange, RegularExpressionMultiplier multiplier)
    {
        this.characterRange = characterRange;
        this.multiplier = multiplier;

        allCharacters = parseCharacterRange();
    }

    public RegularExpressionCharacterRange(String characterRange)
    {
        this.characterRange = characterRange;
        this.multiplier = null;

        allCharacters = parseCharacterRange();
    }

    public String getCharacterRange()
    {
        return characterRange;
    }

    public Optional<RegularExpressionMultiplier> getMultiplier()
    {
        return Optional.ofNullable(multiplier);
    }


    @Override
    public String generateValue()
    {
        String generatedCharacters = generateRandomMultipliedValue(allCharacters);
        return generatedCharacters;
    }

    private String parseCharacterRange() {
        String content = characterRange.replaceAll("^\\[|\\]$", "");
        return content.chars()
                .mapToObj(c -> (char) c)
                .flatMap(ch -> {
                    // Skip processing if current character is a range delimiter
                    if (ch == '-') {
                        return Stream.empty();
                    }

                    // Find the index of current character
                    int index = content.indexOf(ch);

                    // Check if it's part of a range
                    if (index + 1 < content.length() && content.charAt(index + 1) == '-'
                            && index + 2 < content.length()) {
                        char start = ch;
                        char end = content.charAt(index + 2);

                        // Expand range
                        return IntStream.rangeClosed(start, end)
                                .mapToObj(rc -> (char) rc);
                    }

                    // Return single character if not part of a range
                    return Stream.of(ch);
                })
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private String generateRandomMultipliedValue(String characterRange)
    {
        int repetitions = 1;
        if (getMultiplier().isPresent())
        {

            if (multiplier.getMaximalNumberOfCharacters() > multiplier.getMinimalNumberOfCharacters())
            {
                repetitions = ThreadLocalRandom.current().nextInt(multiplier.getMinimalNumberOfCharacters(), multiplier.getMaximalNumberOfCharacters() +1);
            }
            else
            {
                repetitions = multiplier.getMinimalNumberOfCharacters();
            }
        }
        StringBuilder result = new StringBuilder(repetitions);
        for (int i = 0; i < repetitions; i++)
        {
            result.append(characterRange.charAt(ThreadLocalRandom.current().nextInt(characterRange.length())));
        }
        return result.toString();
    }
}
