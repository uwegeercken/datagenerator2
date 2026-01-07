package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.regex.RegularExpressionMultiplier;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionProcessor extends FieldProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(RegularExpressionProcessor.class);

    private static final List<String> availableTransformations = Arrays.asList(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.REMOVE.getName()
    );

    private static final List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR
    );

    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.PATTERN, "[A-Za-z0-9]{1,10}"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public RegularExpressionProcessor(FieldConfiguration fieldConfiguration)
    {
        super(fieldConfiguration);
    }

    @Override
    protected void validateConfiguration() throws InvalidConfigurationException
    {
        checkOptions();
    }

    private void checkOptions() throws InvalidConfigurationException
    {
        String pattern = (String) getFieldConfiguration().getOptions().get(RegularExpressionOptions.PATTERN.getKey());
        if(pattern.isEmpty())
        {
            throw new InvalidConfigurationException("the option pattern cannot be empty for a field of type regularexpression");
        }

        Pattern multiplierPattern = Pattern.compile("\\{[^}]*\\}");
        Matcher matcher = multiplierPattern.matcher(pattern);

        while (matcher.find())
        {
            if(matcher.group().length() <= 2)
            {
                throw new InvalidConfigurationException("no value specified for multiplier [" + matcher.group() + "] in pattern [" + pattern + "]");
            }
            else
            {
                RegularExpressionMultiplier multiplier = new RegularExpressionMultiplier(matcher.group());
                if (multiplier.getMinimalNumberOfCharacters() < 1)
                {
                    throw new InvalidConfigurationException("minimal number of characters in pattern [" + pattern + "] must be greater than zero for multiplier [" + matcher.group() + "]");
                }
                if (multiplier.isMaximalNumberProvided() && multiplier.getMaximalNumberOfCharacters() < 1)
                {
                    throw new InvalidConfigurationException("maximal number of characters in pattern [" + pattern + "] must be greater than zero for multiplier [" + matcher.group() + "]");
                }

                if (multiplier.isMaximalNumberProvided() && multiplier.getMaximalNumberOfCharacters() <= multiplier.getMinimalNumberOfCharacters())
                {
                    throw new InvalidConfigurationException("the multiplier value for the maximum must be greater than the minimum in pattern [" + pattern + "] for multiplier [" + matcher.group() + "]");
                }
            }
        }
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
