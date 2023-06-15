package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CategoryProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(CategoryProcessor.class);
    private static List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.PREPEND.getName(),
            Transformations.APPEND.getName(),
            Transformations.REVERSE.getName()
    );

    public CategoryProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkTotalNumberOfFieldValues(fieldConfiguration);
        checkFieldWeights(fieldConfiguration);
        checkTotalFieldWeight(fieldConfiguration);
        checkTransformations(fieldConfiguration);
    }

    @Override
    public void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        // TODO: define method when there are options available
    }

    @Override
    public void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        distributeWeightValues(fieldConfiguration);
        removeZeroWeightValues(fieldConfiguration);
    }

    private void checkTotalNumberOfFieldValues(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getValues().size()==0)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - the number of values can not be zero");
        }
    }

    private void checkTotalFieldWeight(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getSumOfWeights()>100)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - sum of weights cannot be larger than 100");
        }
    }

    private void checkFieldWeights(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getValues()!=null)
        {
           for(FieldConfigurationValue value : fieldConfiguration.getValues())
           {
               if(value.getWeight() < -1)
               {
                   throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], value [" + value.getValue() + "] - value of weight cannot be smaller than -1");
               }
               if(value.getWeight() > 100)
               {
                   throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], value [" + value.getValue() + "] - value of weight cannot be greater than 100");
               }
           }
        }
    }

    private void checkTransformations(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getTransformations()!=null)
        {
            for(TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                if(!availableTransformations.contains(configuredTransformation.getName()))
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is not allowed - must be in list: " + Arrays.toString(availableTransformations.toArray()));
                }
            }
        }
    }

    private void removeZeroWeightValues(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getValues()!=null)
        {
            Iterator<FieldConfigurationValue> iterator = fieldConfiguration.getValues().iterator();
            while (iterator.hasNext())
            {
                FieldConfigurationValue value = iterator.next();
                if(value.getWeight()==0)
                {
                    logger.debug("field [{}] - removing value [{}] because weight is set to zero", fieldConfiguration.getName(), value.getValue());
                    iterator.remove();
                }
            }
        }
    }

    private void distributeWeightValues(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        fieldConfiguration.calculateNumberOfDefaultWeights();
        if (fieldConfiguration.getNumberOfDefaultWeights()>0)
        {
            int numberOfValues = fieldConfiguration.getNumberOfFieldValues();
            int sumOfWeights = fieldConfiguration.getSumOfWeights();
            if (sumOfWeights < 100)
            {
                int averageWeightValue = (100 - sumOfWeights) / fieldConfiguration.getNumberOfDefaultWeights();
                if (averageWeightValue > 0)
                {
                    int remainder = (100 - sumOfWeights) % fieldConfiguration.getNumberOfDefaultWeights();

                    logger.debug("field [{}] - total elements [{}]. elements without weight definition: [{}]", fieldConfiguration.getName(), numberOfValues, fieldConfiguration.getNumberOfDefaultWeights());
                    logger.debug("field [{}] - distributing weight over [{}] elements: [{}] * [{}] and [{}] * [{}]", fieldConfiguration.getName(), fieldConfiguration.getNumberOfDefaultWeights(), remainder, averageWeightValue + 1, fieldConfiguration.getNumberOfDefaultWeights() - remainder, averageWeightValue);
                    int counter = 0;
                    for (FieldConfigurationValue value : fieldConfiguration.getValues())
                    {
                        if (value.getWeight() == FieldConfigurationValue.DEFAULT_WEIGHT)
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
                } else
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - cannot distribute at least 1 percent of weight to the values which have no weight defined");
                }
            }
        }
        else if(fieldConfiguration.getSumOfWeights()<100)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "] - the total sum of weight values must be 100");
        }
        else
        {
            logger.debug("field [{}] - no values with weight defined in config or category file", fieldConfiguration.getName());
        }

    }


}
