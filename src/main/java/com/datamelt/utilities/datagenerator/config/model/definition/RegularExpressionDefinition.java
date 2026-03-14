package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.options.*;
import com.datamelt.utilities.datagenerator.utilities.type.DataTypeDuckDb;

import java.util.List;

public final class RegularExpressionDefinition
{
    private RegularExpressionDefinition() {}

    public static final List<String> TRANSFORMATIONS = List.of(
            Transformations.LOWERCASE.getName(),
            Transformations.UPPERCASE.getName(),
            Transformations.REMOVE.getName(),
            Transformations.TOLONG.getName(),
            Transformations.TOBOOLEAN.getName(),
            Transformations.TODOUBLE.getName()
    );

    public static final List<DataTypeDuckDb> OUTPUT_TYPES = List.of(
            DataTypeDuckDb.VARCHAR,
            DataTypeDuckDb.LONG,
            DataTypeDuckDb.BOOLEAN,
            DataTypeDuckDb.DOUBLE
    );

    public static final List<FieldOption> OPTIONS = List.of(
            new FieldOption(OptionKey.PATTERN, "[A-Za-z0-9]{1,10}",
                    OptionValidations.IS_VALID_REGEX_PATTERN,
                    "the pattern is invalid - check character groups and multipliers"),
            new FieldOption(OptionKey.OUTPUT_TYPE, DataTypeDuckDb.VARCHAR.name())
    );
}