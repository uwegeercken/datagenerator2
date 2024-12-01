package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CategoryProcessor extends FieldProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(CategoryProcessor.class);
    private static final List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.BASE64ENCODE.getName(),
            Transformations.PREPEND.getName(),
            Transformations.APPEND.getName(),
            Transformations.REVERSE.getName(),
            Transformations.ENCRYPT.getName(),
            Transformations.TRIM.getName(),
            Transformations.MASKLEADING.getName(),
            Transformations.MASKTRAILING.getName(),
            Transformations.REPLACEALL.getName()
    );

    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR
    );

    private static final List<FieldOption> availableOptions = Arrays.asList(
      new FieldOption(OptionKey.CATEGORY_FILE_SEPARATOR, ","),
      new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public CategoryProcessor(FieldConfiguration fieldConfiguration)
    {
        super(fieldConfiguration);
    }

    @Override
    protected void validateConfiguration() throws InvalidConfigurationException
    {
        checkOptions();
        checkTotalNumberOfFieldValues();
        checkFieldWeights();
        checkTotalFieldWeight();
        checkFieldWeightsDistribution();
    }

    @Override
    protected void processConfiguration() throws InvalidConfigurationException
    {
        distributeWeightValues();
        removeZeroWeightValues();
    }

    @Override
    protected List<String> getAvailableTransformations()
    {
        return availableTransformations;
    }

    @Override
    protected List<DataTypeDuckDb> getAvailableOutputTypes()
    {
        return availableOutputTypes;
    }

    @Override
    protected List<FieldOption> getAvailableOptions()
    {
        return availableOptions;
    }

    private void checkOptions() throws InvalidConfigurationException
    {
    }

    private void checkTotalNumberOfFieldValues() throws InvalidConfigurationException
    {
        if(getFieldConfiguration().getValues().size()==0)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "] - the number of values can not be zero");
        }
    }


    private void checkTotalFieldWeight() throws InvalidConfigurationException
    {
        if(getFieldConfiguration().getSumOfWeights()>100)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "] - sum of weights cannot be larger than 100");
        }
    }

    private void checkFieldWeights() throws InvalidConfigurationException
    {
        if(getFieldConfiguration().getValues()!=null)
        {
           for(FieldConfigurationValue value : getFieldConfiguration().getValues())
           {
               if(value.getWeight() < -1)
               {
                   throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], value [" + value.getValue() + "] - value of weight cannot be smaller than -1");
               }
               if(value.getWeight() > 100)
               {
                   throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], value [" + value.getValue() + "] - value of weight cannot be greater than 100");
               }
           }
        }
    }

    private void checkFieldWeightsDistribution() throws InvalidConfigurationException
    {
        if(getFieldConfiguration().getValues() != null && getFieldConfiguration().getNumberOfDefaultWeights() > 0 && getFieldConfiguration().getNumberOfDefaultWeights()!= getFieldConfiguration().getValues().size())
        {
            double calculatedWeightDistribution = (100 - getFieldConfiguration().getSumOfWeights()) / getFieldConfiguration().getNumberOfDefaultWeights();
            if(calculatedWeightDistribution < 1)
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "] value for fields without weight definition can not be distributed to at least 1 percent");
            }
        }
    }

    private void removeZeroWeightValues() throws InvalidConfigurationException
    {
        if(getFieldConfiguration().getValues()!=null)
        {
            Iterator<FieldConfigurationValue> iterator = getFieldConfiguration().getValues().iterator();
            while (iterator.hasNext())
            {
                FieldConfigurationValue value = iterator.next();
                if(value.getWeight()==0)
                {
                    logger.debug("field [{}] - removing value [{}] because weight is set to zero", getFieldConfiguration().getName(), value.getValue());
                    iterator.remove();
                }
            }
        }
    }

    private void distributeWeightValues() throws InvalidConfigurationException
    {
        if (getFieldConfiguration().getNumberOfDefaultWeights()!= getFieldConfiguration().getValues().size() && getFieldConfiguration().getNumberOfDefaultWeights()>0)
        {
            int numberOfValues = getFieldConfiguration().getNumberOfFieldValues();
            int sumOfWeights = getFieldConfiguration().getSumOfWeights();
            if (sumOfWeights < 100)
            {
                int averageWeightValue = (100 - sumOfWeights) / getFieldConfiguration().getNumberOfDefaultWeights();
                if (averageWeightValue > 0)
                {
                    int remainder = (100 - sumOfWeights) % getFieldConfiguration().getNumberOfDefaultWeights();

                    logger.debug("field [{}] - total elements [{}]. elements without weight definition: [{}]", getFieldConfiguration().getName(), numberOfValues, getFieldConfiguration().getNumberOfDefaultWeights());
                    if(remainder!=0) {
                        logger.debug("field [{}] - distributing weight over [{}] elements: [{}] * [{}] and [{}] * [{}]", getFieldConfiguration().getName(), getFieldConfiguration().getNumberOfDefaultWeights(), remainder, averageWeightValue + 1, getFieldConfiguration().getNumberOfDefaultWeights() - remainder, averageWeightValue);
                    }
                    else
                    {
                        logger.debug("field [{}] - distributing weight over [{}] elements: [{}] * [{}]", getFieldConfiguration().getName(), getFieldConfiguration().getNumberOfDefaultWeights(), getFieldConfiguration().getNumberOfDefaultWeights() - remainder, averageWeightValue);

                    }
                    int counter = 0;
                    for (FieldConfigurationValue value : getFieldConfiguration().getValues())
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
                    throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "] - cannot distribute at least 1 percent of weight to the values which have no weight defined");
                }
            }
        }
        else if(getFieldConfiguration().getNumberOfDefaultWeights()!= getFieldConfiguration().getValues().size() && getFieldConfiguration().getSumOfWeights()<100)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "] - the total sum of weight values must be 100");
        }
    }
}
