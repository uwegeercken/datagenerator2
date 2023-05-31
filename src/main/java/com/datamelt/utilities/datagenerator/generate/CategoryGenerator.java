package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CategoryGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    @Override
    public RowField generateRandomValue(Field field) throws Exception
    {
        RowField rowField = null;
        Random random = new Random();
        if(field.getNumberOfDefaultWeights()!=field.getValues().size())
        {
            int randomPercentValue = random.nextInt(1, 100);
            long sum = 0;
            int counter = 0;
            while (sum <= randomPercentValue)
            {
                try
                {
                    sum = sum + field.getValues().get(counter).getWeight();
                }
                catch (Exception ex)
                {
                    System.out.println();
                }
                counter++;
            }
            rowField = new RowField(field.getName(), field.getValues().get(counter-1).getValue());
            logger.trace("field [{}] - values and weights {}", field.getName(), field.getValuesAndWeights());
            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", field.getName(), randomPercentValue, sum, field.getValues().get(counter - 1).getValue());
        }
        else
        {
            int randomValue = random.nextInt(1, field.getValues().size()+1);
            rowField = new RowField(field.getName(),field.getValues().get(randomValue-1).getValue());
        }
        return rowField;
    }
}
