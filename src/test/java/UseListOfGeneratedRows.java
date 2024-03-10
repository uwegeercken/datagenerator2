import com.datamelt.utilities.datagenerator.application.DataGenerator;
import com.datamelt.utilities.datagenerator.generate.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UseListOfGeneratedRows
{
    String dataConfigurationFilename = "/home/uwe/development/datagenerator2/datagenerator2.yml";

    @Test
    @DisplayName("test1")
    void test1() throws Exception
    {
        List<Row> rows =  DataGenerator.generateRows(dataConfigurationFilename, 0);
        assertTrue(() -> rows.size()==0);

    }
}
