import org.junit.Test;
import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.main.BufferThread;

import static org.junit.Assert.*;

public class BufferThreadTest {

    @Test
    public void remove() {
    }

    @Test
    public void pull() {
        BufferThread bf = new BufferThread(1);
        assertEquals(new DataPoint(1.0,1),bf.pull());
    }

    @Test
    public void push() {

    }

    @Test
    public void setOutstream() {
    }

    @Test
    public void setInstream() {
    }
}