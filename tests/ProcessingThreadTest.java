import org.junit.Test;
import org.team5.app.main.ProcessingThread;

import static org.junit.Assert.*;

public class ProcessingThreadTest {

    int process_time = 5;
    ProcessingThread processingThread = new ProcessingThread(process_time);
    @Test
    public void push() {
        assertNull(processingThread.pull());
    }

    @Test
    public void pull() {
        assertNull(processingThread.pull());
    }

    @Test
    public void testRun() {
        processingThread.run();
    }
}