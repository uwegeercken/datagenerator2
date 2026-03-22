# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.5.2] - 2026-03-22

### Added
- `minDaysOffset` and `maxDaysOffset` options for `datereference` fields — add a fixed or random number of days to the referenced date. Negative values subtract days. Both default to `0` (no offset).
- `adjustTo` option for `datereference` fields — snap the referenced date to a boundary: `startOfMonth`, `endOfMonth`, `startOfYear`, `endOfYear`. Mutually exclusive with `minDaysOffset`/`maxDaysOffset`.
- `ADJUST_TO` added to `OptionKey`.
- `VALID_ADJUST_TO_VALUES` and `IS_VALID_ADJUST_TO` added to `OptionValidations`.
- Wiki pages: Nested Structures, Word Lists, updated Regular Expressions with pattern support warning.

### Changed
- `DateReferenceDefinition` — null-default options (`reference`, `dateFormat`, `adjustTo`) are now validated in the cross-option validator rather than via predicates, establishing the pattern that predicates only apply to options with non-null defaults.
- `FieldOption.validate()` — null guard re-added to skip validation when value is null.
- `DateReferenceGenerator` — applies days offset and `adjustTo` adjustment before formatting.

### Fixed
- `FieldOption.validate()` threw `NullPointerException` when `defaultValue` was null and a `ClassCastException` occurred during predicate evaluation.

---

## [0.5.1] - 2026-03-17

### Added
- `nullProbability` option for `randomstring`, `randomdate`, `randomtimestamp`, `datereference`, `regularexpression` and `randomuuid` field types. Accepts an integer between 0 (never null, default) and 100 (always null). Transformations are skipped when a null value is generated.
- Generation failure logging — failed row generations are now counted and reported at the end of a run.
- `IS_LONG` and `IS_PERCENTAGE` validators added to `OptionValidations`.
- Wiki with sample configurations for all major features.

### Fixed
- `TableLayout` static fields were never reset between runs, causing data corruption when `DataGenerator` was instantiated more than once in the same JVM.
- `generateRows()` no longer silently swallows exceptions — errors now propagate cleanly to `main()`.
- `appendField()` error message now correctly includes the unsupported type name instead of the raw `toString()` output.

### Changed
- `RowField.generateValue()` now accepts a `nullProbability` parameter.
- `DataStoreAppender.append()` and `appendStruct()` check for null values and call `appendNull()` before the type switch.
- `DataFieldsProcessor.processField()` syncs `nullProbability` from the options map to `FieldConfiguration` after processing.

---

## [0.5.0] - 2026-03-14

### Added
- Definition classes (`RandomStringDefinition`, `RandomLongDefinition`, `RandomDoubleDefinition`, `RandomDateDefinition`, `RandomTimestampDefinition`, `RandomUuidDefinition`, `DateReferenceDefinition`, `RegularExpressionDefinition`, `CategoryDefinition`) replacing all processor subclasses.
- `OptionValidations` — reusable predicate pool for option validation (`IS_POSITIVE_LONG`, `IS_NON_NEGATIVE_LONG`, `IS_NOT_EMPTY_STRING`, `IS_VALID_SIMPLE_DATE_FORMAT`, `IS_VALID_DATETIME_FORMAT`, `IS_VALID_REGEX_PATTERN`, `IS_LONG`).
- `CrossOptionValidator` and `ConfigurationPostProcessor` functional interfaces.
- All validation failures for a field are now collected and reported together rather than failing on the first error.
- `FieldType` custom Jackson deserializer — invalid field type values now produce a clear error message listing all valid types. Field types in yaml are now case-insensitive.
- `RowGenerator` programmatic API with lazy infinite stream via `Stream.generate()`.
- `DataStoreAppender` append failure counter and `lastAppendSucceeded` flag.
- `-cd` and `-ch` program arguments for CSV delimiter and header configuration.
- `-h` / `--help` program arguments.
- `rowNumberFieldName` configuration in program yaml — customizable name for the auto-generated row number column (default: `rownumber`).
- `InvalidConfigurationException` cause constructor for preserving stack traces when wrapping exceptions.
- Published to Maven Central as `io.github.uwegeercken:datagenerator2`.

### Changed
- `FieldProcessor` rewritten as a single concrete class — no more abstract subclasses per field type.
- `DataFieldsProcessor` simplified to instantiate `FieldProcessor` directly.
- `FieldOption` enhanced with predicate validator and error message.
- `FieldType` enum entries now carry all metadata (available transformations, output types, options, validators, post-processors) via Definition class references.
- `generateRows()` simplified — partitioning and batch methods removed, replaced with a single `LongStream` with `AtomicLong` counter.
- `logOutput()` changed from `logger.debug` to `logger.info`.
- `DataStoreAppender` append failures now logged as warnings and counted rather than silently swallowed.
- `getRowfield()` in `RowBuilder` now returns `Optional<RowField>`.
- `RowBuilder` constructor now declares `throws InvalidConfigurationException` instead of wrapping in `RuntimeException`.

### Removed
- All processor subclasses: `CategoryProcessor`, `RandomStringProcessor`, `RandomLongProcessor`, `RandomDoubleProcessor`, `RandomDateProcessor`, `RandomTimestampProcessor`, `RandomUuidProcessor`, `RegularExpressionProcessor`, `DateReferenceProcessor`.
- All per-type options enums: `RandomStringOptions`, `RandomLongOptions`, `RandomDoubleOptions`, `RandomDateOptions`, `RandomTimestampOptions`, `RandomUuidOptions`, `RegularExpressionOptions`, `DateReferenceOptions`.
- `getFieldProcessorFunction()` from `FieldType`.
- `generateRowsBatch()` and `getPartitions()` from `DataGenerator`.

### Fixed
- `BUG-1`: `RowField.generateValue()` generated value twice, discarding the first result.
- `BUG-2`: `TransformationExecutor.executeAll()` passed original value instead of accumulated transformed value to each step, breaking transformation chains.
- `BUG-3`: `DateUtility.getRandomDateTime()` used `DEFAULT_MAXDATE_HOUR` for the minute parameter instead of `DEFAULT_MAXDATE_MINUTE`.
- `BUG-4`: `RandomStringProcessor` rejected valid `minLength=0` with `<= 0` check. Fixed to `< 0`. Default changed to `1`.
- `BUG-5`: `Argument.NUMBEROFROWSTOGENERATE` description said "number of threads".
- `TransformationExecutor.execute()` error message always showed `null` for parameter values — now correctly captures and displays the actual parameter values.
- `TransformationExecutor.execute()` silently returned original value when method lookup failed — now throws `TransformationExecutionException` with a clear message.
- `CategoryGenerator` weight range `nextInt(1, 100)` caused `IndexOutOfBoundsException` when weights summed to 99 due to rounding. Fixed with bounds guard.

---

## [0.4.5] - 2026-02-28

### Added
- Initial public release on Maven Central.
- CSV, JSON, Excel and Parquet export support.
- DuckDB as internal storage engine with appender-based insertion.
- Field types: `category`, `randomstring`, `randomlong`, `randomdouble`, `randomdate`, `randomtimestamp`, `datereference`, `regularexpression`, `randomuuid`.
- Transformation pipeline with 15+ transformations.
- Nested struct support via dot notation in field names.
- Weighted random value selection for category fields.
- Category file (word list) support with automatic weight distribution.
- Parquet partitioning support.
- Command-line interface with mandatory and optional arguments.
- Statistics output for generated field value distribution.