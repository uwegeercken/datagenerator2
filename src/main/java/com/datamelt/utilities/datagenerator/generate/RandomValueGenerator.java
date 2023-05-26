package com.datamelt.utilities.datagenerator.generate;

import com.datamelt.utilities.datagenerator.config.model.MainConfiguration;
import com.datamelt.utilities.datagenerator.utilities.Row;

public interface RandomValueGenerator
{
    Row generateRandomValues(MainConfiguration configuration) throws Exception;
}
