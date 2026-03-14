package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public final class CategoryDefinition
{
    private static final Logger logger = LoggerFactory.getLogger(CategoryDefinition.class);

    private CategoryDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of(
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
            Transformations.REPLACEALL.getName(),
            Transformations.REMOVE.getName(),
            Transformations.TOLONG.getName(),
            Transformations.TOBOOLEAN.getName()
    );

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG,
            DataTypeDuckDb.BOOLEAN
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.CATEGORY_FILE_SEPARATOR, ","),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public static void validate(FieldConfiguration config) throws InvalidConfigurationException
    {
        if (config.getValues().size() == 0)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "] - the number of values can not be zero");
        }
        if (config.getSumOfWeights() > 100)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "] - sum of weights cannot be larger than 100");
        }
        for (FieldConfigurationValue value : config.getValues())
        {
            if (value.getWeight() < -1)
            {
                throw new InvalidConfigurationException("field [" + config.getName() + "], value [" + value.getValue() + "] - value of weight cannot be smaller than -1");
            }
            if (value.getWeight() > 100)
            {
                throw new InvalidConfigurationException("field [" + config.getName() + "], value [" + value.getValue() + "] - value of weight cannot be greater than 100");
            }
        }
        if (config.getNumberOfDefaultWeights() > 0 && config.getNumberOfDefaultWeights() != config.getValues().size())
        {
            double calculatedWeightDistribution = (100 - config.getSumOfWeights()) / (double) config.getNumberOfDefaultWeights();
            if (calculatedWeightDistribution < 1)
            {
                throw new InvalidConfigurationException("field [" + config.getName() + "] - value for fields without weight definition can not be distributed to at least 1 percent");
            }
        }
    }

    public static void process(FieldConfiguration config) throws InvalidConfigurationException
    {
        distributeWeightValues(config);
        removeZeroWeightValues(config);
    }

    private static void distributeWeightValues(FieldConfiguration config) throws InvalidConfigurationException
    {
        if (config.getNumberOfDefaultWeights() != config.getValues().size() && config.getNumberOfDefaultWeights() > 0)
        {
            int sumOfWeights = config.getSumOfWeights();
            if (sumOfWeights < 100)
            {
                int averageWeightValue = (100 - sumOfWeights) / config.getNumberOfDefaultWeights();
                if (averageWeightValue > 0)
                {
                    int remainder = (100 - sumOfWeights) % config.getNumberOfDefaultWeights();
                    logger.debug("field [{}] - distributing weight over [{}] elements", config.getName(), config.getNumberOfDefaultWeights());
                    int counter = 0;
                    for (FieldConfigurationValue value : config.getValues())
                    {
                        if (value.getWeight() == FieldConfigurationValue.DEFAULT_WEIGHT)
                        {
                            counter++;
                            value.setWeight(remainder != 0 && counter <= remainder
                                    ? averageWeightValue + 1
                                    : averageWeightValue);
                        }
                    }
                }
                else
                {
                    throw new InvalidConfigurationException("field [" + config.getName() + "] - cannot distribute at least 1 percent of weight to the values which have no weight defined");
                }
            }
        }
        else if (config.getNumberOfDefaultWeights() != config.getValues().size() && config.getSumOfWeights() < 100)
        {
            throw new InvalidConfigurationException("field [" + config.getName() + "] - the total sum of weight values must be 100");
        }
    }

    private static void removeZeroWeightValues(FieldConfiguration config)
    {
        Iterator<FieldConfigurationValue> iterator = config.getValues().iterator();
        while (iterator.hasNext())
        {
            FieldConfigurationValue value = iterator.next();
            if (value.getWeight() == 0)
            {
                logger.debug("field [{}] - removing value [{}] because weight is set to zero", config.getName(), value.getValue());
                iterator.remove();
            }
        }
    }
}