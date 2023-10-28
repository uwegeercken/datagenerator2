package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.DateReferenceOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DateReferenceGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DateReferenceGenerator.class);

    private static final Class BASE_DATATYPE = String.class;
    private FieldConfiguration fieldConfiguration;

    private List<TransformationMethod> transformationMethods = new ArrayList<>();

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

        this.prepareMethods();
    }

    public void addReferenceRowField(RowField<?> referenceRowField)
    {
        this.referenceRowField = referenceRowField;
    }

    private void prepareMethods() throws NoSuchMethodException
    {
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(MethodHelper.getMethod(BASE_DATATYPE, transformationConfiguration),transformationConfiguration.getParameters()));
        }
    }

    @Override
    public <T> T generateRandomValue() throws Exception {

        RandomDateGenerator referenceDateGenerator = (RandomDateGenerator) referenceRowField.getGenerator();
        return (T) dateFormat.format(referenceDateGenerator.getGeneratedRandomValue()) ;
    }

    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(value, transformationMethods);
    }
}
