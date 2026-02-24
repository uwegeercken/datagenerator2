package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.FieldOption;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.config.model.options.RegularExpressionOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionMultiplier;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidProcessor extends FieldProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(UuidProcessor.class);

    private static final List<String> availableTransformations = List.of();

    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR);

    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public UuidProcessor(FieldConfiguration fieldConfiguration)
    {
        super(fieldConfiguration);
    }

    @Override
    protected void validateConfiguration() throws InvalidConfigurationException
    {

    }


    @Override
    protected void processConfiguration() throws InvalidConfigurationException {

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
}
