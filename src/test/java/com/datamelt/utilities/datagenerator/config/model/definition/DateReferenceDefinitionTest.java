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

    @Test
    @DisplayName("valid positive days offset passes")
    void validatePositiveDaysOffset()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "minDaysOffset", 1L,
                "maxDaysOffset", 14L));
    }

    @Test
    @DisplayName("valid negative days offset passes")
    void validateNegativeDaysOffset()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "minDaysOffset", -30L,
                "maxDaysOffset", -7L));
    }

    @Test
    @DisplayName("equal minDaysOffset and maxDaysOffset passes")
    void validateEqualDaysOffset()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "minDaysOffset", 7L,
                "maxDaysOffset", 7L));
    }

    @Test
    @DisplayName("zero days offset passes")
    void validateZeroDaysOffset()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "minDaysOffset", 0L,
                "maxDaysOffset", 0L));
    }

    @Test
    @DisplayName("maxDaysOffset smaller than minDaysOffset throws")
    void validateMaxDaysOffsetSmallerThanMin()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "minDaysOffset", 14L,
                        "maxDaysOffset", 1L),
                "maxDaysOffset", "minDaysOffset");
    }

    @Test
    @DisplayName("minDaysOffset wrong type throws")
    void validateMinDaysOffsetWrongType()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "minDaysOffset", "seven"),
                "minDaysOffset", "long");
    }

    @Test
    @DisplayName("maxDaysOffset wrong type throws")
    void validateMaxDaysOffsetWrongType()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "maxDaysOffset", "seven"),
                "maxDaysOffset", "long");
    }

    @Test
    @DisplayName("valid adjustTo startOfMonth passes")
    void validateAdjustToStartOfMonth()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "adjustTo", "startOfMonth"));
    }

    @Test
    @DisplayName("valid adjustTo endOfMonth passes")
    void validateAdjustToEndOfMonth()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "adjustTo", "endOfMonth"));
    }

    @Test
    @DisplayName("valid adjustTo startOfYear passes")
    void validateAdjustToStartOfYear()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "adjustTo", "startOfYear"));
    }

    @Test
    @DisplayName("valid adjustTo endOfYear passes")
    void validateAdjustToEndOfYear()
    {
        assertValidationPasses(Map.of(
                "reference", "date.fulldate",
                "dateFormat", "yyyy-MM-dd",
                "adjustTo", "endOfYear"));
    }

    @Test
    @DisplayName("invalid adjustTo value throws")
    void validateInvalidAdjustTo()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "adjustTo", "middleOfMonth"),
                "adjustTo");
    }

    @Test
    @DisplayName("adjustTo and minDaysOffset together throws")
    void validateAdjustToAndMinDaysOffsetMutuallyExclusive()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "adjustTo", "startOfMonth",
                        "minDaysOffset", 5L,
                        "maxDaysOffset", 5L),
                "adjustTo", "minDaysOffset");
    }

    @Test
    @DisplayName("adjustTo and maxDaysOffset together throws")
    void validateAdjustToAndMaxDaysOffsetMutuallyExclusive()
    {
        assertValidationFails(
                Map.of("reference", "date.fulldate",
                        "dateFormat", "yyyy-MM-dd",
                        "adjustTo", "endOfYear",
                        "minDaysOffset", 0L,
                        "maxDaysOffset", 10L),
                "adjustTo", "maxDaysOffset");
    }

    @Test
    @DisplayName("defaults are applied when no options specified")
    void validateDefaultsAreApplied() throws InvalidConfigurationException
    {
        FieldProcessor processor = buildProcessorWithDefaults("date.field");
        assertEquals(0L, processor.getFieldConfiguration().getOptions().get("minDaysOffset"));
        assertEquals(0L, processor.getFieldConfiguration().getOptions().get("maxDaysOffset"));
        assertNull(processor.getFieldConfiguration().getOptions().get("adjustTo"));
    }
}