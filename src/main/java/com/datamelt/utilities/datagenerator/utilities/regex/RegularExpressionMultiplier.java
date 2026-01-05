package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;

public class RegularExpressionMultiplier
{
    private int minimalNumberOfCharacters;
    private int maximalNumberOfCharacters;

    public RegularExpressionMultiplier(String token) throws InvalidConfigurationException
    {
        if(token.length() < 3)
        {
            throw new InvalidConfigurationException("minimum length of a multiplier must be 3 characters");
        }
        else
        {
            parseToken(token);
            validate();
        }
    }

    public int getMinimalNumberOfCharacters()
    {
        return minimalNumberOfCharacters;
    }

    public int getMaximalNumberOfCharacters()
    {
        return maximalNumberOfCharacters;
    }

    private void parseToken(String token)
    {
        String content = removeCurlyBrackets(token);
        int commaPosition = content.indexOf(',');
        if(commaPosition == -1)
        {
            minimalNumberOfCharacters = Integer.parseInt(content);
            maximalNumberOfCharacters = Integer.parseInt(content);
        }
        else
        {
            String[] values = content.split(",");
            minimalNumberOfCharacters = Integer.parseInt(values[0]);
            maximalNumberOfCharacters = Integer.parseInt(values[1]);
        }
    }

    private String removeCurlyBrackets(String token)
    {
        return token.substring(1,token.length()-1);
    }

    private void validate() throws InvalidConfigurationException
    {
        if(minimalNumberOfCharacters <= 0 || maximalNumberOfCharacters <= 0)
        {
            throw new InvalidConfigurationException("The minimal number of characters must be greater than zero");
        }
        if(maximalNumberOfCharacters < minimalNumberOfCharacters)
        {
            throw new InvalidConfigurationException("The maximal number of characters must be greater than the minimal number of characters");
        }
    }
}
