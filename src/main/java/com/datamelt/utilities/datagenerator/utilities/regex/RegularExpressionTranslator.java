package com.datamelt.utilities.datagenerator.utilities.regex;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class RegularExpressionTranslator2
{
    private static final String OPEN_CURLY_BRACKET = "{";
    private static final String CLOSE_CURLY_BRACKET = "}";
    private static final String OPEN_SQUARE_BRACKET = "[";
    private static final String CLOSE_SQUARE_BRACKET = "]";

    private List<RegularExpressionCharacter> characters = new ArrayList<>();

    public String translate(String pattern) throws InvalidConfigurationException
    {
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<pattern.length();i++)
        {
            String character = pattern.substring(i,i+1);
            String nextCharacter = getNextCharacter(pattern,i);
            if(nextCharacter.equals(OPEN_CURLY_BRACKET))
            {
                int closingCurlyBracketPosition = findNextClosingCurlyBracket(pattern,i);
                String multiplierValue = pattern.substring(i+1,closingCurlyBracketPosition);
                RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(multiplierValue);
                characters.add(new RegularExpressionCharacter(character, multiplier));
            }
            else
            {
                characters.add(new RegularExpressionCharacter(character));
            }


        }

        characters.stream()
                .map()
        return buffer.toString();
    }

    private String getNextCharacter(String pattern, int position)
    {
        String nextCharacter = pattern.substring(position+1,position+2);
        return nextCharacter;
    }

    private int findNextClosingCurlyBracket(String pattern, int position)
    {
        int result = pattern.indexOf("}", position);
        return result;
    }

}
