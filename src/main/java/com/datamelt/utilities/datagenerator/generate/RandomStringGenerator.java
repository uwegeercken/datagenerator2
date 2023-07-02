package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStringGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);
    private static final Class DATATYPE = String.class;
    private FieldConfiguration fieldConfiguration;

    private long minLength;
    private long maxLength;
    private List<TransformationMethod> transformationMethods = new ArrayList<>();
    String randomCharacters;

    public RandomStringGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        minLength = (Long) fieldConfiguration.getOptions().get(RandomStringOptions.MIN_LENGTH.getKey());
        maxLength = (Long) fieldConfiguration.getOptions().get(RandomStringOptions.MAX_LENGTH.getKey());
        randomCharacters = (String) fieldConfiguration.getOptions().get(RandomStringOptions.RANDOM_CHARACTERS.getKey());

        prepareMethods();
    }
    private void prepareMethods() throws NoSuchMethodException
    {
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(  MethodHelper.getMethod(DATATYPE, transformationConfiguration),transformationConfiguration.getParameters()));
        }
    }
    @Override
    public <T> T generateRandomValue() throws Exception {

        Random random = new Random();
        long randomLength = random.nextLong(minLength,maxLength);
        StringBuffer randomString = new StringBuffer();
        for(long i=0;i<randomLength;i++)
        {
            int position = random.nextInt(randomCharacters.length());
            randomString.append(randomCharacters.substring(position, position+1));
        }
        return (T) randomString.toString();
    }

    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(value, transformationMethods);
    }
}
