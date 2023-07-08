package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.RandomDateOptions;
import com.datamelt.utilities.datagenerator.config.model.options.RandomLongOptions;
import com.datamelt.utilities.datagenerator.utilities.DateUtility;
import com.datamelt.utilities.datagenerator.utilities.transformation.MethodHelper;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationExecutor;
import com.datamelt.utilities.datagenerator.utilities.transformation.TransformationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomDateGenerator implements RandomValueGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RandomDateGenerator.class);

    private static final Class DATATYPE = Double.class;
    private FieldConfiguration fieldConfiguration;

    private List<TransformationMethod> transformationMethods = new ArrayList<>();

    private int minYear;
    private int maxYear;

    private SimpleDateFormat dateFormat;

    public RandomDateGenerator(FieldConfiguration fieldConfiguration) throws NoSuchMethodException
    {
        this.fieldConfiguration = fieldConfiguration;

        if(fieldConfiguration.getOptions().get(RandomDateOptions.MIN_YEAR.getKey()) instanceof Long)
        {
            minYear = ((Long) fieldConfiguration.getOptions().get(RandomDateOptions.MIN_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.MAX_YEAR.getKey()) instanceof Long)
        {
            maxYear = ((Long) fieldConfiguration.getOptions().get(RandomDateOptions.MAX_YEAR.getKey())).intValue();
        }
        if(fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()) instanceof String)
        {
            dateFormat = new SimpleDateFormat((String) fieldConfiguration.getOptions().get(RandomDateOptions.DATE_FORMAT.getKey()));
        }

        this.prepareMethods();
    }

    private void prepareMethods() throws NoSuchMethodException
    {
        for(TransformationConfiguration transformationConfiguration : fieldConfiguration.getTransformations())
        {
            transformationMethods.add(new TransformationMethod(MethodHelper.getMethod(DATATYPE, transformationConfiguration),transformationConfiguration.getParameters()));
        }
    }

    @Override
    public <T> T  generateRandomValue() throws Exception {

        Random random = new Random();
        Long minYearMilliseonds = DateUtility.getMinDate(minYear);
        Long maxYearMilliseonds = DateUtility.getMaxDate(maxYear);
        Long value = random.nextLong(minYearMilliseonds, maxYearMilliseonds);
        return (T) dateFormat.format(value) ;
    }

    @Override
    public <T> T transformRandomValue(T value) throws Exception
    {
        return (T) TransformationExecutor.executeAll(value, transformationMethods);
    }
}
