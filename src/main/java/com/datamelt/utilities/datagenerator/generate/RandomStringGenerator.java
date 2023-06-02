package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomStringGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);

    String randomCharacters;

    public RandomStringGenerator(String randomCharacters)
    {
        this.randomCharacters = randomCharacters;
    }
    @Override
    public RowField generateRandomValue(Field field) throws Exception {
        int minLength = (Integer) field.getOptions().get("minLength");
        int maxLength = (Integer) field.getOptions().get("maxLength");
        Random random = new Random();
        int randomLength = random.nextInt(minLength,maxLength);
        StringBuffer randomString = new StringBuffer();
        for(int i=0;i<randomLength;i++)
        {
            int position = random.nextInt(randomCharacters.length());
            randomString.append(randomCharacters.substring(position,position+1));
        }
        return new RowField(field.getName(), randomString.toString());
    }
}
