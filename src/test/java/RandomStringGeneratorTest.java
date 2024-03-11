import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldConfiguration;
import com.datamelt.utilities.datagenerator.config.model.FieldType;
import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.config.process.InvalidConfigurationException;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RandomStringGeneratorTest
{
    private RowBuilder getRowBuilder(String fieldName, Map<String, Object> options) throws Exception
    {
        DataConfiguration dataConfiguration = new DataConfiguration();
        FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName);
        fieldConfiguration.setType(FieldType.RANDOMSTRING);
        fieldConfiguration.setOptions(options);
        List<FieldConfiguration> fields = new ArrayList<>();
        fields.add(fieldConfiguration);
        dataConfiguration.setFields(fields);
        DataFieldsProcessor allFieldsProcessor = new DataFieldsProcessor();
        allFieldsProcessor.processAllFields(dataConfiguration);
        return new RowBuilder(dataConfiguration);
    }

    @Test
    @DisplayName("testing exception when max length < min length")
    void validateErrorMaxLengthSmallerMinLength()
    {
        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 50L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 10L);

        assertThrows(InvalidConfigurationException.class,()->{
            RowBuilder rowBuilder = getRowBuilder("testfield", options);
        });
    }

    @Test
    @DisplayName("testing only defined characters are used")
    void validateRandomCharacters() throws Exception
    {
        String allowedCharacters = "nopqrstuvwxyz";

        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 150L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 150L);
        options.put(RandomStringOptions.RANDOM_CHARACTERS.getKey(), allowedCharacters);

        RowBuilder rowBuilder = getRowBuilder("testfield", options);
        Try<Row> row = rowBuilder.generate();
        String value = (String) row.getResult().getFields().get(0).getValue();
        int countInvalidCharacters = 0;
        for(int i=0;i< value.length();i++)
        {
            String singleChar = value.substring(i, i+1);
            if (!allowedCharacters.contains(singleChar))
            {
                countInvalidCharacters++;
            }
        }

        assertTrue(countInvalidCharacters == 0);

    }
    @Test
    @DisplayName("testing exception when min length <= 0")
    void validateErrorMinLengthSmallerEqualZero()
    {
        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 0L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 10L);

        assertThrows(InvalidConfigurationException.class,()->{
            RowBuilder rowBuilder = getRowBuilder("testfield", options);
        });
    }


    @Test
    @DisplayName("testing fixed length")
    void validateFixedLengthString() throws Exception
    {
        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), 10L);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), 10L);

        RowBuilder rowBuilder = getRowBuilder("testfield", options);
        Try<Row> row = rowBuilder.generate();
        String value = (String) row.getResult().getFields().get(0).getValue();
        assertEquals(10, value.length());
    }

    @Test
    @DisplayName("testing varying length between limits for 1000 cases")
    void validateVaryingLengthString() throws Exception
    {
        Long minValue = 10L;
        Long maxValue = 20L;

        Map<String, Object> options = new HashMap<>();
        options.put(RandomStringOptions.MIN_LENGTH.getKey(), minValue);
        options.put(RandomStringOptions.MAX_LENGTH.getKey(), maxValue);

        RowBuilder rowBuilder = getRowBuilder("testfield", options);
        for(int i=0;i<1000;i++)
        {
            Try<Row> row = rowBuilder.generate();
            String value = (String)row.getResult().getFields().get(0).getValue();
            assertTrue(value.length()>=minValue && value.length()<=maxValue, "length of value must correspond to minValue and mxValue");
        }
    }
}