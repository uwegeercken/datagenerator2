package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RegularExpressionOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.regex.CharacterGenerator;
import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionParser;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RegularExpressionGenerator implements RandomValueGenerator<String>
{
    private static final Logger logger = LoggerFactory.getLogger(RegularExpressionGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;
    private final String pattern;
    private final RegularExpressionParser regularExpressionParser;
    private final List<CharacterGenerator> regularExpressionGenerators;
    private final List<TransformationMethod> transformationMethods;

    public RegularExpressionGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException, InvalidConfigurationException
    {
        this.fieldConfiguration = fieldConfiguration;

        pattern = (String) fieldConfiguration.getOptions().get(RegularExpressionOptions.PATTERN.getKey());
        regularExpressionParser = new RegularExpressionParser();
        regularExpressionGenerators = regularExpressionParser.translate(pattern);
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
            return regularExpressionGenerators.stream()
                    .map(CharacterGenerator::generateValue)
                    .collect(Collectors.joining());
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
