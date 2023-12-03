package com.datamelt.utilities.datagenerator.config.process;

import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;
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

    private static List<DataTypeDuckDb> availableOutputTypes = Arrays.asList(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG
    );

    private static final List<FieldOption> availableOptions = Arrays.asList(
            new FieldOption(OptionKey.REFERENCE, null),
            new FieldOption(OptionKey.DATE_FORMAT,null),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );

    public DateReferenceProcessor(FieldConfiguration fieldConfiguration)
    {
        super(fieldConfiguration);
    }
    @Override
    protected void validateConfiguration() throws InvalidConfigurationException
    {
        checkOptions();
        checkOutputType();
    }

    private void checkOptions() throws InvalidConfigurationException
    {
        try
        {
            if ((String) getFieldConfiguration().getOptions().get(DateReferenceOptions.REFERENCE.getKey()) == null)
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + DateReferenceOptions.REFERENCE.getKey() + "] - the value can not be null");
            }
            boolean monthValueTransformations = getFieldConfiguration().containsTransformation(Transformations.TOQUARTER) || getFieldConfiguration().containsTransformation(Transformations.TOHALFYEAR);
            if (!((String) getFieldConfiguration().getOptions().get(DateReferenceOptions.DATE_FORMAT.getKey())).equals("MM") && monthValueTransformations)
            {
                throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + DateReferenceOptions.DATE_FORMAT.getKey() + "] - the transformation [" + Transformations.TOQUARTER.getName() + ", " + Transformations.TOHALFYEAR.getName() + "] can only be used with dateFormat 'MM'");
            }
        }
        catch(ClassCastException cce)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + DateReferenceOptions.REFERENCE.getKey() + "] - the value must be of type string");
        }

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat((String)getFieldConfiguration().getOptions().get(DateReferenceOptions.DATE_FORMAT.getKey()));
        }
        catch(Exception ex)
        {
            throw new InvalidConfigurationException("field [" + getFieldConfiguration().getName() + "], option [" + DateReferenceOptions.DATE_FORMAT.getKey() + "] - the value can not be parsed as a SimpleDateFormat");
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
