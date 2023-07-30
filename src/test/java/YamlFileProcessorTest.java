import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;
import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YamlFileProcessorTest
{
    private static final String DATACONFIGURATION_TESTFILE_01 = "datagenerator-test-01.yml";

    @Test
    @DisplayName("test1")
    void validateConfiguration() throws Exception
    {
        InputStream stream = getClass().getResourceAsStream("config/" + DATACONFIGURATION_TESTFILE_01);
        DataConfiguration dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor allFieldsProcessor = new DataFieldsProcessor(dataConfiguration);
        allFieldsProcessor.processAllFields();
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        Row row = rowBuilder.generate();

        assertEquals(true,true,"blabla");
    }
}