package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.process.RandomStringOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomStringGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);

    private FieldConfiguration fieldConfiguration;

    int minLength;
    int maxLength;
    String randomCharacters;

    public RandomStringGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;

        minLength = (Integer) fieldConfiguration.getOptions().get(RandomStringOptions.MIN_LENGTH.getKey());
        maxLength = (Integer) fieldConfiguration.getOptions().get(RandomStringOptions.MAX_LENGTH.getKey());
        randomCharacters = (String) fieldConfiguration.getOptions().get(RandomStringOptions.RANDOM_CHARACTERS.getKey());
    }

    @Override
    public String generateRandomValue() throws Exception {

        Random random = new Random();
        int randomLength = random.nextInt(minLength,maxLength);
        StringBuffer randomString = new StringBuffer();
        for(int i=0;i<randomLength;i++)
        {
            int position = random.nextInt(randomCharacters.length());
            randomString.append(randomCharacters.substring(position, position+1));
        }
        return randomString.toString();
    }
}
