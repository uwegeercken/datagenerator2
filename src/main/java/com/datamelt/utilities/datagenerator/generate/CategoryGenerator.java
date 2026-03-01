package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;

import com.datamelt.utilities.datagenerator.config.model.FieldConfigurationValue;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CategoryGenerator.class);
    private final FieldConfiguration fieldConfiguration;
    private static final Class<String> BASE_DATATYPE = String.class;
    private final List<TransformationMethod> transformationMethods;

    public CategoryGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
//        if(fieldConfiguration.getNumberOfDefaultWeights()!=fieldConfiguration.getValues().size())
//        {
//            int randomPercentValue = ThreadLocalRandom.current().nextInt(1, 101);
//            long sum = 0;
//            int counter = 0;
//            while (sum <= randomPercentValue)
//            {
//                try
//                {
//                    sum = sum + fieldConfiguration.getValues().get(counter).getWeight();
//                }
//                catch (Exception ex)
//                {
//                    logger.error("error generating random value: {}", ex.getMessage());
//                }
//                counter++;
//            }
//            logger.trace("field [{}] - values and weights {}", fieldConfiguration.getName(), fieldConfiguration.getValuesAndWeights());
//            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", fieldConfiguration.getName(), randomPercentValue, sum, fieldConfiguration.getValues().get(counter - 1).getValue());
//            return fieldConfiguration.getValues().get(counter-1).getValue();
//        }
//        else
//        {
//            int randomValue = ThreadLocalRandom.current().nextInt(1, fieldConfiguration.getValues().size()+1);
//            return fieldConfiguration.getValues().get(randomValue-1).getValue();
//        }

        if (fieldConfiguration.getNumberOfDefaultWeights() != fieldConfiguration.getValues().size())
        {
            int randomPercentValue = ThreadLocalRandom.current().nextInt(1, 101);
            long sum = 0;
            int counter = 0;
            List<FieldConfigurationValue> values = fieldConfiguration.getValues();
            while (sum <= randomPercentValue && counter < values.size())
            {
                sum = sum + values.get(counter).getWeight();
                counter++;
            }
            // clamp to last value if rounding left total < 100
            int selectedIndex = Math.min(counter - 1, values.size() - 1);
            logger.trace("field [{}] - values and weights {}", fieldConfiguration.getName(), fieldConfiguration.getValuesAndWeights());
            logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", fieldConfiguration.getName(), randomPercentValue, sum, fieldConfiguration.getValues().get(counter - 1).getValue());
            return values.get(selectedIndex).getValue();
        }
        else
        {
            int randomValue = ThreadLocalRandom.current().nextInt(0, fieldConfiguration.getValues().size());
            return fieldConfiguration.getValues().get(randomValue).getValue();
        }


    }

    @Override
    public Object transformRandomValue(Object value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }


}
