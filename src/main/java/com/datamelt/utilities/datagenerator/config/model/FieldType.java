package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.*;
import com.datamelt.utilities.datagenerator.generate.*;

import java.util.function.Function;

public enum FieldType
{
    RANDOMUUID(RandomUuidProcessor::new, RandomUuidGenerator::new),
    CATEGORY(CategoryProcessor::new, CategoryGenerator::new),
    RANDOMSTRING(RandomStringProcessor::new, RandomStringGenerator::new),
    RANDOMINTEGER(RandomLongProcessor::new, RandomLongGenerator::new),
    RANDOMLONG(RandomLongProcessor::new, RandomLongGenerator::new),
    RANDOMDATE(RandomDateProcessor::new, RandomDateGenerator::new),
    RANDOMTIMESTAMP(RandomTimestampProcessor::new, RandomTimestampGenerator::new),
    DATEREFERENCE(DateReferenceProcessor::new, DateReferenceGenerator::new),
    RANDOMDOUBLE(RandomDoubleProcessor::new, RandomDoubleGenerator::new),
    REGULAREXPRESSION(RegularExpressionProcessor::new, RegularExpressionGenerator::new);

    private final Function<FieldConfiguration,FieldProcessor> fieldProcessorFunction;
    private final Function<FieldConfiguration, RandomValueGenerator> randomValueGeneratorFunction;

    FieldType(Function<FieldConfiguration, FieldProcessor> fieldProcessorFunction, Function<FieldConfiguration, RandomValueGenerator> randomValueGeneratorFunction)
    {
        this.fieldProcessorFunction = fieldProcessorFunction;
        this.randomValueGeneratorFunction = randomValueGeneratorFunction;
    }

    public Function<FieldConfiguration, FieldProcessor> getFieldProcessorFunction()
    {
        return fieldProcessorFunction;
    }

    public Function<FieldConfiguration, RandomValueGenerator> getRandomValueGeneratorFunction()
    {
        return randomValueGeneratorFunction;
    }
}
