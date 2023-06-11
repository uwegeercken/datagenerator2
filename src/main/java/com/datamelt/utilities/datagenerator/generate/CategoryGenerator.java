package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.CategoryOptions;
import com.datamelt.utilities.datagenerator.utilities.Constants;
import com.datamelt.utilities.datagenerator.utilities.TransformationExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CategoryGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    private FieldConfiguration fieldConfiguration;

    private String[] transform;

    public CategoryGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
        this.transform = ((String) fieldConfiguration.getOptions().get(CategoryOptions.TRANSFORM.getKey())).split(Constants.OPTION_TRANSFORM_DIVIDER);
    }
    @Override
    public String generateRandomValue() throws Exception
    {
        RowField<String> rowField = null;
        Random random = new Random();
        if(fieldConfiguration.getNumberOfDefaultWeights()!= fieldConfiguration.getValues().size())
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
            return applyTransformations(fieldConfiguration.getValues().get(counter-1).getValue());
        }
        else
        {
            int randomValue = random.nextInt(1, fieldConfiguration.getValues().size()+1);
            return applyTransformations(fieldConfiguration.getValues().get(randomValue-1).getValue());
        }
    }

    private String applyTransformations(String value)
    {
        String transformedValue = value;
        for(String transformation : transform)
        {
            switch (transformation.trim())
            {
                case "lowercase":
                    transformedValue =  transformedValue.toLowerCase();
                    break;
                case "uppercase":
                    transformedValue =  transformedValue.toUpperCase();
                    break;
                default:

            }
        }
        return transformedValue;
    }


    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(transform, value);
    }
}
