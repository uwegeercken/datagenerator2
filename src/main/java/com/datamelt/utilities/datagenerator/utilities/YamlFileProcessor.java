package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.FieldValue;
import com.datamelt.utilities.datagenerator.config.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class YamlFileProcessor
{
    private static Logger logger = LoggerFactory.getLogger(YamlFileProcessor.class);

    public static void isValidConfiguration(MainConfiguration configuration) throws InvalidConfigurationException
    {
        checkFieldWeights(configuration);
        checkTotalFieldWeight(configuration);
    }

    private static void checkTotalFieldWeight(MainConfiguration configuration) throws InvalidConfigurationException
    {
        for (Field field : configuration.getFields())
        {
            if(field.getSumOfWeights()>100)
            {
                throw new InvalidConfigurationException("field [" + field.getName() + "] - sum of weights cannot be larger than 100");
            }
        }
    }

    private static void checkFieldWeights(MainConfiguration configuration) throws InvalidConfigurationException
    {
        for (Field field : configuration.getFields())
        {
            if(field.getValues()!=null)
            {
               for(FieldValue value : field.getValues())
               {
                   if(value.getWeight() < -1)
                   {
                       throw new InvalidConfigurationException("field [" + field.getName() + "], value [" + value.getName() + "] - value of weight cannot be smaller than -1");
                   }
               }
            }
        }
    }

    public static void removeZeroWeightValues(MainConfiguration configuration) throws InvalidConfigurationException
    {
        for (Field field : configuration.getFields())
        {
            if(field.getValues()!=null)
            {
                Iterator<FieldValue> iterator = field.getValues().iterator();
                while (iterator.hasNext())
                {
                    FieldValue value = iterator.next();
                    if(value.getWeight()==0)
                    {
                        logger.debug("field [{}] - removing value [{}] because weight is set to zero", field.getName(), value.getName());
                        iterator.remove();
                    }
                }
            }
        }
    }

    public static void distributeWeightValues(Field field) throws InvalidConfigurationException
    {
        int numberOfValues = field.getNumberOfFieldValues();
        int numberOfDefaultWeights = field.getNumberOfDefaultWeights();
        int sumOfWeights = field.getSumOfWeights();

        if(sumOfWeights<100 && numberOfDefaultWeights>0)
        {
            int averageWeightValue = (100 - sumOfWeights) / numberOfDefaultWeights;
            if(averageWeightValue>0)
            {
                int remainder = (100 - sumOfWeights) % numberOfDefaultWeights;

                logger.info("field [{}] - total elements [{}]. elements without weight definition: [{}]", field.getName(), numberOfValues, numberOfDefaultWeights);
                logger.info("field [{}] - distributing weight over [{}] elements: [{}] * [{}] and [{}] * [{}]", field.getName(), numberOfDefaultWeights, remainder, averageWeightValue + 1, numberOfDefaultWeights - remainder, averageWeightValue);
                int counter = 0;
                for (FieldValue value : field.getValues())
                {
                    if (value.getWeight() == FieldValue.DEFAULT_WEIGHT)
                    {
                        counter++;
                        if (remainder != 0 && counter <= remainder)
                        {
                            value.setWeight(averageWeightValue + 1);
                        } else
                        {
                            value.setWeight(averageWeightValue);
                        }
                    }
                }
            }
            else
            {
                throw new InvalidConfigurationException("field [" + field.getName() + "] - cannot distribute at least 1 percent of weight to the values which have no weight defined");
            }
        }
    }
}
