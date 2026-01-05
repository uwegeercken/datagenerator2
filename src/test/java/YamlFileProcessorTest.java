import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.utilities.ConfigurationLoader;
import com.datamelt.utilities.datagenerator.config.CategoryFileLoader;
import com.datamelt.utilities.datagenerator.config.model.DataConfiguration;
import com.datamelt.utilities.datagenerator.config.process.DataFieldsProcessor;
import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlFileProcessorTest
{
    private static final String DATACONFIGURATION_TESTFILE_01 = "/datagenerator-test-01.yml";

    @Test
    @DisplayName("validate dataconfiguration")
    void validateConfiguration() throws Exception
    {
        Path resourcePath = Path.of(getClass().getResource(DATACONFIGURATION_TESTFILE_01).toURI());
        File dataConfigurationFile = new File(resourcePath.toString());
        DataConfiguration dataConfiguration;
        try (InputStream stream = new FileInputStream(dataConfigurationFile))
        {
            dataConfiguration = ConfigurationLoader.load(stream.readAllBytes(), DataConfiguration.class);
        }
        CategoryFileLoader.loadCategoryFiles(dataConfiguration);
        DataFieldsProcessor.processAllFields(dataConfiguration);
        RowBuilder rowBuilder = new RowBuilder(dataConfiguration);
        Try<Row> row = rowBuilder.generate();

        assertTrue(row.isSuccess(), "invalid configuration file");
    }
}