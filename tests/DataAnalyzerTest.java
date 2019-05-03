import org.junit.Before;
import org.junit.Test;
import org.team5.app.main.DataAnalyzer;

import static org.junit.Assert.*;

public class DataAnalyzerTest {

    DataAnalyzer da = new DataAnalyzer();

    @Before
    public void init() {

        da.writeData(1,2);
        da.writeData(1,3);
        da.writeData(1,4);
        da.writeData(1,5);
        da.writeData(1,6);
        da.writeData(1,7);
        da.writeData(1,8);
        da.writeData(1,9);
        da.writeData(1,10);
    }

    @Test
    public void writeData() {
        System.out.println(da.printStats());
    }

    @Test
    public void mean() {

        double expectedValue = 5.0;
        double actualValue = da.mean();
        assertEquals(expectedValue, actualValue,0.0);
    }

    @Test
    public void percentile() {
        double[] expectedValues = {5.0,7.0,9.0,9.0};
        double[] actualValues = {da.percentile(50), da.percentile(75), da.percentile(90), da.percentile(99)};
        assertArrayEquals(expectedValues,actualValues,0.0);
    }

    @Test
    public void throughput() {
        double expectedValue = 0.2;
        double actualValue = da.throughput();
        assertEquals(expectedValue, actualValue,0.0);
    }
}