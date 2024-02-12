package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    private FieldConfiguration fieldConfiguration;
    private static final Class BASE_DATATYPE = String.class;
    private List<TransformationMethod> transformationMethods = new ArrayList<>();

    public CategoryGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;
        this.prepareMethods();
    }

    private void prepareMethods() throws NoSuchMethodException
    {
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(MethodHelper.getMethod(BASE_DATATYPE, transformationConfiguration),transformationConfiguration.getParameters()));
        }
    }

    @Override
    public <T> T generateRandomValue()
    {
        Random random = new Random();
        if(fieldConfiguration.getNumberOfDefaultWeights()!=fieldConfiguration.getValues().size())
        {
            int randomPercentValue = random.nextInt(1, 100);
            long sum = 0;
            int counter = 0;
            while (sum <= randomPercentValue)
            {
                try
                {
                    sum = sum + fieldConfiguration.getValues().get(counter).getWeight();
                }
                catch (Exception ex)
                {
                    System.out.println();
                }
                counter++;
            }
            logger.trace("field [{}] - values and weights {}", fieldConfiguration.getName(), fieldConfiguration.getValuesAndWeights());
            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", fieldConfiguration.getName(), randomPercentValue, sum, fieldConfiguration.getValues().get(counter - 1).getValue());
            return (T) fieldConfiguration.getValues().get(counter-1).getValue();
        }
        else
        {
            int randomValue = random.nextInt(1, fieldConfiguration.getValues().size()+1);
            return (T) fieldConfiguration.getValues().get(randomValue-1).getValue();
        }
    }

    @Override
    public <T> T transformRandomValue(T value) throws InvalidConfigurationException
    {
        return (T) TransformationExecutor.executeAll(value, transformationMethods);
    }
}
