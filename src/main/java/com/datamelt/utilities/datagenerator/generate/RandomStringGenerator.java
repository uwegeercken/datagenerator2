package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomStringGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);

    private FieldConfiguration fieldConfiguration;

    public RandomStringGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
    }

    @Override
    public void generateRandomValue(FieldConfiguration fieldConfiguration) throws Exception {
        int minLength = (Integer) fieldConfiguration.getOptions().get("minLength");
        int maxLength = (Integer) fieldConfiguration.getOptions().get("maxLength");
        String randomCharacters = (String) fieldConfiguration.getOptions().get("randomCharacters");
        Random random = new Random();
        int randomLength = random.nextInt(minLength,maxLength);
        StringBuffer randomString = new StringBuffer();
        for(int i=0;i<randomLength;i++)
        {
            int position = random.nextInt(randomCharacters.toString().length());
            randomString.append(randomCharacters.substring(position, position+1));
        }
        //return new RowField<String>(RandomStringGenerator, field.getName());
    }
}
