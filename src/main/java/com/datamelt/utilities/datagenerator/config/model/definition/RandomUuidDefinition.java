package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.options.FieldOption;
import com.datamelt.utilities.datagenerator.config.model.options.OptionKey;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class RandomUuidDefinition
{
    private RandomUuidDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of();

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );
}