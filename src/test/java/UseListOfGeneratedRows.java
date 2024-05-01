import com.datamelt.utilities.datagenerator.application.DataGenerator;
import com.datamelt.utilities.datagenerator.error.Try;
import com.datamelt.utilities.datagenerator.generate.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class UseListOfGeneratedRows
{
    String dataConfigurationFilename = "/home/uwe/development/datagenerator2/datagenerator2.yml";

    @Test
    @DisplayName("test generate list of rows")
    void generateListOfRows() throws Exception
    {
        List<Row> rows =  DataGenerator.generateRows(dataConfigurationFilename, 10);
        assertTrue(() -> rows.size()==10);

    }

    @Test
    @DisplayName("test generate list of rows")
    void generateSingleRow() throws Exception
    {
        Try<Row> row =  DataGenerator.generateRow(dataConfigurationFilename);
        assertTrue(row::isSuccess);
    }


}
