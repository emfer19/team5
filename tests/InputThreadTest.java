import org.junit.Test;
import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.main.InputThread;

import static org.junit.Assert.*;

public class InputThreadTest {
    private String csvFilePath = "/home/cletus/sampleMarketData.csv";
    private CSVReader reader = new CSVReader(csvFilePath);
    private InputThread inputThread = new InputThread(reader);

    @Test
    public void run() {
        //Lets check if the run method goes well
        inputThread.run();
    }

    @Test
    public void pull() {
        assertNull(inputThread.pull());
    }

    @Test
    public void push() {
    }
}