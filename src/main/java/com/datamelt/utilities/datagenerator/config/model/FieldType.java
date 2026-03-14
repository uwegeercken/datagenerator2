package com.datamelt.utilities.datagenerator.config.model;

import com.datamelt.utilities.datagenerator.config.model.definition.*;
import com.datamelt.utilities.datagenerator.config.model.options.FieldOption;
import com.datamelt.utilities.datagenerator.config.process.ConfigurationPostProcessor;
import com.datamelt.utilities.datagenerator.config.process.CrossOptionValidator;
import com.datamelt.utilities.datagenerator.generate.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonDeserialize(using = FieldType.FieldTypeDeserializer.class)
public enum FieldType
{
    RANDOMUUID(
            RandomUuidGenerator::new,
            RandomUuidDefinition.TRANSFORMATIONS,
            RandomUuidDefinition.OUTPUT_TYPES,
            RandomUuidDefinition.OPTIONS,
            null,
            null
    ),
    CATEGORY(
            CategoryGenerator::new,
            CategoryDefinition.TRANSFORMATIONS,
            CategoryDefinition.OUTPUT_TYPES,
            CategoryDefinition.OPTIONS,
            CategoryDefinition::validate,
            CategoryDefinition::process
    ),
    RANDOMSTRING(
            RandomStringGenerator::new,
            RandomStringDefinition.TRANSFORMATIONS,
            RandomStringDefinition.OUTPUT_TYPES,
            RandomStringDefinition.OPTIONS,
            RandomStringDefinition::validate,
            null
    ),
    RANDOMINTEGER(
            RandomLongGenerator::new,
            RandomLongDefinition.TRANSFORMATIONS,
            RandomLongDefinition.OUTPUT_TYPES,
            RandomLongDefinition.OPTIONS,
            RandomLongDefinition::validate,
            null
    ),
    RANDOMLONG(
            RandomLongGenerator::new,
            RandomLongDefinition.TRANSFORMATIONS,
            RandomLongDefinition.OUTPUT_TYPES,
            RandomLongDefinition.OPTIONS,
            RandomLongDefinition::validate,
            null
    ),
    RANDOMDOUBLE(
            RandomDoubleGenerator::new,
            RandomDoubleDefinition.TRANSFORMATIONS,
            RandomDoubleDefinition.OUTPUT_TYPES,
            RandomDoubleDefinition.OPTIONS,
            RandomDoubleDefinition::validate,
            null
    ),
    RANDOMDATE(
            RandomDateGenerator::new,
            RandomDateDefinition.TRANSFORMATIONS,
            RandomDateDefinition.OUTPUT_TYPES,
            RandomDateDefinition.OPTIONS,
            RandomDateDefinition::validate,
            null
    ),
    RANDOMTIMESTAMP(
            RandomTimestampGenerator::new,
            RandomTimestampDefinition.TRANSFORMATIONS,
            RandomTimestampDefinition.OUTPUT_TYPES,
            RandomTimestampDefinition.OPTIONS,
            RandomTimestampDefinition::validate,
            null
    ),
    DATEREFERENCE(
            DateReferenceGenerator::new,
            DateReferenceDefinition.TRANSFORMATIONS,
            DateReferenceDefinition.OUTPUT_TYPES,
            DateReferenceDefinition.OPTIONS,
            DateReferenceDefinition::validate,
            null
    ),
    REGULAREXPRESSION(
            RegularExpressionGenerator::new,
            RegularExpressionDefinition.TRANSFORMATIONS,
            RegularExpressionDefinition.OUTPUT_TYPES,
            RegularExpressionDefinition.OPTIONS,
            null,
            null
    );

    private final Function<FieldConfiguration, RandomValueGenerator> randomValueGeneratorFunction;
    private final List<String> availableTransformationNames;
    private final List<DataTypeDuckDb> availableOutputTypes;
    private final List<FieldOption> availableOptions;
    private final CrossOptionValidator crossOptionValidator;
    private final ConfigurationPostProcessor postProcessor;

    FieldType(
            Function<FieldConfiguration, RandomValueGenerator> randomValueGeneratorFunction,
            List<String> availableTransformationNames,
            List<DataTypeDuckDb> availableOutputTypes,
            List<FieldOption> availableOptions,
            CrossOptionValidator crossOptionValidator,
            ConfigurationPostProcessor postProcessor)
    {
        this.randomValueGeneratorFunction = randomValueGeneratorFunction;
        this.availableTransformationNames = availableTransformationNames;
        this.availableOutputTypes = availableOutputTypes;
        this.availableOptions = availableOptions;
        this.crossOptionValidator = crossOptionValidator;
        this.postProcessor = postProcessor;
    }

    public Function<FieldConfiguration, RandomValueGenerator> getRandomValueGeneratorFunction()
    {
        return randomValueGeneratorFunction;
    }

    public List<String> getAvailableTransformationNames()
    {
        return availableTransformationNames;
    }

    public List<DataTypeDuckDb> getAvailableOutputTypes()
    {
        return availableOutputTypes;
    }

    public List<FieldOption> getAvailableOptions()
    {
        return availableOptions;
    }

    public CrossOptionValidator getCrossOptionValidator()
    {
        return crossOptionValidator;
    }

    public ConfigurationPostProcessor getPostProcessor()
    {
        return postProcessor;
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