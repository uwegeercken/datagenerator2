package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;

import java.util.ArrayList;
import java.util.List;

public interface RandomValueGenerator<T>
{
    T generateRandomValue();
    T transformRandomValue(T value) throws TransformationExecutionException;
    default List<TransformationMethod> prepareMethods(Class clazz, FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        final List<TransformationMethod> transformationMethods = new ArrayList<>();
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            try
            {
                transformationMethods.add(new TransformationMethod(MethodHelper.getMethod(clazz, transformationConfiguration), transformationConfiguration.getParameters()));
            }
            catch(NoSuchMethodException nsme)
            {
                throw new NoSuchMethodException("field [" + fieldConfiguration.getName() + "], " + nsme.getMessage());
            }

        }
        return transformationMethods;
    }
}
