package com.datamelt.utilities.datagenerator.utilities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConfigurationLoader
{
    private static Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);
    public static <T> T load(byte[] configuration, Class clazz) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        return (T) mapper.readValue(configuration,clazz );
    }
}
