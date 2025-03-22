package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStringGenerator implements RandomValueGenerator<String>
{
    private final static Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;
    private final long minLength;
    private final long maxLength;
    private final List<TransformationMethod> transformationMethods;
    private final String randomCharacters;

    public RandomStringGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        minLength = (Long) fieldConfiguration.getOptions().get(RandomStringOptions.MIN_LENGTH.getKey());
        maxLength = (Long) fieldConfiguration.getOptions().get(RandomStringOptions.MAX_LENGTH.getKey());
        randomCharacters = (String) fieldConfiguration.getOptions().get(RandomStringOptions.RANDOM_CHARACTERS.getKey());

        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
        long randomLength;
        if(minLength != maxLength) {
            randomLength = ThreadLocalRandom.current().nextLong(minLength, maxLength);
        }
        else
        {
            randomLength = minLength;
        }
        StringBuilder randomString = new StringBuilder();
        for(long i=0;i<randomLength;i++)
        {
            int position = ThreadLocalRandom.current().nextInt(randomCharacters.length());
            randomString.append(randomCharacters.substring(position, position+1));
        }
        return randomString.toString();
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
