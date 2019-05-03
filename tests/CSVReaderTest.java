import org.junit.Before;
import org.junit.Test;
import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;

import static org.junit.Assert.*;

public class CSVReaderTest {
    private String csvFilePath = "/home/cletus/sampleMarketData.csv";
    private CSVReader reader = new CSVReader(csvFilePath);

    @Before
    public void init() {
        reader.setFilePath(csvFilePath);
    }

    @Test
    public void loadData() {
        reader.loadData();
    }

    @Test
    public void getDataSize() {
        //If successfully read data, print out the size
        System.out.println(reader.getDataSize());
    }

    @Test
    public void next() {
        DataPoint dataPoint = reader.next();
        System.out.println(dataPoint.asString());
    }
}