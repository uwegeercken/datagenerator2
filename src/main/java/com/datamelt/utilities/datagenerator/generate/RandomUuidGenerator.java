package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class UuidGenerator implements RandomValueGenerator<String>
{
    private static final Logger logger = LoggerFactory.getLogger(UuidGenerator.class);

    private static final Class<String> BASE_DATATYPE = String.class;
    private final FieldConfiguration fieldConfiguration;

    private final List<TransformationMethod> transformationMethods;

    public UuidGenerator(FieldConfiguration fieldConfiguration)
    {
        this.fieldConfiguration = fieldConfiguration;

        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    @Override
    public String generateRandomValue()
    {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
