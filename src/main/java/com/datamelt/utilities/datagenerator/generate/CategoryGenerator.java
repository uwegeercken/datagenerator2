package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CategoryGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    private FieldConfiguration fieldConfiguration;

    public CategoryGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
    }
    @Override
    public void generateRandomValue(FieldConfiguration fieldConfiguration) throws Exception
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
            //return field.getValues().get(counter-1).getValue();
            logger.trace("field [{}] - values and weights {}", fieldConfiguration.getName(), fieldConfiguration.getValuesAndWeights());
            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", fieldConfiguration.getName(), randomPercentValue, sum, fieldConfiguration.getValues().get(counter - 1).getValue());
        }
        else
        {
            int randomValue = random.nextInt(1, fieldConfiguration.getValues().size()+1);
            //return field.getValues().get(randomValue-1).getValue();
        }
        //return rowField;
    }
}
