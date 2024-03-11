package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.process.TransformationExecutionException;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

public class DateReferenceGenerator implements RandomValueGenerator<String>
{
    private final static Logger logger = LoggerFactory.getLogger(DateReferenceGenerator.class);
    private static final Class<String> BASE_DATATYPE = String.class;
    private FieldConfiguration fieldConfiguration;
    private List<TransformationMethod> transformationMethods;
    private String reference;
    private RowField<?> referenceRowField;
    private SimpleDateFormat dateFormat;
    public DateReferenceGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey()) instanceof String)
        {
            reference = ((String) fieldConfiguration.getOptions().get(DateReferenceOptions.REFERENCE.getKey()));
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()) instanceof String)
        {
            dateFormat = new SimpleDateFormat((String) fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()));
        }
        transformationMethods = prepareMethods(BASE_DATATYPE, fieldConfiguration);
    }

    public void addReferenceRowField(RowField<?> referenceRowField)
    {
        this.referenceRowField = referenceRowField;
    }

    @Override
    public String generateRandomValue()
    {
        RandomValueProvider<?> referenceDateGenerator = (RandomValueProvider<?>) referenceRowField.getGenerator();
        return dateFormat.format(referenceDateGenerator.getGeneratedRandomValue()) ;
    }

    @Override
    public String transformRandomValue(String value) throws TransformationExecutionException
    {
        return TransformationExecutor.executeAll(value, transformationMethods);
    }
}
