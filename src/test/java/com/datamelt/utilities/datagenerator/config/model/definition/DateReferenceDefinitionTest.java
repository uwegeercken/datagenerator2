package com.datamelt.utilities.datagenerator.config.model.definition;

import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.TransformationConfiguration;
import com.datamelt.utilities.datagenerator.config.model.options.Transformations;
import com.datamelt.utilities.datagenerator.config.process.FieldProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DateReferenceDefinitionTest extends AbstractDefinitionTest
{
    @Override
    protected FieldType getFieldType() { return FieldType.DATEREFERENCE; }

    @Test
    @DisplayName("valid configuration passes")
    void validateValidConfiguration()
    {
        assertValidationPasses(Map.of("reference", "date.fulldate", "dateFormat", "yyyy-MM-dd"));
    }

    @Test
    @DisplayName("null reference throws")
    void validateNullReference()
    {
        assertValidationFails(Map.of("dateFormat", "yyyy-MM-dd"), "reference");
    }

    @Test
    @DisplayName("invalid date format throws")
    void validateInvalidDateFormat()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate", "dateFormat", "not-a-format-%%%"),
                "dateFormat");
    }

    @Test
    @DisplayName("toQuarter transformation without MM dateFormat throws")
    void validateToQuarterWithoutMMFormat() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessor("date.quarter",
                Map.of("reference", "date.fulldate", "dateFormat", "yyyy-MM-dd"));

        TransformationConfiguration transformation = new TransformationConfiguration();
        transformation.setName(Transformations.TOQUARTER.getName());
        processor.getFieldConfiguration().setTransformations(List.of(transformation));

        assertThrows(InvalidConfigurationException.class, () ->
                processor.validateConfiguration());
    }

    @Test
    @DisplayName("toQuarter transformation with MM dateFormat passes")
    void validateToQuarterWithMMFormat() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessor("date.quarter",
                Map.of("reference", "date.fulldate", "dateFormat", "MM"));

        TransformationConfiguration transformation = new TransformationConfiguration();
        transformation.setName(Transformations.TOQUARTER.getName());
        processor.getFieldConfiguration().setTransformations(List.of(transformation));

        assertDoesNotThrow(() -> processor.validateConfiguration());
    }

    @Test
    @DisplayName("toHalfYear transformation without MM dateFormat throws")
    void validateToHalfYearWithoutMMFormat() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessor("date.halfyear",
                Map.of("reference", "date.fulldate", "dateFormat", "yyyy-MM-dd"));

        TransformationConfiguration transformation = new TransformationConfiguration();
        transformation.setName(Transformations.TOHALFYEAR.getName());
        processor.getFieldConfiguration().setTransformations(List.of(transformation));

        assertThrows(InvalidConfigurationException.class, () ->
                processor.validateConfiguration());
    }
}