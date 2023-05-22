package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import com.datamelt.utilities.datagenerator.config.YamlFileProcessor;
import com.datamelt.utilities.datagenerator.utilities.*;
import com.datamelt.utilities.datagenerator.utilities.duckdb.DataStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Random;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private MainConfiguration configuration;
    private long numberOfRowsToGenerate=0;
    private DataStore dataStore;

    public DataGenerator(long numberOfRowsToGenerate)
    {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;
    }
    public static void main(String[] args) throws Exception
    {
        if(args!=null && args.length>1)
        {
            long start = System.currentTimeMillis();
            DataGenerator generator = new DataGenerator(Long.parseLong(args[1]));
            generator.loadConfiguration(args[0]);
            generator.dataStore = new DataStore(generator.configuration);

            logger.debug("generating rows: [{}],", generator.numberOfRowsToGenerate);
            long counter = 0;
            for(long i=0;i < generator.numberOfRowsToGenerate;i++)
            {
                counter++;
                if(counter % 25000 == 0)
                {
                    logger.debug("rows generated: [{}],", counter);
                }
                Row row = generator.generateRandomValues(generator.configuration);
                generator.dataStore.insert(row);
            }
            generator.dataStore.flush();

            logger.debug("total rows generated: [{}],", counter);
            long end = System.currentTimeMillis();
            logger.info("total processing time: [{}] seconds", (end-start)/1000);
        }
        else
        {
            logger.error("the path and name of the configuration yaml file needs to be specified");
        }
    }

    private void loadConfiguration(String configurationFilename) throws Exception
    {
        logger.debug("processing configuration file: [{}],", configurationFilename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        configuration = mapper.readValue(new File(configurationFilename), MainConfiguration.class);

        CategoryFileLoader.loadCategoryFiles(configuration);
        YamlFileProcessor.validateConfiguration(configuration);
        YamlFileProcessor.distributeWeightValues(configuration);
        YamlFileProcessor.removeZeroWeightValues(configuration);
    }

    private Row generateRandomValues(MainConfiguration configuration) throws Exception
    {
        Row row = new Row();
        for(Field field : configuration.getFields())
        {
            Random random = new Random();
            if(field.getNumberOfDefaultWeights()!=field.getValues().size())
            {
                int randomPercentValue = random.nextInt(1, 100);
                long sum = 0;
                int counter = 0;
                while (sum <= randomPercentValue)
                {
                    sum = sum + field.getValues().get(counter).getWeight();
                    counter++;
                }

                row.addField(field.getName(),field.getValues().get(counter-1).getValue());
                logger.trace("field [{}] - values and weights {}", field.getName(), field.getValuesAndWeights());
                logger.trace("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", field.getName(), randomPercentValue, sum, field.getValues().get(counter - 1).getValue());
            }
            else
            {
                int randomValue = random.nextInt(1, field.getValues().size()+1);
                row.addField(field.getName(),field.getValues().get(randomValue-1).getValue());
            }
        }
        return row;
    }
}
