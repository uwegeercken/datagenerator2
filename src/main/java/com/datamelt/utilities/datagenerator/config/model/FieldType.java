package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.process.*;
import com.datamelt.utilities.datagenerator.generate.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonDeserialize(using = FieldType.FieldTypeDeserializer.class)
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

    private final Function<FieldConfiguration, FieldProcessor> fieldProcessorFunction;
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

    static class FieldTypeDeserializer extends StdDeserializer<FieldType>
    {
        private static final String VALID_VALUES = Arrays.stream(FieldType.values())
                .map(ft -> ft.name().toLowerCase())
                .collect(Collectors.joining(", "));

        FieldTypeDeserializer()
        {
            super(FieldType.class);
        }

        @Override
        public FieldType deserialize(JsonParser parser, DeserializationContext context) throws IOException
        {
            String value = parser.getText();
            try
            {
                return FieldType.valueOf(value.toUpperCase());
            }
            catch (IllegalArgumentException e)
            {
                throw new IOException("invalid field type [" + value + "]. valid values are: " + VALID_VALUES);
            }
        }
    }
}