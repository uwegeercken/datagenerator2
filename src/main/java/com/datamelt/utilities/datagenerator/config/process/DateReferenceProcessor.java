package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DateReferenceProcessor extends FieldProcessor
{
    private static Logger logger = LoggerFactory.getLogger(DateReferenceProcessor.class);

    private static List<String> availableTransformations = Arrays.asList(
            Transformations.TOQUARTER.getName(),
            Transformations.TOHALFYEAR.getName()
    );

    public DateReferenceProcessor(DataConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    protected void validateConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        checkOptions(fieldConfiguration);
        checkTransformations(fieldConfiguration);
    }

    private void checkTransformations(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        if(fieldConfiguration.getTransformations()!=null)
        {
            for(TransformationConfiguration configuredTransformation : fieldConfiguration.getTransformations())
            {
                if(!availableTransformations.contains(configuredTransformation.getName()))
                {
                    throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], transformation [" + configuredTransformation.getName() + "] is not allowed - must be in list: " + Arrays.toString(availableTransformations.toArray()));
                }

            }
        }
    }

    private void checkOptions(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException
    {
        try
        {
            if ((String) fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey()) == null)
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + DateReferenceOptions.REFERENCE.getKey() + "] - the value can not be null");
            }
            boolean monthValueTransformations = fieldConfiguration.containsTransformation(Transformations.TOQUARTER) || fieldConfiguration.containsTransformation(Transformations.TOHALFYEAR);
            if (!((String) fieldConfiguration.getOptions().get(DateReferenceOptions.DATE_FORMAT.getKey())).equals("MM") && monthValueTransformations)
            {
                throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + DateReferenceOptions.DATE_FORMAT.getKey() + "] - the transformation [" + Transformations.TOQUARTER.getName() + ", " + Transformations.TOHALFYEAR.getName() + "] can only be used with dateFormat 'MM'");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + DateReferenceOptions.REFERENCE.getKey() + "] - the value must be of type string");
        }

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat((String)fieldConfiguration.getOptions().get(DateReferenceOptions.DATE_FORMAT.getKey()));
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + fieldConfiguration.getName() + "], option [" + DateReferenceOptions.DATE_FORMAT.getKey() + "] - the value can not be parsed as a SimpleDateFormat");
        }

    }

    @Override
    protected void setDefaultOptions(FieldConfiguration fieldConfiguration)
    {
        for(RandomDateOptions defaultOption : RandomDateOptions.values())
        {
            if(!fieldConfiguration.getOptions().containsKey(defaultOption.getKey()))
            {
                fieldConfiguration.getOptions().put(defaultOption.getKey(), defaultOption.getDefaultValue());
            }
        }
    }

    @Override
    protected void processConfiguration(FieldConfiguration fieldConfiguration) throws InvalidConfigurationException {

    }
}
