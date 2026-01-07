package com.datamelt.utilities.datagenerator.utilities.regex;

public class RegularExpressionMultiplier
{
    private final String token;
    private int minimalNumberOfCharacters = 0;
    private int maximalNumberOfCharacters = 0;
    private boolean maximalNumberProvided = false;

    public RegularExpressionMultiplier(String token)
    {
        this.token = token;
        parseToken();
    }

    public int getMinimalNumberOfCharacters()
    {
        return minimalNumberOfCharacters;
    }

    public int getMaximalNumberOfCharacters()
    {
        return maximalNumberOfCharacters;
    }

    public String getToken()
    {
        return token;
    }

    public int getMultiplierLength()
    {
        return token.length();
    }

    public boolean isMaximalNumberProvided()
    {
        return maximalNumberProvided;
    }

    private void parseToken()
    {
        String content = removeCurlyBrackets(token);
        int commaPosition = content.indexOf(',');
        if(commaPosition == -1)
        {
            minimalNumberOfCharacters = Integer.parseInt(content);
            maximalNumberOfCharacters = minimalNumberOfCharacters;
        }
        else
        {
            String[] values = content.split(",");
            minimalNumberOfCharacters = Integer.parseInt(values[0]);
            maximalNumberOfCharacters = Integer.parseInt(values[1]);
            maximalNumberProvided = true;
        }
    }

    private String removeCurlyBrackets(String token)
    {
        return token.substring(1,token.length()-1);
    }
}
