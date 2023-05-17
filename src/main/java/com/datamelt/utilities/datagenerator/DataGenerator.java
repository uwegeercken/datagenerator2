package com.datamelt.utilities.datagenerator;

import com.datamelt.utilities.datagenerator.config.Field;
import com.datamelt.utilities.datagenerator.config.FieldValue;
import com.datamelt.utilities.datagenerator.config.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.config.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.utilities.Row;
import com.datamelt.utilities.datagenerator.utilities.YamlFileProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.List;
import java.util.Random;

public class DataGenerator
{
    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    private MainConfiguration configuration;
    private long numberOfRowsToGenerate=0;

    public DataGenerator(long numberOfRowsToGenerate)
    {
        this.numberOfRowsToGenerate = numberOfRowsToGenerate;
    }
    public static void main(String[] args)
    {
        if(args!=null && args.length>1)
        {
            DataGenerator generator = new DataGenerator(Long.parseLong(args[1]));
            try {
                generator.loadConfiguration(args[0]);
                for(long i=0;i< generator.numberOfRowsToGenerate;i++)
                {
                    generator.generateRandomValues(generator.configuration.getFields());
                }

            } catch (Exception ex) {
                logger.error("exception processing configuration file: [{}], error: {}", args[0], ex.getMessage());
            }

            System.out.println();
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

    public MainConfiguration getConfiguration()
    {
        return configuration;
    }

    private void generateRandomValues(List<Field> fields)
    {
        Row row = new Row();
        for(Field field : fields)
        {
            Random random = new Random();
            long randomPercentValue = random.nextLong(1,101);
            long sum = 0;
            int counter = 0;

            while(sum < randomPercentValue)
            {
                sum = sum + field.getValues().get(counter).getWeight();
                counter++;
            }

            logger.debug("field [{}] - values and weights {}", field.getName(), field.getValuesAndWeights());
            logger.debug("field [{}] - randomPercentValue [{}], sum [{}], selected value [{}] ", field.getName(), randomPercentValue, sum, field.getValues().get(counter-1).getValue());

            row.addField(field.getName(),field.getValues().get(counter-1).getValue());

        }
        logger.debug("row: {}", row.toString());
    }
}
