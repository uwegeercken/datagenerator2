package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class CategoryGenerator implements RandomValueGenerator<String>
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    private final FieldConfiguration fieldConfiguration;
    private static final Class<String> BASE_DATATYPE = String.class;
    private final List<TransformationMethod> transformationMethods;

    public CategoryGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
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
                    logger.error("error generating random value: {}", ex.getMessage());
                }
                counter++;
            }
            logger.trace("field [{}] - values and weights {}", fieldConfiguration.getName(), fieldConfiguration.getValuesAndWeights());
            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", fieldConfiguration.getName(), randomPercentValue, sum, fieldConfiguration.getValues().get(counter - 1).getValue());
            return fieldConfiguration.getValues().get(counter-1).getValue();
        }
        else
        {
            int randomValue = random.nextInt(1, fieldConfiguration.getValues().size()+1);
            return fieldConfiguration.getValues().get(randomValue-1).getValue();
        }
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }


}
