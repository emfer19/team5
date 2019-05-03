import org.junit.Test;
import org.team5.app.gui.SwingUI;

import static org.junit.Assert.*;

public class SwingUITest {

    SwingUI swingUI = new SwingUI();

    @Test
    public void getRatePref() {
    }

    @Test
    public void getProcessorNumber() {
    }

    @Test
    public void getDefaultBufferSize() {
        double expectedValue = 1000000;
        double actualValue = swingUI.DEFAULT_BUFFER_SIZE;
        assertEquals(expectedValue, actualValue,0.0);
    }

    @Test
    public void getDefaultProcessTime() {
        double expectedValue = 1.0;
        double actualValue = swingUI.DEFAULT_PROCESS_TIME;
        assertEquals(expectedValue, actualValue,0.0);
    }
}